(ns browserific.cordova.core
  "Lifecycle events for cordova"
  (:require [greenyouse.chenex :as chenex])
  (:require-macros [browserific.cordova.core]))

(defn ready
  "The event fires when Cordova is fully loaded.

  Not needed for FirefoxOS

  * success -- onReady callback (loads your app)"
  [success]
  (chenex/ex! [:firefoxos]
    (js/document.addEventListener "deviceready" success false)))

;; TODO: double-check that pause is wrong for iOS
(defn pause
  "The pause event fires when the native platform puts the application into the
  background, typically when the user switches to a different application.

  Not needed for FirefoxOS nor Tizen

  * success -- onPause callback, a fn that pauses your app

  quirks:
  - ios: Cordova API calls and interactive JS calls will not work in the success
         callback. They'll only be processed when the app resumes, in the next
         run loop. If you don't want the app to continue running in the
         background when the phone gets locked, set UIApplicationExitsOnSuspend
         to YES in the iOS info.plist (default is to have multi-tasking enabled)."
  [success]
  (chenex/in-case!
    [:firefoxos :tizen]
    nil
    [:ios]
    (js/document.addEventListener "resign" success false)
    :else
    (js/document.addEventListener "pause" success false)))

(defn resume
  "The event fires when an application is retrieved from the background.

  Not needed for FirefoxOS nor Tizen

  * success -- onResume callback, a fn that resumes your app state

  quirks:
  - ios: Cordova API calls and interactive JS calls will not work in the success
         callback. They'll only be processed when the app resumes, in the next
         run loop. If you don't want the app to continue running in the
         background when the phone gets locked, set UIApplicationExitsOnSuspend
         to YES in the iOS info.plist (default is to have multi-tasking enabled)."
  [success]
  (chenex/in-case!
    [:firefoxos :tizen]
    nil
    [:ios]
    (js/document.addEventListener "active" success false)
    :else
    (js/document.addEventListener "resume" success false)))

(defn back
  "The event fires when the user presses the back button.

  Only works on Amazon-FireOS, Android, Blackberry, and WP8

  * success -- onBackButton callback, a fn to handle pressing the back button. It
               may be better to create an SPA app and handle this with
               ClojureScript"
  [success]
  (chenex/in!
    [:amazon-fireos :android :blackberry :wp8]
    (js/document.addEventListener "backbutton" success false)))

(defn menu
  "The event fires when the user presses the menu button.

  Only works on Amazon-FireOS, Android, and BlackBerry

  * success -- onMenuButton callback, a fn to handle menu button presses."
  [success]
  (chenex/in!
    [:amazon-fireos :android :blackberry]
    (js/document.addEventListener "menubutton" success false)))

(defn search
  "The event fires when the user presses the search button on Android.

  Only works for Android

  * success -- onSearch callback, a fn for handle when the search button is
               pressed."
  [success]
  (chenex/in!
    [:android]
    (js/document.addEventListener "searchbutton" success false)))

(defn start-call
  "The event fires when the user presses the start call button. Overrides the
  default start call behavior.

  Only works on BlackBerry

  * success -- onStartCall callback, fn for handling the start call button"
  [success]
  (chenex/in!
    [:blackberry]
    (js/document.addEventListener "startcallbutton" success false)))

(defn end-call
  "The event fires when the user presses the end call button. It overrides the
  default end call behavior.

  Only works on BlackBerry

  * success -- onStartCall callback, fn for handling the end call button"
  [success]
  (chenex/in!
    [:blackberry]
    (js/document.addEventListener "endcallbutton" success false)))

(defn vol-down
  "The event fires when the user presses the volume down button.

  Only works on BlackBerry

  * success -- onVolumeDown callback, a fn for when the volume down is pressed"
  [success]
  (chenex/in!
    [:blackberry]
    (js/document.addEventListener "volumedownbutton" success false)))

(defn vol-up
  "The event fires when the user presses the volume down button.

  Only works on BlackBerry

  * success -- onVolumeUp callback, a fn for when the volume up is pressed"
  [success]
  (chenex/in!
    [:blackberry]
    (js/document.addEventListener "volumeupbutton" success false)))
