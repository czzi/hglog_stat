(ns hglog-stat.core
  (require [clojure.string :refer :all]))

(defn read-log-file [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (doall (line-seq rdr))))

(defn conj-not-empty [to-list x]
  (if (empty? x) to-list (conj to-list x)))

(defn group-by-commit [lines]
  (loop [lines-tail lines
         current-commit '()
         result '()]
    (let [current-line (first lines-tail)]
      (cond
       (nil? current-line)
         (conj-not-empty result current-commit)
       (blank? current-line)
         (recur (rest lines-tail) '() (conj-not-empty result current-commit))
       :else
         (recur (rest lines-tail) (conj current-commit current-line) result)))))

(defn split-commit-lines [lines]
  (map #(split % #":\s+" 2) lines))

(defn -main [ & args]
  (doall
    (map convert-commit-to-map
      (group-by-commit (read-log-file "/home/roman/hglog.txt")))))
