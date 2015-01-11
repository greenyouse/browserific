(ns browserific.helpers.plist
  "Helper file for converting a Clojure map to a plist"
  (:require [clojure.string :as s]))


(declare process-nodes)

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

(defmethod node-builder clojure.lang.PersistentVector [item]
  [:array (process-nodes item)])

(defmethod node-builder clojure.lang.PersistentArrayMap [item]
  [:dict (process-nodes item)])

(defmethod node-builder clojure.lang.MapEntry [item]
  (process-nodes item))


(defn- process-nodes
  "Takes a map and recursively converts all elements into the plist DSL
   for sexp-as-element"
  ([conf] (trampoline (process-nodes conf nil)))
  ([conf acc] (if (empty? conf) (reverse acc)
                  #(process-nodes (rest conf)
                         (conj acc (node-builder (first conf)))))))

(defn plist
  "Build a plist for a given map to be consumed by clojure.data.xml/sexp-as-element.
   This has a special token -.- to indicate whitespaces for keys. For example:

   [:key-.-with-.-spaces] ==> [:key \"key with spaces\"]"
  [conf]
  [:plist {:version "1.0"}
   (into [] (cons :dict (process-nodes conf)))])
