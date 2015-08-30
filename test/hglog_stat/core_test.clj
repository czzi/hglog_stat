(ns hglog-stat.core-test
  (:require [clojure.test :refer :all]
            [hglog-stat.core :refer :all]))

(deftest group-by-info-fields-no-grouping
  (testing "Group All"
    (is (= '({:info {} :summary {:field2 3}})
           (group-by-info-fields '(
                                   {:info {:field1 "value1"} :summary {:field2 1}}
                                   {:info {:field1 "value1"} :summary {:field2 2}})
                                 '()) ))))

(deftest group-by-info-fields-simple
  (testing "Simple"
    (is (= '({:info {:field1 "value1"} :summary {:field2 3}})
           (group-by-info-fields '(
                                   {:info {:field1 "value1"} :summary {:field2 1}}
                                   {:info {:field1 "value1"} :summary {:field2 2}})
                                 '(:field1)) ))))

(deftest group-by-info-fields-group-by-equal-field
  (testing "Group by equal fields"
    (is (= '({:info {:field1 "value1"} :summary {:field2 3}})
           (group-by-info-fields '(
                                   {:info {:field1 "value1" :field2 "valueX"} :summary {:field2 1}}
                                   {:info {:field1 "value1" :field2 "valueY"} :summary {:field2 2}})
                                 '(:field1))))))

(deftest group-by-info-fields-group-by-different-field
  (testing "Group by different fields"
    (is (= '(
             {:info {:field2 "valueX"} :summary {:field2 1}}
             {:info {:field2 "valueY"} :summary {:field2 2}})
           (group-by-info-fields '(
                                   {:info {:field1 "value1" :field2 "valueX"} :summary {:field2 1}}
                                   {:info {:field1 "value1" :field2 "valueY"} :summary {:field2 2}})
                                 '(:field2))))))


(deftest group-by-info-fields-group-by-multiple-fields
  (testing "Group by multiple fields"
    (is (= '(
             {:info {:field1 "value1" :field2 "valueX"} :summary {:field2 1}}
             {:info {:field1 "value1" :field2 "valueY"} :summary {:field2 2}})
           (group-by-info-fields '(
                                   {:info {:field1 "value1" :field2 "valueX"} :summary {:field2 1}}
                                   {:info {:field1 "value1" :field2 "valueY"} :summary {:field2 2}})
                                 '(:field1 :field2))))))
