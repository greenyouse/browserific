(ns notes.components
  (:refer-clojure :exclude [atom])
  (:require [reagent.core :as reagent :refer [atom]])
  (:require-macros [chenex.macros :as chenex]))

;; TODO: this should eventually show off abstract, ShadowDOM  components, for
;; now I cheated and just wrote (pretty horrible looking) app-specific
;; components

;; Make mobile first, then do specialization, then do desktops
;; TODO: get a touch optimization thingy

(defonce page-atom
  (atom {:page "select"}))

(defn to-select
  "Switches to the select page"
  []
  (reset! page-atom {:page "select"}))

(defn to-entry
  "Selects the entry page and provides the necessary data"
  [id title text]
  (reset! page-atom {:page "entry" :id id :title title :text text}))

;; TODO: checkout that cool polymer search bar
;;; mobile
;; [<]? [Notes]? [title]? ([Save] | [+NEW])
;;  [search-input]?
;;
;;; desktop
;; [search-input]? [title]? ([Delete] [Save] | [+NEW])
;;
;; generally
;; larrow back title action rarrow search opts id class style
(defn header-c
  "The page header"
  [{:keys [larrow back title action search]}]
  (chenex/in-case!
   [:firefoxos] [:section {:role "region"}
                 [:header
                  (if larrow [:button {:on-click (:fn larrow)}
                              [:span {:class (:class larrow)} "back"]])
                  [:menu
                   (if search [:button {:on-click (:fn search)
                                        :data-icon "search"}
                               [:span {:class (:class search)}]])
                   (if action [:button {:on-click (:fn action)}
                               [:span {:class (:class action)}
                                (:text action)]])]
                  (if title [:h1 title])]]
   :else [:header
          (if larrow [:span {:on-click (:fn larrow)} "<"])
          (if back [:span {:on-click (:fn back)} (:text back)])
          (if title [:h1 title])
          (if action [:button {:on-click (:fn action)} (:text action)])
          (if search [:input {:type "text" :value "search"}])]))

(defn search-c
  "A basic search bar"
  [{:keys [placeholder reset close query]}]
  (chenex/in-case!
   [:firefoxos] [:form {:role "search"}
                 [:button {:type "submit"
                           :on-click close} "close"]
                 [:p
                  [:input {:type "search"
                           :on-change query
                           :placeholder placeholder}]
                  ;; FIXME: reset should clear the input text
                  [:button {:type "reset"
                            :on-click #(js/console.log "clear broken")} "Clear"]]]
   :else [:form {:role "search"}
          [:button {:type "submit"} "close"]
          [:p
           [:input {:type "search"
                    :placeholder placeholder}]
           [:button {:type "reset"} "Clear"]]]))

;; ([title] - [item text] \n)*
;;
;; generally
;; link data id class style
(defn list-c
  "A list of the items"
  [{:keys [link data]}]
  (letfn [(text-title [txt]
            (str (subs txt 0 10) "... "))]
    (fn []
      (chenex/in-case!
       [:firefoxos] [:section {:data-type "list"}
                     (reduce (fn [ul [id title text]]
                               (conj ul ^{:key (gensym)}
                                     [:li [:a {:href link
                                               :on-click #(to-entry id title text)}
                                           [:aside {:class "pack-end"
                                                    :data-icon "forward"}]
                                           (if (or (= 0 (count title))
                                                   (nil? title))
                                             [:p (text-title text)]
                                             [:p [:strong title]])]]))
                             [:ul] data)]
       :else [:section {:id "list"}
              (reduce (fn [ul [id title text]]
                        (conj ul ^{:key (gensym)}
                              [:li [:a {:href link
                                        :on-click #(to-entry id title text)}
                                    (if-not (nil? title)
                                      [:p [:strong title]]
                                      [:p (text-title text)])]
                               ;; do a timestamp here?
                               ]))
                      [:ul] data)]))))

;;; mobile
;; [cloud upload] [trash] [new note]
;;
;; generally
;; tab item1 item2 item3 item4 item5 id class style
(defn footer-c
  "A special footer for input screens"
  [{:keys [item1 item2 item3]}]
  (chenex/in-case!
   [:firefoxos] [:footer {:role "toolbar"}
                 item1 item2 item3]
   :else [:footer item1 item2 item3]))
