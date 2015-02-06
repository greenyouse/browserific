(ns browserific.config.pages.mobile.amazon-fire
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn amazon-fire-page
  "Little page for amazon fire options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "Amazon-Fire Preferences"
      :htxt "These preferences only apply to amazon-fire."
      :hurl
      "https://cordova.apache.org/docs/en/4.0.0/guide_platforms_amazonfireos_config.md.html#Amazon%20Fire%20OS%20Configuration"
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :KeepRunning] config-db) :label "Keep Running?"
        :help "Determines whether the application stays running in the background even after a pause event fires. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :ErrorUrl] config-db) :label "Error URL"
        :help "If set, will display the referenced page upon an error in the application instead of a dialog with the title \"Application Error\". Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadingDialog] config-db) :label "Loading Dialog"
        :help "If set, displays a dialog with the specified title and message, and a spinner, when loading the first page of an application. The title and message are separated by a comma in this value string, and that comma is removed before the dialog is displayed. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadingPageDialog] config-db) :label "Loading Page Dialog"
        :help "The same as LoadingDialog, but for loading every page after the first page in the application. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :LoadUrlTimeout] config-db) :label "Loading Timeout"
        :help "When loading a page, the amount of time to wait before throwing a timeout error. The default is 20000 msec or 20 seconds. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :SplashScreen] config-db) :label "Splash Screen"
        :help "The name of the file minus its extension in the res/drawable directory. Various assets must share this common name in various subdirectories. Applies to Amazon-Fire and Android."}
       {:type :name :data (reagent/cursor [:mobile :preferences :SplashScreenDelay] config-db) :label "Splash Screen Delay"
        :help "The amount of time the splash screen image displays. Default is 5000 msec or 5 seconds. Applies to Amazon-Fire and Android."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ShowTitle] config-db) :label "Show Title?"
        :help "Whether to show the title at the top of the screen. Applies to Amazon-Fire and Android."}
       {:type :select :data (reagent/cursor [:mobile :preferences :LogLevel] config-db) :label "Log Level"
        :help "Sets the minimum log level through which log messages from your application will be filtered. Valid values are ERROR, WARN, INFO, DEBUG, and VERBOSE. Applies to Amazon-Fire and Android."
        :options ["ERROR" "WARN" "INFO" "DEBUG" "VERBOSE"]}]}
     {:type :multi :data (reagent/cursor [:mobile :icons :amazon-fire] config-db) :label "Amazon Fire Icons"
      :htxt "Icons for Amazon Fire."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}
                                           {:type :select :options ["ldpi" "mdpi" "hdpi" "xhdpi"] :label "density"}])}
     {:type :multi :data (reagent/cursor [:mobile :splash :amazon-fire] config-db) :label "Amazon-Fire Splashscreen"
      :htxt "Splashscreen for Amazon Fire."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :multi-c (multi-input-template :vec [{:type :name :label "location"}
                                           {:type :select :options ["ldpi" "mdpi" "hdpi" "xhdpi"] :label "density"}])}]))
