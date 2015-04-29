(ns browserific.cordova.plugins.geolocation
  "Handles Cordova's geolocation plugin")

(defn get-position
  "Retuns the device's current position.

  * success -- onSuccess callback gives a Positon object with coords and
               timestamp fields (see docs for more)
  * err -- onError callback gives a PositionError object with code and message
           fields
  * opts -- geolocation options (see docs)"
  [success & err opts]
  (let [e (first err)
        o (first opts)]
    (js/navigator.geolocation.getCurrentPosition success e o)))

(defn watch-position
  "Returns the device's current position when a change is detected.

  Returns a Watch ID which can be used with clear-position to clear the watch.

  * success -- onSuccess callback gives a Positon object with coords and
               timestamp fields (see docs for more)
  * err -- onError callback gives a PositionError object with code and message
           fields
  * opts -- geolocation options (see docs)"
  [success & err opts]
  (let [e (first err)
        o (first opts)]
    (js/navigator.geolocation.watchPosition success e o)))

(defn clear-position
  "Stop watching the position referenced by watch.

  * watch -- The ID returned from watch-accel"
  [watch]
  (js/navigator.geolocation.clearWatch watch))
