(defproject hglog_stat "0.1.0-SNAPSHOT"
  :description "Hg log statistics builder"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :profiles {:dev {:plugins [[com.jakemccrary/lein-test-refresh "0.10.0"]]}}
  :main hglog-stat.core)
