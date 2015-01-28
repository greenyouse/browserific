(ns browserific.config.pages.mobile.windows-phone
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn windows-phone-page
  "Windows Phone 8 Page"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "Windows Phone 8 Preferences"
      :htxt "Preferences for Windows Phone 8."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/guide_platforms_win8_index.md.html"
      :prefs
      [{:type :select :data (reagent/cursor [:mobile :preferences :wp8] config-db) :label "Windows Target Version"
        :help "Only for Window's Phone 8.1. Upgrades the build command to target Windows Phone 8.1 and Windows 8.1 (but you should use nw.js for that :p)."
        :options ["8.0" "8.1"]}]}
     {:type :cordova-multi :data (reagent/cursor [:mobile :icons :wp8] config-db) :label "Windows Phone 8 Icons"
      :htxt "Icons for Windows Phone 8."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "wp8"}
     {:type :cordova-multi :data (reagent/cursor [:mobile :splash :wp8] config-db) :label "Windows Phone 8 Splashscreen"
      :htxt "Splashscreen for Windows Phone 8."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "wp8"}]))
