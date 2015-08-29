(ns hglog-stat.parse-test
  (:require [clojure.test :refer :all]
            [hglog-stat.parse :refer :all]))

(deftest trim-empty-lines-test
  (testing "Empty lines are removed from the start and the end of the list"
    (is (= '("1" "" "2" "3")
           (trim-empty-lines '("" "" "1" "" "2" "3" "" "" ""))))))

(deftest group-by-commit-simple-test
  (testing "Simple"
    (is (= '({:info ["branch: anotherBranch"] :changes ["change3"]}
             {:info ["branch: theBranch" "author: theAuthor"] :changes ["change1" "change2"]})
          (group-by-commit '(
                             "branch: theBranch"
                             "author: theAuthor"
                             ""
                             "change1"
                             "change2"
                             ""
                             "branch: anotherBranch"
                             ""
                             "change3"))))))

(deftest split-info-lines-simple
  (testing "Simple"
    (is (= '(["key1" "value1"] ["key2" "value2"])
           (split-info-lines '("key1: value1" "key2:  value2"))))))

(deftest split-info-lines-split-only-by-first-separator
  (testing "Split only by first separator"
    (is (= '(["key1" "the reason: some comment"] ["key2" "value2"])
           (split-info-lines '("key1: the reason: some comment" "key2:  value2"))))))

(deftest convert-info-lines-simple
  (testing "Simple"
    (is (= {:key1 "value1" :key2 "value2"}
        (convert-info-lines '(["key1" "value1"] ["key2" "value2"]))))))

(deftest prepare-commits-info-simple
  (testing "Simple"
    (is (= '({:info {:branch "branch1", :author "author1"},
              :changes ["changes1" "changes2"]}
             {:info {:branch "branch2"},
              :changes ["changes3"]})
           (prepare-commits-info '(""
                                   "branch: branch1"
                                   "author: author1"
                                   ""
                                   "changes1"
                                   "changes2"
                                   ""
                                   "branch: branch2"
                                   ""
                                   "changes3"))))))

(deftest get-changes-summary-simple
  (testing "Simple"
    (is (= {:affectedFiles 2, :insertions 4, :deletions 3}
           (get-changes-summary {:changes ["line1" "line2" " 2 files changed, 4 insertions(+), 3 deletions(-)"]})))))


(deftest add-changes-summry-to-commits-simple
  (testing "Simple"
    (is (= '({:changes ["line1" "line2" " 2 files changed, 4 insertions(+), 3 deletions(-)"], :summary {:affectedFiles 2, :insertions 4, :deletions 3}})
           (add-changes-summry-to-commits '({:changes ["line1" "line2" " 2 files changed, 4 insertions(+), 3 deletions(-)"]}))))))

;(run-tests)
