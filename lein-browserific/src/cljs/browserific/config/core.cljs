(ns browserific.config.core
  (:refer-clojure :exclude [atom])
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.trans :as t]
            [browserific.config.pages.general :refer [general-page]]
            [browserific.config.pages.desktop :refer [desktop-page]]
            [browserific.config.pages.mobile :as m :refer [mobile-page]]
            [browserific.config.pages.browser :as b :refer [browser-page]]
            [browserific.config.pages.mobile.amazon-fireos :refer [amazon-fireos-page]]
            [browserific.config.pages.mobile.android :refer [android-page]]
            [browserific.config.pages.mobile.blackberry :refer [blackberry-page]]
            [browserific.config.pages.mobile.ios :refer [ios-page]]
            [browserific.config.pages.mobile.tizen :refer [tizen-page]]
            [browserific.config.pages.mobile.windows-phone :refer [windows-phone-page]]
            [browserific.config.pages.mobile.firefoxos :refer [firefoxos-page]]
            [browserific.config.pages.browser.chrome :refer [chrome-page]]
            [browserific.config.pages.browser.firefox :refer [firefox-page]]
            [browserific.config.pages.browser.safari :refer [safari-page]]
            [browserific.config.components :as co]
            [reagent.core :as reagent :refer [atom]]
            cljsjs.react))

;; NOTE: if you want to develop on this, use the server from `lein browserific config`

(enable-console-print!)

(defonce page
  (atom "General"))

(defn config-screen []
  (let [current (case @page
                   "General" [general-page]
                   "Mobile" [mobile-page]
                   "Mobile-General" [mobile-page]
                   "Amazon-FireOS" [amazon-fireos-page]
                   "Android" [android-page]
                   "Blackberry" [blackberry-page]
                   "iOS" [ios-page]
                   "Tizen" [tizen-page]
                   "Windows-Phone" [windows-phone-page]
                   "FirefoxOS" [firefoxos-page]
                   "Desktop" [desktop-page]
                   "Browsers" [browser-page]
                   "Browser-General" [browser-page]
                   "Chrome" [chrome-page]
                   "Firefox" [firefox-page]
                   "Safari" [safari-page])]
    [:div
     [co/nav-bar-c {:atom page}]
     current]))

(t/edn-xhr
  {:method "GET"
   :url "/init"
   :on-complete
   (fn [{:keys [coll]}]
     (reset! config-db coll)
     (reagent/render-component [config-screen]
       (.getElementById js/document "content")))})
