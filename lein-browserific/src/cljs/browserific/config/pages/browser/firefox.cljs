(ns browserific.config.pages.browser.firefox
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn firefox-page
  "The Page for Firefox options."
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :strings :data (reagent/cursor [:extensions :extra :firefox :dependencies] config-db) :label "Dependencies"
      :htxt "String or array of strings representing package names that this add-on requires in order to function properly."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :lib] config-db) :label "Lib"
      :htxt "String representing the top-level module directory provided in this add-on. Defaults to \"lib\"."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :main] config-db) :label "Main"
      :htxt "String representing the name of a program module that is located in one of the top-level module directories specified by lib. Defaults to \"main\"."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :packages] config-db) :label "Packages"
      :htxt "String pointing to a directory containing additional packages. Defaults to \"packages\"."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :strings :data (reagent/cursor [:extensions :extra :firefox :permissions :cross-domain-content] config-db) :label "Cross-Domain Content"
      :htxt "Allows content scripts to do XHR and access iframe content."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Guides/Content_Scripts/Cross_Domain_Content_Scripts"}
     ;; TODO: write :preferences
     #_  {:type :name :data (reagent/cursor [:extensions :extra :firefox :preferences] config-db) :label "Preferences"
          :htxt "Gives extensions the ability to store designated preferences across application restarts."
          :url "https://developer.mozilla.org/en-US/Add-ons/SDK/High-Level_APIs/simple-prefs"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :preferences-branch] config-db) :label "Preferences Branch"
      :htxt "Use this to specify an alternative branch for your add-on's simple-prefs. "
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/High-Level_APIs/simple-prefs#Simple-prefs_in_the_preferences_system"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :tests] config-db) :label "Tests"
      :htxt "String representing the top-level module directory containing test suites for this package. Defaults to \"tests\"."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :name :data (reagent/cursor [:extensions :extra :firefox :title] config-db) :label "Package Title"
      :htxt "The title of the package. It can contain spaces. If this key is present its value will be used as the add-on's em:name element in its \"install.rdf\"."
      :url "https://developer.mozilla.org/en-US/Add-ons/SDK/Tools/package_json"}
     {:type :strings :data (reagent/cursor [:extensions :extra :firefox :translators] config-db) :label "Translators"
      :htxt "Names of translators."
      :url "https://developer.mozilla.org/en-US/Add-ons/Install_Manifests#translator"}]))
