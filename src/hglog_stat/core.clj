(ns hglog-stat.core
  (require [hglog-stat.parse :refer :all])
  (require [clojure.pprint]))

(defn -main [ & args]
  (let [raw-file (read-log-file "/home/roman/hglog2_short.txt")
        prepared-commits (prepare-commits-info raw-file)]
    (clojure.pprint/pprint (add-changes-summry-to-commits prepared-commits))))

