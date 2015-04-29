(ns browserific.cordova.plugins.transfer
  "Handles Cordova's file-transfer plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn upload
  "For uploading a file.

  Doesn't work on tizen.

  * ft -- a FileTransfer object, (js/FileTransfer.)
  * furl -- a filesystem URL.
  * server -- the url of the server to recieve the file
  * success -- onSuccess callback gives a FileUploadResult object with bytesSent,
               responseCode, response, and headers fields
  * err -- onError callback gives a FileTransferError object with code, source,
           target, http_status, body, and exception fields
  * opts -- a map of options (see docs)

  quirks:
  - android: Supports upload headers and progress events.
  - ios: Does not support responseCode or bytesSent. Supports upload headers and
         progress events."
  [ft furl server success err & opts]
  (let [s (js/encodeURI server)
        o (first opts)]
    (chenex/ex! [:tizen] (.upload ft furl s success err o))))

(comment (throw (js/Error. "Browserific Error: transfer/upload does not work on tizen")))

(defn download
  "Downloads a file.

  Doesn't work on tizen.

  * ft is a FileTransfer object, (js/FileTransfer.)
  * server is the URL of the server to download the file from
  * furl is the file destination, as a filesystem URL
  * success -- onSuccess callback gives a FileEntry object with a toURL field
  * err -- onError callback
  * opts -- a map of options (see docs)"
  [ft server furl success err & opts]
  (let [s (js/encodeURI server)
        o (first opts)]
    (chenex/ex! [:tizen] (.download ft s success err false o))))

(comment (throw (js/Error. "Browserific Error: transfer/upload does not work on tizen")))

(defn abort
  "Aborts an in-progress transfer.

  Doesn't work on tizen.

  * ft is a FileTransfer object, (js/FileTransfer.)"
  [ft]
  (chenex/ex! [:tizen] (.abort ft)))

(comment (throw (js/Error. "Browserific Error: transfer/upload does not work on tizen")))
