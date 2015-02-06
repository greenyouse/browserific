(ns browserific.config.components
  (:refer-clojure :exclude [atom])
  (:require [browserific.config.trans :as t]
            [cljs.reader :as reader]
            [reagent.core :as reagent :refer [atom]]))

(defn- display
  "helper fn to hide/show an element using inline css"
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn- member?
  "helper fn that checks if an item is in a collection"
  [coll item]
  (some #(= item %) coll))

(defn- checkbox-entry
  "helper fn for checkboxes that updates their state"
  [atm item]
  (if (member? @atm item)
    (t/add-item (fn [d i]
                  (swap! d (fn [] (remove #(= i %) @d))))
      atm item)
    (t/add-item #(swap! % conj %2) atm item)))

(declare name-c strings-c checkbox-c checkbox-list-c select-c)

(defn- dispatch
  "helper fn to dispatch based on the type of an element"
  [type data boxes number default options multi-c]
  (case type
    :name [name-c {:data data :number number}]
    :strings [strings-c {:data data}]
    :checkbox [checkbox-c {:data data :default default}]
    :checkbox-list [checkbox-list-c {:data data :boxes boxes}]
    :select [select-c {:data data :options options}]
    :multi [multi-c {:data data}]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Components

(defn nav-bar-c
  "Nav-bar component"
  [{:keys [atom]}]
  [:div.nav
   (letfn [(menu [root pgs]
             (reduce (fn [ul [pg subpages]]
                       (conj ul ^{:key (gensym)}
                         [:li
                          [:a {:href "#" :on-click #(reset! atom pg)} pg]
                          subpages]))
               root pgs))]
     (menu [:ul]
       [["General"] ["Desktop"]
        ["Mobile" (menu [:ul]
                    [["Mobile-General"] ["Amazon-Fire"]
                     ["Android"] ["Blackberry"]
                     ["iOS"] ["Tizen"] ["Windows-Phone"] ["FirefoxOS"]])]
        ["Browsers" (menu [:ul]
                      ;; Opera doesn't have anything special so no page
                      [["Browser-General"] ["Chrome"]
                       ["Firefox"] ["Safari"]])]]))])

;; TODO: beautify this with images :D
;; FIXME: track down the `false` err-msg from react.js
(defn help-c
  "Help component"
  [{:keys [text url]}]
  (let [helper (atom false)]
    (fn []
      [:div
       [:div [:button {:on-click #(reset! helper true)} "?"]
        [:button.close-btn {:style (display @helper)
                            :on-click #(reset! helper false)}
         "xclosex"]]
       [:div.help {:style (display @helper)}
        [:p text]
        [:br]
        (if-not (nil? url)
          (if (string? url)
            [:p (str "For more information go here: \n")
             [:a {:href url :target "_blank"} url]]
            ;; NOTE: listing multiple help urls for extra confusing
            ;; stuff (e.g. Mozilla Web Activities :p).
            (reduce (fn [p link]
                      (conj p ^{:key link}
                        [:li [:a {:href link :target "_blank"} link] [:br]]))
              [:ul (str "For more information see one of these resources: \n")]
              url)))]])))

;; HACK: Need refs, doing this for now
(defn name-c
  "Name entry component"
  [{:keys [data number]}]
  (let [g (gensym)
        save (fn []
               (let [el (.getElementById js/document g)
                     v (.-value el)]
                 (t/add-item #(reset! % %2) data
                   (if number
                     (reader/read-string v)
                     v))
                 (set! (.-value el) "")))
        rm (fn []
             (t/rm-item #(reset! % %2) data ""))]
    (fn []
      [:div (if-not (empty? (str @data))
              [:div [:span @data]
               [:button {:on-click #(rm)}
                "x"]])
       [:div [:input {:type "text" :id g
                      :on-key-down #(if (= 13 (.-which %))
                                      (save))}]
        [:button {:on-click #(save)}
         "Add"]]])))

(defn strings-c
  "List of strings component"
  [{:keys [data]}]
  (let [g (gensym)
        save (fn []
               (let [el (.getElementById js/document g)
                     v (.-value el)]
                 (t/add-item #(swap! % conj %2) data v)
                 (set! (.-value el) "")))
        rm (fn [item]
             (t/rm-item (fn [d]
                          (swap! d #(remove (fn [x] (= item x)) %))) data))]
    (fn []
      [:div (if-not (empty? @data) ;watch for numbers, not ISeqable
              (for [item @data]
                ^{:key item} [:div [:span item]
                              [:button {:on-click #(rm item)} "x"]]))
       [:input {:type "text" :id g
                :on-key-down #(if (= 13 (.-which %))
                                (save))}]
       [:button {:on-click #(save)}
        "Add"]])))

;; FIXME: more err-msgs with the checkbox
(defn checkbox-c
  "Checkbox component"
  [{:keys [data default]}]
  (let [check (fn []
                (t/add-item #(reset! % %2)
                  data (if (true? @data) false true)))]
    (fn []
      [:input {:type "checkbox"
               :checked (cond
                          (true? @data) "checked"
                          (false? @data) false
                          (true? default) "checked")
               :on-click check}])))

(defn checkbox-list-c
  "A component that has a list of checkboxes"
  [{:keys [data boxes]}]
  (fn []
    (reduce (fn [div box]
              (conj div ^{:key box}
                [:input {:type "checkbox"
                         :checked (if (member? @data box)
                                    "checked" false)
                         :on-click #(checkbox-entry data box)}
                 box] [:br]))
      [:div] boxes)))

;; FIXME: should display the stored config.edn value when loaded
(defn select-c
  "A select component"
  [{:keys [data options]}]
  (let [g1 (gensym)]
    (fn []
      [:div [:br]
       (reduce (fn [div opt]
                 (conj div ^{:key (gensym)}
                   [:option opt]))
         [:select {:id g1
                   :on-click (fn []
                               (t/add-item #(reset! % %2)
                                 data (-> (.getElementById js/document g1)
                                        .-value)))}]
         options)
       [:button {:on-click (fn []
                             (t/rm-item #(reset! % %2) data ""))}
        "clear-data"]])))

(defn cordova-pref-c
  "Cordova preference component"
  [{:keys [type data label help boxes number default options]}]
  [:div label
   (dispatch type data boxes number default options nil)
   [:button {:on-click #(js/alert help)} "help!"]])

(defn generic-c
  "A generic component that can be used for composing fields of the
  config menu"
  [{:keys [type data label htxt hurl boxes number default options
           prefs multi-c]}]
  [:section
   [:h4 label]
   (if (empty? prefs)
     (dispatch type data boxes number default options multi-c)
     (reduce (fn [div pref]
               (conj div ^{:key (gensym)}
                 (cordova-pref-c pref) [:br]))
       [:div] prefs))
   [help-c {:text htxt :url hurl}]])
