(ns woot.client
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [goog.events :as events]
              [goog.dom :as gdom]
              [cljs.core.async :as async :refer [put! <! >! chan timeout]]
              [sablono.core :as sa :refer-macros [html]]
              [secretary.core :as secretary :include-macros true :refer [defroute]]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [om-sync.core :refer [om-sync]]
              [om-sync.util :refer [tx-tag edn-xhr]]))

(enable-console-print!)


(def app-state
  (atom {}))


(defn display
  "hide/show an element using inline css"
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn on-edit
  "Send the change back to the server"
  [id title]
  (edn-xhr
    {:method :put
     :url (str "class/" id "/update")
     :data {:class/title title}
     :on-complete
     (fn [res]
       (println "server response:" res))}))

(defn uuid [] (rand 100000))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Render Dispatch fns

(defn help-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:helper false})
    om/IRenderState
    (render-state [_ helper]
                  (html [:div
                         [:button {:onClick #(om/set-state! owner :helper true)}
                          "?"]
                         [:div {:class "help"
                                :style (display (:helper helper))}
                          [:button {:class "close-btn"
                                    :onClick #(om/set-state! owner :helper false)}
                           "xclosex"]
                          [:p (str (:help-text app))]
                          [:br]
                          (if-not (nil? (:help-url app))
                            (html [:p (str "For more information go here: ")
                                   [:a (:help-url app)]]))]]))))

(defn name-commit-change
  "Re-render a widget with some new state"
  [app e owner]
  (let [name-el (om/get-node owner "name")]
    (om/set-state! owner :text js/e.target.value)
    (om/update! app :value js/e.target.value :update)
    (set! (.-value name-el) "")))

;; DOM dispatcher
;; FIXME: make an input button (this is a pain because the cursor cannot be pased to commit-change)
(defn name-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:text (:value app)})
    om/IRenderState
    (render-state [_ state]
            (html [:section
                   [:h4 (:label app)]
                   [:p (:text state)]
                   [:input {:type "text"
                            :ref "name"
                            :onKeyPress (fn [e]
                                          (when (== 13 js/e.keyCode)
                                            (name-commit-change app e owner)))}]
                   (om/build help-view app)]))))

(defn list-commit-change [app e state owner]
  (let [list-el (om/get-node owner "list")]
    (om/set-state! owner :items (conj (:value @app) js/e.target.value))
    (om/transact! app :value #(conj % js/e.target.value) :update)
    (set! (.-value list-el) "")))

(defn list-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:items (:value app)})
    om/IRenderState
    (render-state [_ state]
                  (html [:section
                         [:div
                          [:h4 (:label app)]
                          (apply dom/ul nil
                                 (map #(dom/li nil %) (:items state)))]
                         [:input {:type "text"
                                  :ref "list"
                                  :onKeyPress (fn [e]
                                                (when (== 13 js/e.keyCode)
                                                  (list-commit-change app e state owner)))}]
                         (om/build help-view app)]))))

(defn checkbox-view [app owner]
  (reify om/IRender
    (render [_]
            (html [:section
                   [:input {:type "checkbox"
                            :id (:id app)}
                    (:value app)]
                   [:div [:p (:help-text app)]
                    [:a (:help-url app)]]]))))

(defn radio-view [app owner]
  (reify om/IRender
    (render [_]
            (html [:section
                   [:input {:type "radio"
                            :id (:id app)}
                    (:value app)]
                   [:div [:p (:help-text app)]
                    [:a (:help-url app)]]]))))

(defn file-view [app owner]
  (reify om/IRender
    (render [_]
            (html [:section
                   [:input {:type "file"
                            :id (:id app)}
                    (:value app)]
                   [:div [:p (:help-text app)]
                    [:a (:help-url app)]]]))))


(defmulti content-type :dom-type)

(defmethod content-type :name-entry
    [app owner] (name-view app owner))

(defmethod content-type :list
  [app owner] (list-view app owner))

(defmethod content-type :checkbox
    [app owner] (checkbox-view app owner))

(defmethod content-type :radio
    [app owner] (radio-view app owner))

(defmethod content-type :file-input
    [app owner] (file-view app owner))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Root and General fns

;; app-state -> page-dispatch -> build-all -> dom dispatcher
;; onclick handler -> id dispatch -> dispatch fns -> update dom
;; Embed the page dispatcher here!
(defn config-app [app owner]
  (html [:div
         [:h2 "woot woot"]
         (map #(om/build content-type %) (:general app))]))

(let [tx-chan (chan)
      tx-pub-chan (async/pub tx-chan (fn [_] :txs))]
  (edn-xhr
    {:method :get
     :url "/init"
     :on-complete
     (fn [res]
       (reset! app-state res)
       (swap! app-state assoc :showing :general) ;load general page when starting
       (om/root config-app app-state
         {:target (gdom/getElementByClass "content")
          :shared {:tx-chan tx-pub-chan}
          :tx-listen
          (fn [tx-data root-cursor]
            (put! tx-chan [tx-data root-cursor]))}))}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Hacking/Dev section (deleteme)

(comment
  (def dump
    {:general
     [{:id :name :value "woot" :dom-type :text-box :help-text "The name of your app" :help-url nil}
      {:id :author:author-name :value "me" :dom-type :text-box :help-text "Your name." :help-url nil}
      {:id :author:author-id :value "me.woot.corp" :dom-type :text-box :help-text "Your id or company id." :help-url nil}
      {:id :author:email :value "g@gmail.com" :dom-type :text-box :help-text "Your email address." :help-url nil}
      {:id :author:url :value "wootwoot.com" :dom-type :text-box :help-text "Your website." :help-url nil}
      {:id :author:contributors :value ["lambda" "fns"] :dom-type :text-box :help-text "Any contributors on the project." :help-url nil}]}
    ))


;; this isn't working with cursors
(comment
  ;; TODO: fill in the dispaches here
  (defn click-dispatcher [e]
    (cond
     (not= "" js/e.target.id) (om/transact! app-state )
     js/e.target.dataset.help (println (str "help -- " js/e.target.dataset.help))
     js/e.target.dataset.page (println (str "page -- " js/e.target.dataset.page))))

  (comment (println (str "id -- " js/e.target.id)))
  ;; Set a DOM level 4 event listener
  (events/listen (gdom/getDocument) "click" click-dispatcher)

  ;; a little clunky but oh well
  (def schema
  {:author:email (first (om/transact! app-state ))})
  )
