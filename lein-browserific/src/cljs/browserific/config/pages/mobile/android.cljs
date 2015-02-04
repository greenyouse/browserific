(ns browserific.config.pages.mobile.android
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn android-page
  "A page for android options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "Android Preferences"
      :htxt "These are preferences for Android"
      :hurl ["https://cordova.apache.org/docs/en/4.0.0/guide_platforms_android_config.md.html#Android%20Configuration" "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File"]
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :KeepRunning] config-db) :label "Keep Runing?"
        :help "Determines whether the application stays running in the background even after a pause event fires. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadUrlTimeoutValue] config-db) :label "Load URL Timeout"
        :help "When loading a page, the amount of time to wait before throwing a timeout error. Default is 2000 msec or 20 seconds. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :SplashScreen] config-db) :label "Splash Screen"
        :help "The name of the file minus its extension in the res/drawable directory. Various assets must share this common name in various subdirectories. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :SplashScreenDelay] config-db) :label "Splash Screen Delay"
        :help "The amount of time the splash screen image displays. Default is 3000 msec or 3 seconds. Applies to Amazon-Fire and Android."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :InAppBrowserStorageEnabled] config-db) :label "Browser Storage Enabled?"
        :help "Controls whether pages opened within an InAppBrowser can access the same localStorage and WebSQL storage as pages opened with the default browser. Default is true"
        :default true}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadingDialog] config-db) :label "Loading Dialog"
        :help "If set, displays a dialog with the specified title and message, and a spinner, when loading the first page of an application. The title and message are separated by a comma in this value string, and that comma is removed before the dialog is displayed. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadingPageDialog] config-db) :label "Loading Page Dialog"
        :help "The same as LoadingDialog, but for loading every page after the first page in the application. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :ErrorUrl] config-db) :label "Error URL"
        :help "If set, will display the referenced page upon an error in the application instead of a dialog with the title \"Application Error\". Applies to Amazon-Fire and Android."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ShowTitle] config-db) :label "Show Title?"
        :help "Whether to show the title at the top of the screen. Applies to Amazon-Fire and Android."}
       {:type :select :data (reagent/cursor [:mobile :preferences :LogLevel] config-db) :label "Log Level"
        :help " Sets the minimum log level through which log messages from your application will be filtered. Valid values are ERROR, WARN, INFO, DEBUG, and VERBOSE. Applies to Amazon-Fire and Android."
        :options ["ERROR" "WARN" "INFO" "DEBUG" "VERBOSE"]}
       {:type :select :data (reagent/cursor [:mobile :preferences :AndroidLaunchMode] config-db) :label "Launch Mode"
        :help "Sets the Activity android:launchMode attribute. This changes what happens when the app is launched from app icon or intent and is already running. The default is singleTop."
        :options ["singleTop" "standard" "singleTask" "singleInstance"]}
       {:type :name :data (reagent/cursor [:mobile :preferences :BackgroundColor] config-db) :label "Background Color"
        :help "Set the app's background color. Supports a four-byte hex value, with the first byte representing the alpha channel, and standard RGB values for the following three bytes. Applies to Android and Blackberry."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :DisallowOverscroll] config-db) :label "Disallow Overscroll?"
        :help "Set to true if you don't want the interface to display any feedback when users scroll past the beginning or end of content. Applies to Android and iOS."}]}
     {:type :multi :data (reagent/cursor [:mobile :icons :android] config-db) :label "Android Icons"
      :htxt "Icons for Android."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}
                                           {:type :select :options ["ldpi" "mdpi" "hdpi" "xhdpi"] :label "density"}])}
     {:type :multi :data (reagent/cursor [:mobile :splash :android] config-db) :label "Android Splashscreen"
      :htxt "Splashscreen for Android."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}
                                           {:type :select :options ["ldpi" "mdpi" "hdpi" "xhdpi"] :label "density"}])}]))
