(ns browserific.cordova.plugins.dialogs
  "Handles Cordova's dialogs plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn alert
  "Shows a custom alert or dialog box. Most Cordova implementations use a
  native dialog box for this feature, but some platforms use the browser's
  alert function, which is typically less customizable.

  * msg -- Dialog message
  * success -- callback with the index of the button pressed
  * title -- Dialog title (defaults to 'alert')
  * btn-name -- Button's name (defaults to 'OK')

  quirks:
  - blackberry: callback parameter is passed the number 1.
  - firefoxos: Both native-blocking window.alert() and non-blocking
               navigator.notification.alert() are available.
  - wp7: There is no alert() but it can be bound to window.alert. Alert is also
         asynchronous.
  - wp8: There is no alert() but it can be bound to window.alert. Alert is also
         asynchronous."
  [msg success & title button-name]
  (let [t (first title)
        b (first button-name)]
    (js/navigator.notification.alert msg success t b)))

(defn confirm
  "Displays a customizable confirmation dialog box.

  * msg -- Dialog message
  * success -- callback with the index of the button pressed
  * title -- Dialog title (defaults to 'alert')
  * btn-labels -- Array of strings specifying button labels (Array)
                  (defaults to ['OK','Cancel'])

  quirks:
  - firefoxos: Both native-blocking window.confirm() and non-blocking
               navigator.notification.confirm() are available.
  - wp7: There is no confirm() but it can be bound to window.confirm. Confirm
         is also asynchronous.
  - wp8: There is no confirm() but it can be bound to window.confirm. Confirm
         is also asynchronous."
  [msg success & title btn-labels]
  (let [t (first title)
        b (first btn-labels)]
    (js/navigator.notification.confirm msg success t b)))

(defn prompt
  "Displays a native dialog box that is more customizable than the browser's
  prompt function.

  Doesn't work on Blackberry or Tizen.

  * msg -- Dialog message
  * success -- callback with the index of the button pressed
  * title -- Dialog title (defaults to 'alert')
  * btn-labels -- Array of strings specifying button labels (Array)
                  (defaults to ['OK','Cancel'])
  * text -- Default textbox input value (default to empty string)

  quirks
  - android: Android supports a maximum of three buttons, and ignores any more
             than that. On Android 3.0 and later, buttons are displayed in
             reverse order for devices that use the Holo theme.
  - firefoxos: Both native-blocking window.prompt() and non-blocking
               navigator.notification.prompt() are available."
  [msg success & title btn-labels text]
  (let [t (first title)
        b (first btn-labels)
        txt (first text)]
    (chenex/ex! [:blackberry :tizen]
      (js/navigator.notification.prompt msg success t b txt))))

(comment (throw (js/Error. "Browserific Error: dialogs/prompt does not work on blackberry or tizen")))

(defn beep
  "The device plays a beep sound.

  * times -- the number of times to repeat the beep.

  quirks:
  - amazon-fireos: Plays the default notification sound.
  - android: Plays the default notification ringtone.
  - tizen: Tizen implements beeps by playing an audio file via the media
           API. The beep file must be short, must be located in a sounds
           subdirectory of the application's root directory, and must be named
           beep.wav.
  - wp7: Plays a generic beep
  - wp8: Plays a generic beep"
  [times]
  (js/navigator.notification.beep times))
