(ns browserific.cordova.plugins.contact
  "Handles Cordova's contact plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn create
  "Creates a new contact synchonously and returns a new contact object. To save
  the contact permanently, use the save fn.

  Doesn't work Amazon-FireOS on Tizen.

  * contact -- a contact object as a clojure map"
  [contact]
  (let [c (clj->js contact)]
    (chenex/ex! [:amazon-fireos :tizen] (js/navigator.contacts.create c))))

(comment (throw (js/Error. "Browserific Error: contact/create does not work on amazon-fireos or tizen")))

(defn finder
  "Searches asynchronously for a contact. The field is used to search for the
   contact (it's a string), * searches all contacts. Can also filter results and
   get multiple results through the options.

   Doesn't work on Amazon-FireOS or Tizen

  * field -- a vector of contact object fields
  * success -- onSuccess callback gives an arry of Contact objects
  * err -- onError callback
  * opts -- an instance of js/ContactFindOptions()"
  [field success & err opts]
  (let [o-map (first opts)]
    (chenex/ex! [:amazon-fireos :tizen]
      (js/navigator.contacts.find field success err o-map))))

(comment (throw (js/Error. "Browserific Error: contact/find does not work on amazon-fireos or tizen")))

(defn pick
  "Launches a contact picker to select the contact. The result is passed to the
  callback.

  Doesn't work on Amazon-FireOS, BlackBerry, FirefoxOS, WP 7, or Tizen

  * success -- onSuccess callback gives the Contact object
  * err -- onError callback"
  [success & err]
  (let [e (first err)]
    (chenex/ex! [:amazon-fireos :blackberry :firefoxos :wp7 :tizen]
      (js/navigator.contacts.pickContact success e))))

(comment (throw (js/Error. "Browserific Error: contact/pick does not work on amazon-fireos, blackberry, firefoxos, wp7 or tizen")))
