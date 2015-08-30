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

(defn analyze-file [file]
  (let [raw-file (read-log-file file)
        prepared-commits (prepare-commits-info raw-file)
        summaries (add-changes-summary-to-commits prepared-commits)]
    summaries))

(defn -main [ & args]
  (let [data (analyze-file  "/home/roman/hglog2.txt")
        grouping (group-by-info-fields data '(:branch))]
    (clojure.pprint/pprint grouping)))

