(ns hglog-stat.parse
  (require [clojure.string :as str]))

(defn read-log-file [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (doall (line-seq rdr))))

(defn trim-empty-lines [lines]
  (let [trim-from-start
        #(loop [x %]
           (cond
            (str/blank? (first x)) (recur (rest x))
            :else x))
        trimmed-from-start (trim-from-start lines)
        trimmed-from-end (trim-from-start (reverse trimmed-from-start))]
    (reverse trimmed-from-end)))

(defn group-by-commit [lines]
  (loop [lines-tail lines
         current-commit {:info [], :changes []}
         current-mode :info
         result '()]
    (let [current-line (first lines-tail)]
      (cond
       (nil? current-line)
         (conj result current-commit)
       (str/blank? current-line)
         (case current-mode
            :info (recur (rest lines-tail) current-commit :changes result)
            :changes (recur (rest lines-tail) {:info [], :changes []} :info (conj result current-commit)))
       :else
         (recur (rest lines-tail)
                (assoc current-commit
                  current-mode
                  (conj (current-mode current-commit) current-line))
                current-mode
                result)))))

(defn split-info-lines [info-lines]
  "Split each line in the list from 'key: value' to ['key' 'value']"
  (map #(str/split % #":\s+" 2) info-lines))

(defn convert-info-lines [splitted-lines]
  "Convert info list from (['key1' 'value1'] ['key2' 'value2'] to {:key1 'value1' :key2 'value2'})"
  (let [maps-with-keywords
        (map #(into {} (conj
                        ()
                        (conj [] (keyword (nth % 0)) (nth % 1))))
             splitted-lines)]
    (into {} maps-with-keywords)))

(defn prepare-commits-info [raw-file]
  (let [trimmed-file (trim-empty-lines raw-file)
        lines-groupped-by-commit (group-by-commit trimmed-file)
        commits-with-converted-info (map #(assoc % :info (convert-info-lines (split-info-lines (:info %)))) lines-groupped-by-commit)]
    (reverse commits-with-converted-info)))

(defn get-changes-summary [commit]
  (let [last-line (last (:changes commit))
        matches (map read-string (rest (re-matches #"\s+(\d+) files changed, (\d+) insertions\(\+\), (\d+) deletions\(\-\)" last-line)))]
    {:affectedFiles (nth matches 0), :insertions (nth matches 1), :deletions (nth matches 2)}))

(defn add-changes-summry-to-commits [commits]
  (map #(assoc % :summary (get-changes-summary %)) commits))
