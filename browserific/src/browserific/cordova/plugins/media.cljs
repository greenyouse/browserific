(ns browserific.cordova.plugins.media
  "Handles Cordova's media plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn get-position
  "Returns the current position within an audio file.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])
  * sucess -- onSuccess callback that gives the current position in seconds
  * err -- onError callback"
  [media success & err]
  (let [e (first err)]
    (chenex/ex! [:amazon-fireos :firefoxos]
      (.getCurrentPosition media success e))))

(comment (throw (js/Error. "Browserific Error: media/get-position does not work on amazon-fireos or firefoxos")))

(defn get-duration
  "Returns the duration of an audio file in seconds. If the duration is
  unknown, it returns a value of -1.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])"
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.getDuration media)))

(comment (throw (js/Error. "Browserific Error: media/get-duration does not work on amazon-fireos or firefoxos")))

(defn pause
  "Pause playback of an audio file.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status]) "
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.pause media)))

(comment (throw (js/Error. "Browserific Error: media/pause does not work on amazon-fireos or firefoxos")))

(defn play
  "Start or resume playing an audio file.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])

  quirks:
  - ios: There are some additional play options, however they're not included
         (see docs)"
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.play media)))

(comment (throw (js/Error. "Browserific Error: media/play does not work on amazon-fireos or firefoxos")))

(defn release
  "Releases the underlying operating system's audio resources. Applications
  should call the release function for any Media resource that is no longer
  needed (especially on Android).

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])"
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.release media)))

(comment (throw (js/Error. "Browserific Error: media/release does not work on amazon-fireos or firefoxos")))

(defn seek-to
  "Moves the position within the audio file.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])
  * pos -- the position to set the playback position within the audio, in
           milliseconds.

  quirks:
  - blackberry: doesn't work on BlackBerry OS5"
  [media pos]
  (chenex/ex! [:amazon-fireos :firefoxos] (.seekTo media pos)))

(comment (throw (js/Error. "Browserific Error: media/seek-to does not work on amazon-fireos or firefoxos")))

(defn set-vol
  "Set the volume for audio playback.

  Only works on Android and iOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])
  * vol -- the volume level, must be within the range of 0.0 to 1.0"
  [media vol]
  (chenex/in! [:android :ios] (.pause media vol)))

(comment (throw (js/Error. "Browserific Error: media/set-vol only works on android and ios")))

(defn start-rec
  "Start recording an audio file.

  Only works on Android, iOS, WP7, and WP8

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])

  quirks:
  - android: Android devices record audio in Adaptive Multi-Rate format. The
             specified file should end with a .amr extension. After release the
             media object, the volume will return to its default level.
  - ios: iOS only records to files of type .wav and returns an error if the file
         name extension is not correct. If a full path is not provided, the
         recording is placed in the application's documents/tmp directory. This
         can be accessed via the File API using LocalFileSystem.TEMPORARY. Any
         subdirectory specified at record time must already exist.
  - wp8: If a full path is not provided, the recording is placed in the
         AppData/temp directory. This can be accessed via the File API using
         LocalFileSystem.TEMPORARY or 'ms-appdata:///temp/' URI. Any subdirectory
         specified at record time must already exist."
  [media]
  (chenex/in! [:android :ios :wp7 :wp8] (.startRecord media)))

(comment (throw (js/Error. "Browserific Error: media/start-rec only works on android, ios, wp7, and wp8")))

(defn stop-rec
  "Stop recording an audio file.

  Only works on Android, iOS, WP7, and WP8

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])"
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.stopRecord media)))

(comment (throw (js/Error. "Browserific Error: media/stop-rec only works on android, ios, wp7, and wp8")))

(defn stop
  "Stop playing an audio file.

  Doesn't work on Amazon-FireOS or FirefoxOS

  * media -- a media object, (js/Media. src mediaSuccess [err] [status])"
  [media]
  (chenex/ex! [:amazon-fireos :firefoxos] (.stop media)))

(comment (throw (js/Error. "Browserific Error: media/stop does not work on amazon-fireos or firefoxos")))
