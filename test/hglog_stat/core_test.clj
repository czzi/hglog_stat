(ns hglog-stat.core-test
  (:require [clojure.test :refer :all]
            [hglog-stat.core :refer :all]))

(deftest group-by-commit-simple-test
  (testing "Simple"
    (is (= '(("5" "4") ("3") ("2" "1"))
          (group-by-commit '("1" "2" "" "3" "" "4" "5"))))))


(deftest group-by-commit-empty-commit
  (testing "Empty commit is dropped"
    (is (= '(("2" "1"))
          (group-by-commit '("1" "2" "" ""))))))

(deftest split-commit-lines-simple
  (testing "Simple"
    (is (= '(["key1" "value1"] ["key2" "value2"])
           (split-commit-lines '("key1: value1" "key2:  value2"))))))

(deftest split-commit-lines-split-only-by-first-separator
  (testing "Split only by first separator"
    (is (= '(["key1" "the reason: some comment"] ["key2" "value2"])
           (split-commit-lines '("key1: the reason: some comment" "key2:  value2"))))))

;(deftest convert-commit-to-map-simple
 ; (testing "Simple"
  ;  (is (= {"key1" "value1", "key2" "value2"}
   ;        (convert-commit-to-map '("key1: value1" "key2:  value2"))))))
;(run-tests)
