(ns browserific.cordova.plugins.vibrate
  "Handles Cordova's vibrate plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn vibrate
  "Vibrates the device for a given amount of time.

  Doesn't work on Tizen

  * time -- Milliseconds to vibrate the device. For android, this may also be
            a vector of durations which will be executed sequentially. For all
            devices but iOS, a value of zero may be given in order to cancel the
            vibration.

  quirks:
  - blackberry: Max time is 5000ms (5s) and min time is 1ms.
  - ios: Ignores the specified time and vibrates for a pre-set amount of time."
  [time]
  (chenex/ex! [:tizen] (js/navigator.vibrate time)))

(comment (throw (js/Error. "Browserific Error: vibrate/vibrate does not work on tizen")))
