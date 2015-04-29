(ns browserific.config.pages.mobile
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))


(defn mobile-page
  "The page for mobile options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :name :data (reagent/cursor [:mobile :id] config-db) :label "App ID"
      :htxt "A unique identifier for your app."
      :hurl
  "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File"}
     {:type :checkbox-list :data (reagent/cursor [:mobile :platforms] config-db) :label "Mobile Platforms"
      :htxt "The mobile platforms to deploy your app on."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/guide_support_index.md.html#Platform%20Support"
      :boxes ["amazon-fireos" "android" "blackberry" "firefoxos" "ios" "ubuntu" "wp7" "wp8" "tizen"]}
     ;; FIXME: should have a default of index.html
     {:type :name :data (reagent/cursor [:mobile :content] config-db) :label "Content"
      :htxt "The optional <content> element defines the app's starting page in the top-level web assets directory. The default value is /index.html, which customarily appears in a project's top-level www directory."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File"}
     {:type :name :data (reagent/cursor [:mobile :icons :global :src] config-db) :label "Icons"
      :htxt "Enter the relative path to your icon. This is a global icon and can be used for all your platforms and is the default if none other is specified. Other icons can be created for each platform for more exact sizes."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"}
     {:type :strings :data (reagent/cursor [:mobile :permissions] config-db) :label "Permissions"
      :htxt "A whitelist of domains that can connect with your app."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/guide_appdev_whitelist_index.md.html#Whitelist%20Guide"}
     {:label "Global Preferences"
      :htxt "These global preferences apply to all platforms."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File"
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :fullscreen] config-db) :label "Fullscreen?"
        :help "Fullscreen allows you to hide the status bar at the top of the screen."
        :default false}
       {:type :select :data (reagent/cursor [:mobile :preferences :orientation] config-db) :label "Orientation"
        :help "Allows you to lock orientation and prevent the interface from rotating in response to changes in orientation. Default means both landscape and portrait."
        :options ["default" "landscape" "portrait"]}]}]))
