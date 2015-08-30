(ns hglog-stat-web.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hglog-stat.core :refer :all]
            [cheshire.core :refer :all]))

;(defn -main [ & args]
;  (let [data (analyze-file  "/home/roman/hglog2.txt")
;        grouping (group-by-info-fields data '(:branch))]
;    (clojure.pprint/pprint grouping)))

(defroutes app-routes
  (GET "/" [] (let [data (analyze-file  "/home/roman/hglog2.txt")
                    groups (group-by-info-fields data '(:branch))
                    sorted-groups (sort-by #(:lines-difference (:info %)) groups)]
                (generate-string sorted-groups {:pretty true})))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
