(ns browserific.cordova.plugins.compass
  "Handles Cordova's device-orientation (compass) plugin")

(defn get-heading
  "Get the current compass heading. The compass heading is returned via a
  CompassHeading object using the compassSuccess callback function.

  * success -- onSuccess callback gives a CompassHeading object with
               magneticHeading, trueHeading, headingAccuracy, and timestamp
               fields
  * err -- onError callback gives a CompassError object with a code field"
  [success err]
  (js/navigator.compass.getCurrentHeading success err))

(defn watch-heading
  "Gets the device's current heading at a regular interval. Each time the
  heading is retrieved, the headingSuccess callback function is executed.

  Returns a Watch ID which can be used with clear-heading to clear the watch.

  * success -- onSuccess callback gives a CompassHeading object with
               magneticHeading, trueHeading, headingAccuracy, and timestamp
               fields
  * err -- onError callback gives a CompassError object with a code field
  * opts -- a map with :filter and :frequency options (see docs)

  quirks:
  - amazon-fire: filter is not supported
  - anroid: filter is not supported
  - firefoxos: filter is not supported
  - ios: Only one watchHeading can be in effect. Watching heading changes with a filter is more efficient than with time intervals.
  - wp7: filter is not supported
  - wp8: filter is not supported
  - tizen: filter is not supported"
  [success err & opts]
  (let [o (js->clj (first opts))]
    (js/navigator.compass.watchHeading success err o)))

(defn clear-heading
  "Stop watching the compass referenced by watch.

  * watch -- The ID returned from watch-heading"
  [watch]
  (js/navigator.compass.clearWatch watch))
