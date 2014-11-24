(ns browserific.config.client
  "General and Cordova widgets constructed with Om views and
  om-sync support."
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [browserific.config.sync :as sync]
            [cljs.core.async :as async :refer [put! <! chan]]
            [sablono.core :as sa :refer-macros [html]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-sync.util :refer [edn-xhr tx-tag]]))

(enable-console-print!)

(def app-state
  (atom {}))

(defn display
  "hide/show an element using inline css"
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn member? [i coll]
  (some #(= i %) coll))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Widgets

(defn help-view
  "Displays the relevant help information for a config option"
  [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:helper false})
    om/IRenderState
    (render-state [_ {:keys [helper]}]
      (html [:div
             [:div [:button {:onClick #(om/set-state! owner :helper true)} "?"]
              [:button {:class "close-btn"
                        :style (display helper)
                        :onClick #(om/set-state! owner :helper false)}
               "xclosex"]]
             [:div {:class "help"
                    :style (display helper)}
              [:p (str (:help-text app))]
              [:br]
              (if-not (nil? (:help-url app))
                (html [:p (str "For more information go here: \n")
                       [:a {:href (:help-url app) :target "_blank"}
                        (:help-url app)]]))]]))))


(defmulti widget :dom-type)

(defmethod widget :name [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:text (:value app)
       :actions (chan)})
    om/IWillMount
    (will-mount [_]
      (let [actions (om/get-state owner :actions)]
        (go-loop []
          (let [[tag edn] (<! actions)]
            (case tag
              :add (do (om/set-state! owner :text edn)
                       (om/transact! app :value #(identity edn) :update))
              :rm (do (om/set-state! owner :text "")
                      (om/transact! app :value #(str "") :delete))
              :num (do (om/set-state! owner :text edn)
                       (om/transact! app :value #(int edn) :update)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [text actions]}]
      (html [:section
             [:h4 (:label app)]
             (if-not (or (nil? text) (= "" text))
               [:div [:span text]
                [:button {:onClick #(put! actions [:rm app])} "x"]])
             [:input {:type "text"
                      :ref "name"
                      :onKeyDown  (fn [e] (when (== 13 js/e.keyCode)
                                           (let [el (om/get-node owner "name")
                                                 v (.-value el)]
                                             (if (:number @app)
                                               (put! actions [:num v])
                                               (put! actions [:add v]))
                                             (set! (.-value el) ""))))}
              [:button {:onClick  #(let [el (om/get-node owner "name")
                                         v (.-value el)]
                                     (put! actions [:add v])
                                     (set! (.-value el) ""))} "Add"]]
             (om/build help-view app)]))))

(defmethod widget :list [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:items (:value app)
       :actions (chan)})
    om/IWillMount
    (will-mount [_]
      (let [actions (om/get-state owner :actions)]
        (go-loop []
          (let [[tag edn] (<! actions)]
            (if (= :add tag)
              (do (om/set-state! owner :items (vec (conj (:value @app) edn)))
                  (om/transact! app :value (fn [xs] (vec (conj xs edn))) :update))
              (do  (om/set-state! owner :items (vec (remove #(= % edn) (:value @app))))
                   (om/transact! app :value
                                 (fn [xs] (vec (remove #(= % edn) xs))) :delete)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [items actions]}]
      (html [:section
             [:h4 (:label app)]
             (reduce (fn [acc x]
                       (conj acc
                             [:li [:span x]
                              [:button {:onClick #(put! actions [:rm x])} "x"]]))
                     [:ul] items)
             [:input {:type "text"
                      :ref "list"
                      :onKeyDown (fn [e] (when (== 13 js/e.keyCode)
                                          (let [el (om/get-node owner "list")
                                                v (.-value el)]
                                            (put! actions [:add v])
                                            (set! (.-value el) ""))))}
              [:button {:onClick #(let [el (om/get-node owner "list")
                                        v (.-value el)]
                                    (put! actions [:add v])
                                    (set! (.-value el) ""))}
               "Add"]]
             (om/build help-view app)]))))

(defmethod widget :checkbox [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:value (:value app)
       :marking (chan)})
    om/IWillMount
    (will-mount [_]
      (let [marking (om/get-state owner :marking)]
        (go-loop []
          (let [x (<! marking)]
            (om/set-state! owner :value x)
            (om/transact! app :value #(identity x) :update)
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [value marking]}]
      (html [:section
             [:h4 (:label app)]
             [:input {:type "checkbox" ;handling nil is annoying for checkboxes
                      :checked (if (true? value) "checked" false)
                      :onClick #(put! marking (if (or (false? value) (nil? value))
                                                true false))}]
             (om/build help-view app)]))))

(defmethod widget :checkbox-list [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:items (vec (:value app))
       :boxes (:boxes app)
       :marking (chan)})
    om/IWillMount
    (will-mount [_]
      (let [marking (om/get-state owner :marking)]
        (go-loop []
          (let [[tag edn] (<! marking)]
            (if (= :add tag)
              (do (om/set-state! owner :items (vec (conj (:value @app) edn)))
                  (om/transact! app :value (fn [xs] (vec (conj xs edn))) :update))
              (do (om/set-state! owner :items (vec (remove #(= % edn) (:value @app))))
                  (om/transact! app :value (fn [xs] (vec (remove #(= % edn) xs))) :delete)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [items boxes marking]}]
      (html [:section
             [:h4 (:label app)]
             (reduce (fn [acc x]
                       (conj acc
                             [:input {:type "checkbox" :checked (member? x items)
                                      :onClick #(put! marking (if (member? x items)
                                                                [:rm x] [:add x]))}
                              x] [:br]))
                     [:div] boxes)
                 (om/build help-view app)]))))

(defmethod widget :file [app owner]
  (om/component
   (html [:section
          [:h4 (:label app)]
          [:input {:type "file"
                   :ref "filename"
                   :onChange (fn [e]
                               (om/transact! app :value #(identity js/e.target.value) :update))}
           (:value app)
           [:button {:onClick (fn [e]
                                (let [el (om/get-node owner "filename")]
                                  (om/transact! app :value #(str "") :update)
                                  (set! (.-value el) "")))}
            "x"]]
          (om/build help-view app)])))

(defmethod widget :cordova-pref [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:items (->> (:value app) (vec) (map pr-str))
       :actions (chan)})
    om/IWillMount
    (will-mount [_]
      (let [actions (om/get-state owner :actions)]
        (go-loop []
          (let [[tag edn] (<! actions)]
            (if (= tag :add)
              (do (om/set-state! owner :items (vec (conj (:value @app) edn)))
                  (om/transact! app :value (fn [items] (vec (conj items edn))) :update))
              (do (om/set-state! owner :items (vec (remove #(= % edn) (:value @app))))
                  (om/transact! app :value (fn [items] (vec (remove #(= % edn) items))) :delete)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [items actions]}]
      (html [:section
             [:h4 (:label app)]
             (reduce (fn [acc x]
                       (conj acc
                             [:li [:span (str x)]
                              [:button {:onClick #(put! actions [:rm x])} "x"]]))
                     [:ul] items)
             [:div
              [:p "name: " [:input {:type "text" :ref "pref1"}]]
              [:p "value: " [:input {:type "text" :ref "pref2"
                                     :onKeyDown  (fn [e] (when (== 13 js/e.keyCode)
                                                          (let [el1 (om/get-node owner "pref1")
                                                                el2 (om/get-node owner "pref2")
                                                                val1 (.-value el1)
                                                                val2 (.-value el2)]
                                                            (put! actions [:add {:name val1 :value val2}])
                                                            (set! (.-value el1) "") (set! (.-value el2) ""))))}]]
              [:button {:onClick #(let [el1 (om/get-node owner "pref1")
                                        el2 (om/get-node owner "pref2")
                                        val1 (.-value el1)
                                        val2 (.-value el2)]
                                    (put! actions [:add {:name val1 :value val2}])
                                    (set! (.-value el1) "") (set! (.-value el2) ""))}
               "Add"]]
             (om/build help-view app)]))))

(defmethod widget :cordova-plugin [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:items (vec (:value app))
       :customs (reduce (fn [acc x]
                          (if-not  (member? (:name x) (vec (:boxes app)))
                            (conj acc (pr-str x))))
                        [] (vec (:value app)))
       :boxes (vec (:boxes app))
       :actions (chan)})
    om/IWillMount
    (will-mount [_]
      (let [actions (om/get-state owner :actions)]
        (go-loop []
          (let [[tag edn] (<! actions)]
            (case tag
              :add (do (om/set-state! owner :items (vec (conj (:value @app) {:name edn :value true})))
                       (om/transact! app :value (fn [items] (vec (conj items {:name edn :value true}))) :update))
              :rm (do (om/set-state! owner :items (vec (remove #(= % {:name edn :value true}) (:value @app))))
                      (om/set-state! owner :customs (vec (remove #(= % edn) (om/get-state owner :customs))))
                      (om/transact! app :value (fn [items] (vec (remove #(= % {:name edn :value true}) items))) :delete))
              :cust (do (om/set-state! owner :items (vec (conj (:value @app) {:name edn :value true})))
                        (om/set-state! owner :customs (vec (conj (om/get-state owner :customs) edn)))
                        (om/transact! app :value (fn [items] (vec (conj items {:name edn :value true}))) :update)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [items customs boxes actions]}]
      (html [:section
             [:h4 (:label app)]
             (reduce (fn [acc x]
                       (conj acc
                             [:input {:type "checkbox" :checked (member? {:name x :value true} items)
                                      :onClick #(put! actions (if (member? {:name x :value true} items)
                                                                [:rm x] [:add x]))}
                              x][:br]))
                     [:div] boxes)
             (reduce (fn [acc x]
                       (conj acc
                             [:li [:span (str x)]
                              [:button {:onClick #(put! actions [:rm x])} "x"]]))
                     [:ul] customs)
             [:input {:type "text" :ref "plugin"
                      :onKeyDown  (fn [e] (when (== 13 js/e.keyCode)
                                           (let [el (om/get-node owner "plugin")
                                                 v (.-value el)]
                                             (put! actions [:cust v])
                                             (set! (.-value el) ""))))}
              [:button {:onClick #(let [el (om/get-node owner "plugin")
                                        v (.-value el)]
                                    (put! actions [:cust v])
                                    (set! (.-value el) ""))}
               "Add"]]
             (om/build help-view app)]))))

(defmethod widget :cordova-logos [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:items (map pr-str (vec (:value app)))
       :actions (chan)})
    om/IWillMount
    (will-mount [_]
      (let [actions (om/get-state owner :actions)]
        (go-loop []
          (let [[tag edn] (<! actions)]
            (if (= tag :add)
              (do (om/set-state! owner :items (vec (conj (:value @app) edn)))
                  (om/transact! app :value (fn [items] (vec (conj items edn))) :update))
              (do (om/set-state! owner :items (vec (remove #(= % edn) (:value @app))))
                  (om/transact! app :value (fn [items] (vec (remove #(= % edn) items))) :delete)))
            (recur)))))
    om/IRenderState
    (render-state [_ {:keys [items actions]}]
      (html [:section
             [:h4 (:label app)]
             (reduce (fn [acc x]
                       (conj acc
                             [:li [:span (str x)]
                              [:button {:onClick #(put! actions [:rm x])} "x"]]))
                     [:ul] items)
             [:div
              [:p "location: " [:input {:type "text" :ref "logo1"}]]
              [:p "width: " [:input {:type "text" :ref "logo2"}]]
              [:p "height: " [:input {:type "text" :ref "logo3"}]]
              [:p "platform: " [:input {:type "text" :ref "logo4"}]]
              [:p "density: " [:input {:type "text" :ref "logo5"}]]
              [:button {:onClick #(let [el1 (om/get-node owner "logo1")
                                        el2 (om/get-node owner "logo2")
                                        el3 (om/get-node owner "logo3")
                                        el4 (om/get-node owner "logo4")
                                        el5 (om/get-node owner "logo5")
                                        val1 (.-value el1)
                                        val2 (.-value el2)
                                        val3 (.-value el3)
                                        val4 (.-value el4)
                                        val5 (.-value el5)]
                                    (put! actions [:add {:location val1 :width val2 :height val3
                                                         :platform val4 :density val5}])
                                    (set! (.-value el1) "") (set! (.-value el2) "")
                                    (set! (.-value el3) "") (set! (.-value el4) "")
                                    (set! (.-value el5) ""))}
               "Add"]]
             (om/build help-view app)]))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Main Views

(defn current-page-view [{:keys [general desktop mobile]} owner]
  (reify
    om/IInitState
    (init-state [_]
      {:showing :general})
    om/IRenderState
    (render-state [_ {:keys [showing]}]
      (let [current (case showing ;cursor of the currently rendered page
                      :general general
                      :desktop desktop
                      :mobile mobile)]
        (html [:div
               (reduce (fn [ul page]
                         (if (= page showing)
                           (conj ul [:li {:class "showing"} (drop 1 (str page))])
                           (conj ul [:li {:onClick #(om/set-state! owner :showing page)}
                                     (drop 1 (str page))])))
                       [:ul {:id "page-select"}] [:general :desktop :mobile])
               (om/build-all widget current ;render the selected page
                             {:key :id})])))))

(defn app-view [app owner]
  (reify
    om/IWillUpdate
    (will-update [_ next-props next-state]
      (when (:err-msg next-state)
        (js/alert (:err-msg next-state))
        (js/setTimeout #(om/set-state! owner :err-msg nil) 5000)))
    om/IRenderState
    (render-state [_ {:keys [err-msg]}]
      (dom/div nil
               (om/build sync/om-sync  app
                         {:opts {:view current-page-view
                                 :filter (comp #{:create :update :delete} tx-tag)
                                 :id-key :path-id
                                 :edn-hander-fn (fn [_ path _ tx-data]
                                                  (assoc {} :path (into [] (rest path))
                                                         :value (:new-value tx-data)))
                                 :on-success (fn [res tx-data] (println res))
                                 :on-error
                                 (fn [err tx-data]
                                   (reset! app-state (:old-state tx-data))
                                   (om/set-state! owner :err-msg
                                                  "Uh-oh, something went wrong!\n\nIf this problem persists, let me know by filing an issue here: https://github.com/greenyouse/browserific/issues"))}})
               (when err-msg
                 (html [:div err-msg]))))))


(let [tx-chan (chan)
      tx-pub-chan (async/pub tx-chan (fn [_] :txs))]
  (edn-xhr
   {:method :get
    :url "/init"
    :on-complete
    (fn [res]
      (reset! app-state res)
      (om/root app-view app-state
               {:target (.getElementById js/document "content")
                :shared {:tx-chan tx-pub-chan}
                :tx-listen
                (fn [tx-data root-cursor]
                  (put! tx-chan [tx-data root-cursor]))}))}))
