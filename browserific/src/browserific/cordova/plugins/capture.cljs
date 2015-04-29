(ns browserific.cordova.plugins.capture
  "Handles Cordova's media-capture plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn capture-audio
  "Start the audio recorder application and return information about captured
  audio clip files.

  Doesn't work on FirefoxOS or Tizen

  * success -- onSuccess callback, gives a mediaFiles object (see docs)
  * err -- onError callback, gives an Error object with code and message fields
  * opts -- a map of options (see docs)

  quirks:
  - wp7: Windows Phone 7 does not have a default audio recording application, so
         a simple user interface is provided.
  - wp7: Windows Phone 8 does not have a default audio recording application, so
         a simple user interface is provided."
  [success err & opts]
  (let [o (first opts)]
    (chenex/ex! [:firefoxos :tizen]
      (js/navigator.device.capture.captureAudio success err o))))

(comment (throw (js/Error. "Browserific Error: media/capture-audio does not work on firefoxos or tizen")))

(defn capture-image
  "Start the camera application and return information about captured image
  files.

  Doesn't work on FirefoxOS or Tizen

  * success -- onSuccess callback, gives a mediaFiles object (see docs)
  * err -- onError callback, gives an Error object with code and message fields
  * opts -- a map of options (see docs)

  quirks:
  - wp7: Invoking the native camera application while your device is connected
         via Zune does not work, and the error callback executes."
  [success err & opts]
  (let [o (first opts)]
    (chenex/ex! [:firefoxos :tizen]
      (js/navigator.device.capture.captureImage success err o))))

(comment (throw (js/Error. "Browserific Error: media/capture-image does not work on firefoxos or tizen")))

(defn capture-video
  "Start the video recorder application and return information about captured
  video clip files.

  Doesn't work on FirefoxOS or Tizen

  * success -- onSuccess callback, gives a mediaFiles object (see docs)
  * err -- onError callback, gives an Error object with code and message fields
  * opts -- a map of options (see docs)

  quirks:
  - blackberry: Cordova for BlackBerry 10 attempts to launch the Video Recorder
                application, provided by RIM, to capture video recordings. The
                app receives a CaptureError.CAPTURE_NOT_SUPPORTED error code if
                the application is not installed on the device."
  [success err & opts]
  (let [o (first opts)]
    (chenex/ex! [:firefoxos :tizen]
      (js/navigator.device.capture.captureVideo success err o))))

(comment (throw (js/Error. "Browserific Error: media/take-video does not work on firefoxos or tizen")))
