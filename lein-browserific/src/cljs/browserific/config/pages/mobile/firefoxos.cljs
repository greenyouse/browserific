(ns browserific.config.pages.mobile.firefoxos
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.trans :as t]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn firefoxos-page
  "A page for firefoxos configurations"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :multi :data (reagent/cursor [:mobile :firefoxos :icons] config-db) :label "FirefoxOS Icons"
      :htxt "Icons for FirefoxOS. Give the size in pixels and the relative path to the icon file."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#icons"
      :multi-c (multi-input-template :vec [{:type :name :label "src"}
                                           {:type :name :label "size"}])}
     {:type :name :data (reagent/cursor [:mobile :firefoxos :type] config-db) :label "Type"
      :htxt "The app's type, which defines its level of access to sensitive device WebAPIs. If you do not define type, it will default to web as the type."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#type"}
     ;; NOTE: Web Activity filters aren't 100% implemented, they only allow for strings,
     ;;  not used very much though (low priority)
     {:type :multi :data (reagent/cursor [:mobile :firefoxos :activities] config-db) :label "FirefoxOS Activities"
      :htxt "Activities allow apps to either create new functions that are callable by other apps (called \"activity handlers\") or consume functions of other apps. The form above allow you to register new activity handlers."
      :hurl ["https://developer.mozilla.org/en-US/docs/Web/API/Web_Activities#App_manifest_%28a.k.a._declaration_registration%29" "https://hacks.mozilla.org/2013/01/introducing-web-activities/"]
      :multi-c (multi-input-template :map [{:type :name :label "activity-name"}
                                           {:type :name :label "href"}
                                           {:type :select :options ["window" "inline"] :label "disposition"}
                                           {:type :name :label "filters"}
                                           {:type :checkbox :label "returnValue"}])}
     {:type :name :data (reagent/cursor [:mobile :firefoxos :appcache] config-db) :label "AppCache Path"
      :htxt "The absolute path to the application cache manifest. You don't need to set an AppCache for packaged apps."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#appcache_path"}
     {:type :checkbox :data (reagent/cursor [:mobile :firefoxos :chrome :navigation] config-db) :label "Chrome?"
      :htxt "Whether to use the default FirefoxOS chrome navigation in your app (it's almost always better to create your own)."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#chrome"}
     {:type :strings :data (reagent/cursor [:mobile :firefoxos :installs-allowed-from] config-db) :label "Installs Allowed From"
      :htxt "One (or more) URLs to the domains where your app can be installed from."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#installs_allowed_from"}
     {:type :multi :data (reagent/cursor [:mobile :firefoxos :locales] config-db) :label "Locales"
      :htxt "A map of one or more language-specific overrides for the name and description sections of your app manifest. This can change the name and description of your app for its listing in the Firefox Marketplace."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#locales"
      :multi-c (multi-input-template :map [{:type :name :label "language-code"}
                                           {:type :name :label "name"}
                                           {:type :name :label "description"}])}
     {:type :multi :data (reagent/cursor [:mobile :firefoxos :messages] config-db) :label "Messages"
      :htxt "The system messages you allow the app to capture, and the pages in your app that will display when those messages occur."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#messages"
      :multi-c (multi-input-template :vec [{:type :name :label "type"}
                                           {:type :name :label "response"}])}
     {:type :strings :data (reagent/cursor [:mobile :firefoxos :orientation] config-db) :label "Orientation"
      :htxt "The positioning at which the application will stay locked. Choose only one from the values: portrait-primary, portrait-secondary, portrait, landscape-primary, landscape-secondary, or landscape"
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#orientation"}
     {:type :name :data (reagent/cursor [:mobile :firefoxos :origin] config-db) :label "Origin"
      :htxt "Packaged apps have a special internal protocol of app://UUID where UUID is a string unique to each device the app is installed on. UUID is not easily accessible at this time. The origin field allows you to replace this UUID value with a single domain name that will be used by each installed app."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#origin"}
     {:type :multi :data (reagent/cursor [:mobile :firefoxos :preferences] config-db) :label "Preferences"
      :htxt "The user permissions for sensitive device APIs that your app needs."
      :hurl ["https://developer.mozilla.org/en-US/Apps/Build/Manifest#permissions" "https://developer.mozilla.org/en-US/Apps/Build/App_permissions"]
      :multi-c (multi-input-template :map [{:type :name :label "name"}
                                           {:type :name :label "description"}
                                           {:type :name :label "access"}])}
     {:type :strings :data (reagent/cursor [:mobile :firefoxos :precompile] config-db) :label "Precompile"
      :htxt "The path to JavaScript files containing asm.js code that you want compiled at install time."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#precompile"}
     {:type :multi :data (reagent/cursor [:mobile :firefoxos :redirects] config-db) :label "Redirects"
      :htxt "The internal URLs your app uses to handle external processes."
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#redirects"
      :multi-c (multi-input-template :vec [{:type :name :label "from"}
                                           {:type :name :label "to"}])}
     {:type :select :data (reagent/cursor [:mobile :firefoxos :role] config-db) :label "Role"
      :htxt "The role field is mainly for internal use by the Gaia engineering team; it allows you to specify how an app should be used by B2G, its role in the system. For example, is it a keyboard, or a homescreen replacement?"
      :hurl "https://developer.mozilla.org/en-US/Apps/Build/Manifest#role"
      :options ["system" "input" "homescreen" "search"]}]))
