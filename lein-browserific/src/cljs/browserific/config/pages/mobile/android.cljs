(ns browserific.config.pages.mobile.android
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn android-page
  "A page for android options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "Android Preferences"
      :htxt "These are preferences for Android"
      :hurl "https://cordova.apache.org/docs/en/4.0.0/guide_platforms_android_config.md.html#Android%20Configuration"
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :android :KeepRunning] config-db) :label "Keep Runing?"
        :help "Determines whether the application stays running in the background even after a pause event fires."}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :LoadUrlTimeoutValue] config-db) :label "Load URL Timeout"
        :help "When loading a page, the amount of time to wait before throwing a timeout error. Default is 2000 msec or 20 seconds."}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :SplashScreen] config-db) :label "Splash Screen"
        :help "The name of the file minus its extension in the res/drawable directory. Various assets must share this common name in various subdirectories."}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :SplashScreenDelay] config-db) :label "Splash Screen Delay"
        :help "The amount of time the splash screen image displays. Default is 3000 msec or 3 seconds."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :android :InAppBrowserStorageEnabled] config-db) :label "Browser Storage Enabled?"
        :help "Controls whether pages opened within an InAppBrowser can access the same localStorage and WebSQL storage as pages opened with the default browser. Default is true"
        :default true}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :LoadingDialog] config-db) :label "Loading Dialog"
        :help "If set, displays a dialog with the specified title and message, and a spinner, when loading the first page of an application. The title and message are separated by a comma in this value string, and that comma is removed before the dialog is displayed."}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :LoadingPageDialog] config-db) :label "Loading Page Dialog"
        :help "The same as LoadingDialog, but for loading every page after the first page in the application."}
       {:type :name :data (reagent/cursor [:mobile :preferences :android :ErrorUrl] config-db) :label "Error URL"
        :help "If set, will display the referenced page upon an error in the application instead of a dialog with the title \"Application Error\"."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :android :ShowTitle] config-db) :label "Show Title?"
        :help "Whether to show the title at the top of the screen."}
       {:type :select :data (reagent/cursor [:mobile :preferences :android :LogLevel] config-db) :label "Log Level"
        :help " Sets the minimum log level through which log messages from your application will be filtered. Valid values are ERROR, WARN, INFO, DEBUG, and VERBOSE."
        :options ["ERROR" "WARN" "INFO" "DEBUG" "VERBOSE"]}]}
     {:type :cordova-multi :data (reagent/cursor [:mobile :icons :android] config-db) :label "Android Icons"
      :htxt "Icons for Android."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "android"}
     {:type :cordova-multi :data (reagent/cursor [:mobile :splash :android] config-db) :label "Android Splashscreen"
      :htxt "Splashscreen for Android."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "android"}]))
