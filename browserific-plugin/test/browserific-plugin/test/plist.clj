(ns browserific-plugin.test.plist
  (:require [leiningen.browserific-plugin.helpers.plist :refer :all]
            [clojure.test :refer :all]))

(deftest plist-array
  "start with an array"
  []
  (is (= (plist [:oh "nohes" :dict {:miyagi "style"}])
         [:dict
          [:key "oh"] [:string "nohes"]
          [:dict [:key "miyagi"] [:string "style"]]])))

(deftest plist-map
  "start with a map"
  []
  (is (= (plist {:hi "dude" :array [:oh "nohes" :dict {:miyagi "style"}]})
         [:dict
          [:key "hi"] [:string "dude"]
          [:array
           [:key "oh"] [:string "nohes"]
           [:dict [:key "miyagi"] [:string "style"]]]])))

(deftest plist-all
  "test all the plist elements we'll use"
  []
  (is (= (plist  {:string "hello world"
                   :real 12345
                   :true true
                   :false false
                   :array [{:some "data"} {:woot "woot"}]
                  :dict {:more "stuff"}})
         [:dict
          [:key "string"] [:string "hello world"]
          [:key "real"] [:real 12345]
          [:key "true"] [:true]
          [:key "false"] [:false]
          [:array
           [:dict [[:key "some"] [:string "data"]]]
           [:dict [[:key "woot"] [:string "woot"]]]]
          [:dict [:key "more"] [:string "stuff"]]])))

(run-tests)
