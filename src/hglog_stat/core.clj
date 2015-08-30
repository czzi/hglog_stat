(ns hglog-stat.core
  (require [hglog-stat.parse :refer :all])
  (require [clojure.pprint]))

(defn group-by-info-fields [commits fields]
  (let [grouping-fn (fn [commit] (map (fn [field] (field (:info commit))) fields))
        groups (group-by grouping-fn commits)
        reduce-fn (partial merge-with +)
        convert-partition-fn (fn [the-group]
                               (let [info (zipmap fields (nth the-group 0))
                                     summaries (map #(:summary %) (nth the-group 1))
                                     aggregated-summary (reduce reduce-fn summaries)]
                                 {:info info :summary aggregated-summary}))]
    (map convert-partition-fn groups)))

(defn -main [ & args]
  (let [raw-file (read-log-file "/home/roman/hglog2_short.txt")
        prepared-commits (prepare-commits-info raw-file)]
    (clojure.pprint/pprint (add-changes-summary-to-commits prepared-commits))))

