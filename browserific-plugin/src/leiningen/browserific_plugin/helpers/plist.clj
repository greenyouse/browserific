(ns leiningen.browserific-plugin.helpers.plist
  "Helper file for converting a Clojure map to a plist"
  (:require [clojure.walk :as w]
            [clojure.string :as s])
  (:use utils))

;; TODO: don't forget to delete these notes after we're completely done with
;; plist stuff
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NOTES

;; Basic plist elements are specified like this:
;;
;; <string> 	UTF-8 encoded string
;; <real>, <integer> 	Non-decimal or Decimal
;; <true/> or <false/> 	No data (tag only)
;; <date> 	ISO 8601 formatted string
;; <data> 	Base64 encoded data
;; <array> 	Can contain any number of child elements
;; An empty array may be represented as <array/>
;; <dict> 	Alternating <key> tags and plist element tags

;; All possible plist types
{:string "hello world" ; java.lang.String
 :real 12345 ; java.lang.Long
 :integer 10.10 ; doesn't get used by safari
 :true true ; java.lang.Boolean
 :false false ; java.lang.Booolean
 :date ; not really necessary, omit for now
 :array [1 2 3] ; clojure.lang.PersistenVector
 :dict {:more "stuff"} ; clojure.lang.PersistentArrayMap
 }

;; Input
{:string "hello world" ; java.lang.String
 :real 12345 ; java.lang.Long
 :true true ; java.lang.Boolean
 :false false ; java.lang.Booolean
 :array [:some "data"] ; clojure.lang.PersistenVector
 :dict {:more "stuff"} ; clojure.lang.PersistentArrayMap
 }

;; Output to be passed to xml-sexp-as-element
[:dict
 [:key "string"] [:string "hello world"]
 [:key "real"] [:real 12345]
 [:key "true"] [:true]
 [:key "false"] [:false]
 [:array [:key "more"] [:string "stuff"]]
 [:dict [:key "some"] [:string "stuff"]]]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Plist funcitons

;; FIXME: change to node-builder later
(defmulti node-builder (fn [item] (type item)))

(defmethod node-builder clojure.lang.Keyword [item]
  ;; expand tokens for whitespaces here
  [:key (s/replace (subs (str item) 1)
                   "-.-" " ")])

(defmethod node-builder java.lang.String [item]
  [:string item])

(defmethod node-builder java.lang.Long [item]
  [:real item])

(defmethod node-builder java.lang.Boolean [item]
  (if item
    [:true]
    [:false]))

;; TODO: delete these if they are never used
(defmethod node-builder clojure.lang.PersistentVector [item]
  [:array (woot item)])

(defmethod node-builder clojure.lang.PersistentArrayMap [item]
  [:dict (woot item)])

(defmethod node-builder clojure.lang.MapEntry [item]
  (woot item))

(defn woot
  ([conf] (trampoline (woot conf nil)))
  ([conf acc] (if (empty? conf) acc
                  #(woot (rest conf)
                         (conj acc (node-builder (first conf)))))))

(defn plist [conf]
  (into [] (cons :dict (woot conf))))


;; TODO: Maybe there should be a macro so we can break up unnecessary vectors?

(plist [:string "hello world"
         :real 12345
         :true true
         :false false
         :array [{:some "data"} {:woot "woot"}]
         :dict {:more "stuff"}])
