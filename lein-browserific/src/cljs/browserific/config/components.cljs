(ns browserific.config.components
  (:refer-clojure :exclude [atom])
  (:require [browserific.config.trans :as t]
            [cljs.reader :as reader]
            [reagent.core :as reagent :refer [atom]]))

;; FIXME: the more complex components have lots of repeated code, macros would
;; be great here to make the syntax more concise. This is kind of spaghetti
;; right now because I'm rushing through it :(
;; Tried doing macros over reagent but that didn't work very well, could try
;; again or do closures

(defn display
  "helper fn to hide/show an element using inline css"
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn jval
  "helper fn for getting the value from a callback (not compatible
  with set!)"
  [e]
  (-> e .-target .-value))

(defn member?
  "helper fn that checks if an item is in a collection"
  [coll item]
  (some #(= item %) coll))

;; FIXME: may need to ensure the cursor uses a vector for conj, not a seq
(defn checkbox-entry
  "helper fn for checkboxes that updates their state"
  [atm item]
  (if (member? @atm item)
    (swap! atm (fn [] (remove #(= item %) @atm)))
    (swap! atm conj item)))

(declare name-c strings-c icon-c checkbox-c checkbox-list-c select-c
  cordova-multi-c firefoxos-activity-c firefoxos-message-c firefoxos-preferences-c
  firefoxos-redirects-c locales-c)

(defn dispatch
  "helper fn to dispatch based on the type of an element"
  [type data boxes number default options plat items]
  (case type
    :name [name-c {:data data :number number}]
    :strings [strings-c {:data data}]
    :icon [icon-c {:data data}]
    :checkbox [checkbox-c {:data data :default default}]
    :checkbox-list [checkbox-list-c {:data data :boxes boxes}]
    :select [select-c {:data data :options options}]
    :cordova-multi [cordova-multi-c {:data data :plat plat}]
    :firefoxos-activity [firefoxos-activity-c {:data data}]
    :firefoxos-messages [firefoxos-message-c {:data data}]
    :firefoxos-preferences [firefoxos-preferences-c {:data data}]
    :firefoxos-redirects [firefoxos-redirects-c {:data data}]
    :locales [locales-c {:data data}]))

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
                     ["iOS"] ["Windows-Phone"] ["FirefoxOS"]])]
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

;; TODO: cleanup a little here
(defn name-c
  "Name entry component"
  [{:keys [data number platform]}]
  (let [g (gensym)
        save (fn [e]
               (t/add-item #(reset! % %2) data
                 (if number
                              (reader/read-string (jval e))
                              (jval e)))
               ;; NOTE: using jval with set! gives a compiler error
               (set! (-> e .-target .-value) ""))
        btn-save (fn [e]
                   (reset! data (if number
                                  (reader/read-string (-> e .-value))
                                  (-> e .-value)))
                   (set! (-> e .-value) ""))]
    (fn []
      [:div (if-not (empty? @data)
              [:div [:span @data]
               [:button {:on-click #(reset! data "")} "x"]])
       ;; HACK: Need refs, doing this for now
       [:div [:input {:type "text"
                      :id g
                      :on-key-down #(if (= 13 (.-which %))
                                      (save %))}]
        [:button {:on-click #(let [el (.getElementById js/document
                                        g)]
                               (btn-save el))}
         "Add"]]])))

;; FIXME: There should be an "Add" button for input too
(defn strings-c
  "List of strings component"
  [{:keys [data]}]
  (let [save (fn [e]
               (swap! data conj (jval e))
               (set! (-> e .-target .-value) ""))
        rm (fn [e]
             (swap! data #(remove (fn [x] (= e x)) %)))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                ^{:key item} [:div [:span item]
                              [:button {:on-click #(rm item)} "x"]]))
       [:input {:type "text"
                :on-key-down #(if (= 13 (.-which %))
                                (save %))}]])))

(defn icon-c
  "Component for browser + firefoxos icons.
  Similar to strings-c."
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     v1 (keyword (.-value el1))
                     v2 (.-value el2)]
                 (swap! data conj {v1 v2})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")))
        rm (fn [e]
             (swap! data #(remove (fn [x] (= e x)) %)))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                ^{:key item} [:div [:span (str "size: " (first (keys item))
                                            ", location: " (first (vals item)))]
                              [:button {:on-click #(rm item)} "x"]]))
       [:div "size " [:input {:type "text"
                              :id g1}]
        [:div "src " [:input {:type "text"
                              :id g2}]
         [:button {:on-click #(save)} "Add"]]]])))

;; FIXME: more err-msgs with the checkbox
(defn checkbox-c
  "Checkbox component"
  [{:keys [data default]}]
  (let [check #(reset! data (if (true? @data) false true))]
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
                   :on-change #(reset! data (-> (.getElementById js/document g1)
                                             .-value))}]
         options)])))

;; This is bad, but we're going fast :)
(defn cordova-multi-c
  "Cordova component that can handle multiple input for icons + splashscreens."
  [{:keys [type data plat]}]
  (let [g1 (gensym) g2 (gensym) g3 (gensym)
        save (fn []
               (let [el (.getElementById js/document g1)
                     v (.-value el)]
                 (swap! data conj {:src v})
                 (set! (.-value el) "")))
        save2 (fn []
                (let [el1 (.getElementById js/document g1)
                      el2 (.getElementById js/document g2)
                      v1 (.-value el1)
                      v2 (.-value el2)]
                  (swap! data conj
                    {:src v1 :density v2})
                  (set! (.-value el1) "")
                  (set! (.-value el2) "")
                  [ ]))
        save3 (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     el3 (.getElementById js/document g3)
                     v1 (.-value el1)
                     v2 (.-value el2)
                     v3 (.-value el3)]
                 (swap! data conj
                   {:src v1 :width v2 :height v3})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")
                 (set! (.-value el3) "")))
        rm (fn [e]
             (swap! data #(remove (fn [x] (= e x)) %)))
        plat-cond (fn [c1 c2 c3]
                    (cond (= plat "ios") c1
                      (member? ["amazon-fire" "android"] plat) c2
                      :else c3))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                ^{:key item} [:div [:span
                                    (plat-cond
                                      (str "location: " (:src item)
                                        ", width: " (:width item)
                                        ", height: " (:height item))
                                      (str "location: " (:src item)
                                        ", density: " (:density
                                                      item))
                                      (str "location: " (:src item)))]
                              [:button {:on-click #(rm item)} "x"]]))
       [:div "location" [:input {:type "text"
                                 :id g1}]
        (plat-cond
          [:div "width" [:input {:type "text"
                                 :id g2}]
           "height" [:input {:type "text"
                             :id g3}]
           [:button {:on-click #(save3)} "Add"]]
          [:div "density" [:input {:type "text"
                                   :id g2}]
           [:button {:on-click #(save2)} "Add"]]
          [:button {:on-click #(save)} "Add"])]])))

;; each handler can have: href, disposition (either window or inline),
;;  returnValue (boolean), filters (either basic string or number, array
;;  of basic strings or numbers, or a filter definition object).
;; name input for new activities
;; FIXME: filters are not complete yet, research these more in the future
;; NOTE: disposition + returnValue retain default values if none are given
(defn firefoxos-activity-c
  "Web Activity component for FirefoxOS activities"
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym) g3 (gensym) g4 (gensym) g5 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     el3 (.getElementById js/document g3)
                     el4 (.getElementById js/document g4)
                     el5 (.getElementById js/document g5)
                     v1 (keyword (.-value el1))
                     v2 (.-value el2)
                     v3 (.-value el3)
                     v4 (.-value el4)
                     v5 (.-checked el5)]
                 (if (nil? @data) ;prep the cusor if nil
                   (reset! data {}))
                 (swap! data assoc v1 {:href v2 :disposition v3
                                       :filters v4 :returnValue v5})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")
                 (set! (.-value el4) "")))
        rm (fn [e]
             (swap! data dissoc e))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                (let [k (first item)
                      m (second item)]
                  ^{:key (gensym)} [:div [:span
                                          (str "activity name: " k
                                            ", href: " (:href m)
                                            ", disposition: " (:disposition m)
                                            ", filters: " (:filters m)
                                            ", returnValue: " (:returnValue m))]
                                    [:button {:on-click #(rm k)} "x"]])))
       [:br]
       [:div " activity name " [:input {:type "text" :id g1}]
        [:br]
        " href " [:input {:type "text" :id g2}]
        [:br]
        " disposition " [:select {:id g3}
                         [:option "window"]
                         [:option "inline"]]
        [:br]
        " filters " [:input {:type "text" :id g4}]
        [:br]
        "returnValue" [:input {:type "checkbox" :id g5}]
        [:button {:on-click #(save)} "Add"]]])))

(defn firefoxos-message-c
  "A component for FirefoxOS messages"
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     v1 (keyword (.-value el1))
                     v2 (.-value el2)]
                 (swap! data conj {v1 v2})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")))
        rm (fn [e]
             (swap! data #(remove (fn [x] (= e x)) %)))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                ^{:key (gensym)} [:div [:span
                                        (str "type: " (first (keys item))
                                          ", response: " (first (vals item)))]
                                  [:button {:on-click #(rm item)} "x"]]))
       [:br]
       [:div "type " [:input {:type "text" :id g1}]
        [:br]
        "response " [:input {:type "text" :id g2}]
        [:button {:on-click #(save)} "Add"]]])))

(defn firefoxos-preferences-c
  "Component for FirefoxOS preferences"
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym) g3 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     el3 (.getElementById js/document g3)
                     v1 (.-value el1)
                     v2 (.-value el2)
                     v3 (.-value el3)]
                 (if (nil? @data) ;prep the cusor if nil
                   (reset! data {}))
                 (swap! data assoc v1
                   (if v3
                     {:description v2 :access v3}
                     {:description v2}))
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")))
        rm (fn [e]
             (swap! data dissoc e))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                (let [k (first item)
                      m (second item)]
                  ^{:key (gensym)} [:div [:span
                                          (str "name: " k
                                            ", description: " (:description m)
                                            ", access: " (:access m))]
                                    [:button {:on-click #(rm k)} "x"]])))
       [:br]
       [:div "name " [:input {:type "text" :id g1}]
        [:br]
        "description " [:input {:type "text" :id g2}]
        [:br]
        "access " [:input {:type "text" :id g3}]
        [:button {:on-click #(save)} "Add"]]])))

(defn firefoxos-redirects-c
  "A component for FirefoxOS redirects"
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     v1 (.-value el1)
                     v2 (.-value el2)]
                 (swap! data conj {:from v1 :to v2})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")))
        rm (fn [e]
             (swap! data #(remove (fn [x] (= e x)) %)))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                ^{:key (gensym)} [:div [:span
                                        (str "from: " (:from item)
                                          ", to: " (:to item))]
                                  [:button {:on-click #(rm item)} "x"]]))
       [:br]
       [:div "from " [:input {:type "text" :id g1}]
        [:br]
        "to " [:input {:type "text" :id g2}]
        [:button {:on-click #(save)} "Add"]]])))

(defn locales-c
  "Locale component"
  [{:keys [data]}]
  (let [g1 (gensym) g2 (gensym) g3 (gensym)
        save (fn []
               (let [el1 (.getElementById js/document g1)
                     el2 (.getElementById js/document g2)
                     el3 (.getElementById js/document g3)
                     v1 (keyword (.-value el1))
                     v2 (.-value el2)
                     v3 (.-value el3)]
                 (if (nil? @data) ;prep the cusor if nil
                   (reset! data {}))
                 (swap! data assoc v1 {:name v2
                                       :description v3})
                 (set! (.-value el1) "")
                 (set! (.-value el2) "")
                 (set! (.-value el3) "")))
        rm (fn [e]
             (swap! data dissoc e))]
    (fn []
      [:div (if-not (empty? @data)
              (for [item @data]
                (let [k (first item)
                      m (second item)]
                  ^{:key (gensym)} [:div [:span
                                          (str "language-code: " k
                                            ", name: " (:name m)
                                            ", description: " (:description m))]
                                    [:button {:on-click #(rm k)} "x"]])))
       [:br]
       [:div "language-code " [:input {:type "text" :id g1}]
        [:br]
        "name " [:input {:type "text" :id g2}]
        [:br]
        "description " [:input {:type "text" :id g3}]
        [:button {:on-click #(save)} "Add"]]])))

(defn cordova-pref-c
  "Cordova preference component"
  [{:keys [type data label help boxes number default options items]}]
  [:div label
   (dispatch type data boxes number default options nil items) ;no plat here
   [:button {:on-click #(js/alert help)} "help!"]])

(defn generic-c
  "A generic component that can be used for composing fields of the
  config menu"
  [{:keys [type data label htxt hurl boxes number default options plat items prefs]}]
  [:section
   [:h4 label]
   (if (empty? prefs)
     (dispatch type data boxes number default options plat items)
     (reduce (fn [div pref]
               (conj div ^{:key (gensym)}
                 (cordova-pref-c pref) [:br]))
       [:div] prefs))
   [help-c {:text htxt :url hurl}]])
