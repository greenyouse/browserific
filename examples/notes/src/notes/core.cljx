(ns notes.core
  (:refer-clojure :exclude [atom])
  (:require [notes.db :as db]
            [notes.components :as c]
            [reagent.core :as reagent :refer [atom]]
            [cljs.core.async :as async :refer [put! <! chan]]
            cljsjs.react)
  (:require-macros [chenex.macros :as chenex]
                   [cljs.core.async.macros :refer [go-loop]]))

(defn matcher
  "partial matching for search input"
  [q t]
  (let [pmatch (subs t 0 (count q))]
    (= pmatch q)))

(defn search-view [q]
  (filter (fn [[_ title _]]
            (and (< 0 (count q))
                 (matcher q title)))
          (db/list-view)))

;; TODO: css transitions for page navigation + search bar + text inputs
(defn- select-page
  "The main note selection page"
  []
  (let [search-bar (atom false)
        query (atom "")]
    (fn []
      [:div
       [c/header-c {:action {:text "+New"
                             :fn #(c/to-entry (db/uuid) nil nil)
                             :class "icon icon-compose"}
                    :title "Notes"
                    :search {:fn #(if @search-bar
                                    (reset! search-bar false)
                                    (reset! search-bar true))
                             :class "icon icon-search"}}]
       (if @search-bar [c/search-c {:placeholder "Search Notes"
                                    :query #(reset! query (-> % .-target .-value))
                                    :close #(reset! search-bar false)}])
       ;;FIXME: search input not updating list-c
       [c/list-c {:link "#" :data (if @search-bar
                                    (search-view @query)
                                    (db/list-view))}]])))

(defn- entry-page
  "The page for writing new notes"
  [id title text]
  (let [title-atom (atom title)
        text-atom (atom text)
        save (fn [atm e]
               (reset! atm (-> e .-target .-value))
               (set! (-> e .-target .-value) ""))
        ;; starts the editing mode for new notes
        edit-title (if (nil? @title-atom) (atom true) (atom false))
        edit-text (if (nil? @text-atom) (atom true) (atom false))]
    (fn []
      [:div
       [c/header-c {:larrow {:fn #(c/to-select)
                             :class "icon icon-close"}
                    :back {:fn #(c/to-select) :text "Notes"}
                    :title "Add Note"
                    :action {:fn #(do (db/save-note id @title-atom @text-atom)
                                      (reset! edit-text false))
                             :text "Save"}}]

       ;; TODO: set focus to input or textarea onclick
       (if @edit-title
         [:form {:class "title-entry" :id "woot"}
          [:p
           [:input {:on-change #(reset! title-atom (-> % .-target .-value))
                    :on-key-down #(if (= 13 (.-which %))
                                    (do (save title-atom %)
                                        (reset! edit-title false)))
                    :placeholder "Title" :type "text"
                    :default-value @title-atom}]]]
         [:p {:class "title-entry"
              :on-click #(reset! edit-title true)} (str "Title: " @title-atom)])
       (if @edit-text
         [:form {:class "text-entry"}
          [:textarea {:on-change #(reset! text-atom (-> % .-target .-value))
                      :on-key-down #(reset! text-atom (-> % .-target .-value))
                      :default-value @text-atom}]]
         [:p {:class "text-entry"
              :on-click #(reset! edit-text true)} @text-atom])

       [c/footer-c (chenex/in-case!
                    [:firefoxos] {:item1
                                  ;;FIXME: center button icons
                                  [:button {:on-click #(js/alert "to the cloud!")
                                            :data-icon "send"}]

                                  :item2
                                  [:button
                                   {:on-click #(do (db/rm-note id @title-atom @text-atom)
                                                   (c/to-select))
                                    :data-icon "delete"}]

                                  ;; FIXME: get this to reload entry with new data
                                  :item3
                                  [:button
                                   {:on-click #(c/to-entry (db/uuid) nil nil)
                                    :data-icon "compose"}]}
                    :else [:footer
                           [:h4 {:on-click #(js/alert "to the cloud!")}]
                           [:button {:on-click #(do
                                                  (db/rm-note id @title-atom @text-atom)
                                                  (c/to-select))}]
                           ;; FIXME: get this to reload entry with new data
                           [:h4
                            {:on-click #(reset! c/page-atom {:page "entry" :id (db/uuid)
                                                             :title nil :text nil})}]])]])))

(defn notes-screen
  "The in-app navigation, this renders new pages based on page-atom"
  []
  (let [{:keys [page id title text]} @c/page-atom
        current (case page
                  "select" [select-page]
                  "entry" [entry-page id title text])]
    [:div
     current]))

(defn on-device-ready
  "Loads cordova after the deviceready event"
  []
  (reagent/render-component [notes-screen]
                            (.getElementById js/document "app")))

(with-meta
  (defn setup []
    (chenex/in-case!
     [:m] (.addEventListener js/document "deviceready" on-device-ready false)
     [:d] (reagent/render-component [notes-screen]
                                    (.getElementById js/document "app"))))
  {:export true})
