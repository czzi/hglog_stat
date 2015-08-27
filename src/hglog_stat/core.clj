(ns hglog-stat.core
  (require [clojure.string :as str])
  (require [clojure.pprint]))

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
         current-commit {}
         current-mode :info
         result '()]
    (let [current-line (first lines-tail)]
      (cond
       (nil? current-line)
         (conj result current-commit)
       (str/blank? current-line)
         (case current-mode
            :info (recur (rest lines-tail) current-commit :changes result)
            :changes (recur (rest lines-tail) {} :info (conj result current-commit)))
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



(defn -main [ & args]
  (let [raw-lines (read-log-file "/home/roman/hglog2_short.txt")
        lines-groupped-by-commit (group-by-commit raw-lines)]
    (clojure.pprint/pprint lines-groupped-by-commit)))
  ;(println
   ; (doall
    ; (map #(into {} %)
     ; (map split-info-lines
      ;  (group-by-commit (read-log-file "/home/roman/hglog2_short.txt")))))))
