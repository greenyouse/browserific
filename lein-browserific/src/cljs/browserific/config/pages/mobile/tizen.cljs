(ns browserific.config.pages.mobile.tizen
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn tizen-page
  "Tiny page for Tizen options."
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :multi :data (reagent/cursor [:mobile :icons :tizen] config-db) :label "Tizen Icons"
      :htxt "Icons for Tizen."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}
                                           {:type :name :label "height"}
                                           {:type :name :label "width"}])}]))
