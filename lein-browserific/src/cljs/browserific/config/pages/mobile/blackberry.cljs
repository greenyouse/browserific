(ns browserific.config.pages.mobile.blackberry
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn blackberry-page
  "Config page for blackberry"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "Blackberry Preferences"
      :htxt "Preferences for Blackberry."
      :hurl ["https://cordova.apache.org/docs/en/4.0.0/guide_platforms_blackberry10_config.md.html#BlackBerry%2010%20Configuration" "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File"]
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :ChildBrowser] config-db) :label "Child Browser?"
        :help "Disables child browser windows. By default, apps launch a secondary browser window to display resources accessed via window.open() or by specifying a _blank anchor target. Specify disable to override this default behavior."
        :default true}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :PopupBlocker] config-db) :label "Popup Blocker?"
        :help "Enables the popup blocker, which prevents calls to window.open(). By default, popups display in a child browser window. Setting the preference to enable prevents it from displaying at all."
        :default true}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :WebSecurity] config-db) :label "Web Security?"
        :help "Set to disable to override web security settings, allowing access to remote content from unknown sources. This preference is intended as a development convenience only, so remove it before packaging the app for distribution. Use the <access> element to whitelist URIs saftely in production."
        :default true}
       {:type :name :data (reagent/cursor [:mobile :preferences :BackgroundColor] config-db) :label "Background Color"
        :help "Set the app's background color. Supports a four-byte hex value, with the first byte representing the alpha channel, and standard RGB values for the following three bytes. Applies to Android and Blackberry."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :HideKeyboardFromAccessoryBar] config-db) :label "Hide Keyboard?"
        :help "Set to true to hide the additional toolbar that appears above the keyboard, helping users navigate from one form input to another. Applies to Blackberry and iOS."}]}
     {:type :multi :data (reagent/cursor [:mobile :icons :blackberry] config-db) :label "Blackberry Icons"
      :htxt "Icons for Blackberry."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}])}
     {:type :multi :data (reagent/cursor [:mobile :splash :blackberry] config-db) :label "Blackberry Splashscreen"
      :htxt "Splashscreen for Blackberry."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}])}]))
