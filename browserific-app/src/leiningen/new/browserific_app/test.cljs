(ns {{name}}.test
    (:require-macros [cemerick.cljs.test
                      :refer (is deftest with-test run-tests testing test-var)])
    (:require [cemerick.cljs.test :as t]))


(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
