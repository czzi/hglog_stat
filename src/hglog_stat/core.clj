(ns hglog-stat.core
  (require [clojure.string :as str]))

(defn read-log-file [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (doall (line-seq rdr))))

(defn conj-not-empty [to-list x]
  (if (empty? x) to-list (conj to-list x)))

(defn trim-empty-lines [lines]
  (let [trim-from-start
        #(loop [x %]
           (cond
            (str/blank? (first x)) (recur (rest x))
            :else x))
        trimmed-from-start (trim-from-start lines)
        trimmed-from-end (trim-from-start (reverse trimmed-from-start))]
    (reverse trimmed-from-end)))

(defn group-by-commit-old [lines]
  (loop [lines-tail lines
         current-commit '()
         result '()]
    (let [current-line (first lines-tail)]
      (cond
       (nil? current-line)
         (conj-not-empty result current-commit)
       (str/blank? current-line)
         (recur (rest lines-tail) '() (conj-not-empty result current-commit))
       :else
         (recur (rest lines-tail) (conj current-commit current-line) result)))))

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

(defn split-commit-lines [commit-lines]
  (map #(str/split % #":\s+" 2) commit-lines))

(defn convert-commit-lines [splitted-lines]
  (let [maps-with-keywords (map #(into {} (conj () (conj [] (keyword (nth % 0)) (nth % 1)))) splitted-lines)]
    (into {} maps-with-keywords)))



(defn -main [ & args]
  (println
    (doall
     (map #(into {} %)
      (map split-commit-lines
        (group-by-commit (read-log-file "/home/roman/hglog.txt")))))))
