(ns browserific.cordova.plugins.camera
  "Handles Cordova's camera plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn get-picture
  "Opens the default camera app to take a picture. Closes that app once
   the picture is done, and returns to host app. The cameraError gets
   called when a user cancels the picture taking too.

   See official documentation for the options fields of opts.
   http://plugins.cordova.io/#/package/org.apache.cordova.camera

  * success -- onSuccess callback with either a base64 string of the photo
               taken or a string of the image file's location
  * err -- onError callback with a message describing why the camera failed
  * opts -- a map of camera options (see docs)

   quirks:
   - AmazonFire: Amazon Fire OS uses intents to launch the camera activity on
                 the device to capture images, and on phones with low memory,
                 the Cordova activity may be killed. In this scenario, the
                 image may not appear when the cordova activity is restored.
   - Android: Same problem as AmazonFire
   - FirefoxOS: Uses WebActivies.
   - iOS: Has a special CameraUseGeolocation option. JS alerts in the callbacks
          cause problems, wrap them in setTimeouts instead. Also has a special
          popoverOptions option.
   - Tizen: Tizen only supports a destinationType of
            Camera.DestinationType.FILE_URI and a sourceType of
            Camera.PictureSourceType.PHOTOLIBRARY.
   - WP7: Invoking the native camera application while the device is connected
          via Zune does not work, and triggers an error callback."
  [success err & opts]
  (let [o (clj->js (first opts))]
    (js/navigator.camera.getPicture success err o)))

(defn cleanup
  "Only applies to iOS.

   Removes intermediate image files that are kept in temporary storage after
   calling camera.getPicture. Applies only when the value of Camera.sourceType
   equals Camera.PictureSourceType.CAMERA and the Camera.destinationType equals
   Camera.DestinationType.FILE_URI.

  * success -- onSuccess callback
  * err -- onError callback"
  [success err]
  (chenex/in! [:ios] (js/navigator.camera.cleanup success err)))

(comment (throw (js/Error. "Browserific Error: camera/cleanup only supported on iOS")))
