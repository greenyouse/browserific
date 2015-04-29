(ns browserific.cordova.plugins.accelerometer
  "Handles Cordova's device-motion (accelerometer) plugin")

(defn get-accel
  "Get the current acceleration along the x, y, and z axes. Success gives an
  acceleration object with x, y, z, and timestamp fields.

  * success -- onSuccess callback gives a js-obj with x,y,z, and timestamp fields
  * err -- onError callback

  quirks:
  - ios: Can't get current acceleration, use watch-accel instead. Returns
         the last value of watch-accel."
  [success err]
  (js/navigator.accelerometer.getCurrentAcceleration success err))

#_  (chenex/in-case!
      [:ios] (js/Error. "Use watch-accel instead")
      :else
      (js/navigator.accelerometer.getCurrentAcceleration success err))

(defn watch-accel
  "Retrieves the device's current Acceleration at a regular interval, executing
  the accelerometerSuccess callback function each time. Specify the interval in
  milliseconds via the acceleratorOptions object's frequency parameter. Success
  gives an acceleration object with x, y, z, and timestamp fields.

  Returns a Watch ID which can be used with clear-accel to clear the watch.

  * success -- onSuccess callback gives a js-obj with x,y,z, and timestamp fields
  * err -- onError callback
  * period -- the interval with which to check in milliseconds (default is
              1000ms)

  quirks:
  - ios: The period must be in the range of 40ms to 1000ms. Rounds to nearest
  value otherwise."
  [success err & period]
  (let [opts (clj->js {:frequency period})]
    (js/navigator.accelerometer.watchAcceleration success err opts)))

(defn clear-accel
  "Stop watching the Acceleration referenced by watch.

  * watch -- The ID returned from watch-accel"
  [watch]
  (js/navigator.accelerometer.clearWatch watch))
