(ns browserific.cordova.plugins.device
  "Handles Cordova's device plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn cordova
  "Get the version of Cordova running on the device."
  []
  (js/device.cordova))

(defn model
  "Returns the name of the device's model or product.

  Doesn't work on Amazon-FireOS or FirefoxOS

  quirks:
  - anroid: gets the product name instead of the model name.
  - tizen: returns the vendor (e.g. TIZEN)
  - wp7: gives the manufacterer"
  []
  (chenex/ex! [:amazon-fireos :firefoxos] (js/device.model)))

(comment (throw (js/Error. "Browserific Error: device/model not supported on Amazon-FireOS or FirefoxOS")))

(defn platform
  "Get the device's operating system name.

  Doesn't work on Amazon-FireOS or FirefoxOS

  quirks:
  - wp7: Windows Phone 7 devices report the platform as WinCE.
  - wp8: Windows Phone 8 devices report the platform as Win32NT."
  []
  (chenex/ex! [:amazon-fireos :firefoxos] (js/device.platform)))

(comment (throw (js/Error. "Browserific Error: device/platform not supported on Amazon-FireOS or FirefoxOS")))

(defn uuid
  "The details of how a UUID is generated are determined by the device
  manufacturer and are specific to the device's platform or model.

  Doesn't work on Amazon-FireOS or FirefoxOS

  quirks:
  - ios: Specific only to the current installation of an app.
  - wp7: Generates a guid for the application.
  - wp8: Generates a guid for the application."
  []
  (chenex/ex! [:amazon-fireos :firefoxos] (js/device.uuid)))

(comment (throw (js/Error. "Browserific Error: device/uuid not supported on Amazon-FireOS or FirefoxOS")))

(defn version
  "Get the operating system version.

  Doesn't work in Amazon-FireOS or FirefoxOS"
  []
  (chenex/ex! [:amazon-fireos :firefoxos] (js/device.version)))

(comment (throw (js/Error. "Browserific Error: device/version not supported on Amazon-FireOS or FirefoxOS")))
