(ns hglog-stat.core-test
  (:require [clojure.test :refer :all]
            [hglog-stat.core :refer :all]))

(deftest group-by-info-fields-no-grouping
  (testing "No Grouping"
    (is (= '({:info {} :summary {:field2 1}} {:info {} :summary {:field2 2}})
          (group-by-info-fields '({:info {:field1 "value1"} :summary {:field2 1}} {:info {:field1 "value1"} :summary {:field2 2}}) '()) ))))

(deftest group-by-info-fields-simple
  (testing "Simple"
    (is (= '({:info {:field1 "value1"} :summary {:field2 3}})
          (group-by-info-fields '({:info {:field1 "value1"} :summary {:field2 1}} {:info {:field1 "value1"} :summary {:field2 2}}) '(:field1)) ))))
