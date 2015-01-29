(ns browserific.config.trans
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [cljs.core.async :as async :refer [put! <! chan]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:import [goog.net XhrIo]
           goog.net.EventType
           [goog.events EventType]))

(defn dissoc-in
  [m [k & ks]]
  (if (and ks m)
    (assoc m k (dissoc-in (get m k) ks))
    (dissoc m k)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; XHR Helper

;; taken from om-sync
(defn edn-xhr [{:keys [method url data on-complete on-error]}]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.SUCCESS
      (fn [e]
        (on-complete (reader/read-string (.getResponseText xhr)))))
    (events/listen xhr goog.net.EventType.ERROR
      (fn [e]
        (on-error {:error (.getResponseText xhr)})))
    (. xhr
      (send url method (when data (pr-str data))
        #js {"Content-Type" "application/edn" "Accept" "application/edn"}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transactor

(def trans-chan
  (chan))

(go-loop []
  (let [data (<! trans-chan)]
    (edn-xhr {:method "PUT"
              :url "/config"
              :data {:value data}
              ;; NOTE: good place for XHR debugging
              :on-complete (fn [] (do (println "OK")
                                     (println data)))
              :on-error (fn [e] (do (println "error!")
                                   (js/console.log e)))}))
  (recur))

(defn add-item
  "This evaluates some update to a reagent cursor and then puts the entire
  database on tras-chan so it can be written to config.edn."
  [f cursor v]
  (let [s (f cursor v)]
    (put! trans-chan s)
    s))

(defn rm-item
  "This remove an item from a cursor and saves the results to config.edn."
  [f cursor & k]
  (let [s (f cursor (first k))]
    (put! trans-chan s)
    s))
