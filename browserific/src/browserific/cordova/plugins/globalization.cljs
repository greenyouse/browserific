(ns browserific.cordova.plugins.globalization
  "Handles Cordova's globalization plugin"
  (:require [greenyouse.chenex :as chenex]))

(defn get-language
  "Get the BCP 47 language tag for the client's current language (i.e. 'en').

  Doesn't work on Tizen or WP7

  * success -- onSuccess callback, gives a Language object with a value field
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.

  quirks:
  - android: Returns the ISO 639-1 two-letter language code, upper case ISO
             3166-1 country code and variant separated by hyphens. Examples:
             'en', 'en-US', 'US'
  - wp8: Returns the ISO 639-1 two-letter language code, upper case ISO
         3166-1 country code and variant separated by hyphens. Examples:
        'en', 'en-US', 'US'"
  [success err]
  (chenex/ex! [:tizen :wp7]
    (js/navigator.globalization.getPreferredLanguage success err)))

(comment (throw (js/Error. "Browserific Error: globalization/get-language does not work on tizen or wp7")))

(defn get-locale
  "Returns the BCP 47 compliant locale tag for the client's current language
  (i.e. 'en-US').

  Doesn't work on Tizen or WP7

  * success -- onSuccess callback, gives a Language object with a value field
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.

  quirks:
  - anroid: doesn't distinguish between language and locale, so this is
            equivalent to get-language
  - wp8: Returns the ISO 639-1 two-letter language code and ISO 3166-1 country
         code of the regional variant corresponding to the 'Regional Format'
         setting, separated by a hyphen."
  [success err]
  (chenex/ex! [:tizen :wp7]
    (js/navigator.globalization.getLocaleName success err)))

(comment (throw (js/Error. "Browserific Error: globalization/get-locale does not work on tizen or wp7")))

(defn date->string
  "Returns a date formatted as a string according to the client's locale and
  timezone.

  Doesn't work on Tizen or WP7

  * date -- a JS date object, (js/Date.)
  * success -- onSuccesss callback, gives the an object with a value field that
               contains the formatted date string
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map of options with fields :formatLength and :selector

  quirks:
  - android: has a 4 digit year instead of 2 digits
  - firefoxos: formatLength is set by the system, custom values are ignored
  - wp8: formatLength supports only short and full values. The 'date and time'
         pattern is always a full datetime format. The value may not completely
         align with user's ICU, depending on the locale."
  [date success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:tizen :wp7]
      (js/navigator.globalization.dateToString date success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/date->string does not work on tizen or wp7")))

(defn get-currency
  "Returns a pattern string to format and parse currency values according to the
  client's user preferences and ISO 4217 currency code.

  Doesn't work on FirefoxOS, Tizen, WP7, or WP8

  * currency -- a string of an ISO 4217 currency code
  * successs -- onSuccess callback, gives an object with pattern, code, fraction,
                rouding, decimal, and grouping fields.
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields."
  [currency successs err]
  (chenex/ex! [:firefoxos :tizen :wp7 :wp8]
    (js/navigator.globalization.dateToString date success err opts)))

(comment (throw (js/Error. "Browserific Error: globalization/get-currency does not work on firefoxos, tizen, wp7, or wp8")))

(defn date-names
  "Returns an array of the names of the months or days of the week, depending on
  the client's user preferences and calendar.

  Doesn't work on Tizen or WP7

  * success -- onSuccess callback, gives an object with a value field that holds
               a vector of dates as strings
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with :type and :item fields

  quirks:
  - firefoxos: options.type supports a genitive value, important for some
               languages
  - wp8: The array of months contains 13 elements. The returned array may be not
         completely aligned with ICU depending on a user locale."
  [success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:tizen :wp7]
      (js/navigator.globalization.getDateNames success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/date-names does not work on tizen or wp7")))

(defn date-pattern
  "Returns a pattern string to format and parse dates according to the client's
  user preferences.

  Doesn't work on Tizen or WP7

  * success -- onSuccess callback, gives an object with pattern, timezone,
               utc_offset, and dst_offset fields
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with :formatLength and :selector fields

  quirks:
  - wp8: The formatLength supports only short and full values.The pattern for
         date and time pattern returns only full datetime format. The timezone
         returns the full time zone name. The dst_offset property is not
         supported, and always returns zero. The pattern may be not completely
         aligned with ICU depending on a user locale."
  [success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:tizen :wp7]
      (js/navigator.globalization.getDatePattern success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/date-pattern does not work on tizen or wp7")))

(defn first-day-of-week
  "Returns the first day of the week in number format (1-7) according to the
  client's user preferences and calendar.

  Doesn't work on Tizen or WP7

  * success -- onSuccess callback, gives an object with a value field that
               has the first day of week number (starting with Sunday as 1).
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields."
  [success err]
  (chenex/ex! [:tizen :wp7]
    (js/navigator.globalization.getFirstDayOfWeek success err)))

(comment (throw (js/Error. "Browserific Error: globalization/first-day-of-week does not work on tizen or wp7")))

(defn number-pattern
  "Returns a pattern string to format and parse numbers according to the
  client's user preferences.

  Doesn't work on FirefoxOS, Tizen, or WP7

  * success -- success callback, gives an object with pattern, symbol, fraction,
               rounding, positive, negative, decimal, and grouping fields
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with a :type field

  quirks:
  - wp8: The pattern property is not supported, and returns an empty string. The
         fraction property is not supported, and returns zero."
  [successs err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:firefoxos :tizen :wp7]
      (js/navigator.globalization.getNumberPattern success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/number-pattern does not work on firefoxos, tizen, or wp7")))

(defn daylight-savings?
  "Indicates whether daylight savings time is in effect for a given date using
  the client's time zone and calendar.

  Doesn't work on Tizen or WP7

  * date -- a JS date object, (js/Date.)
  * success -- onSuccess callback, gives an object with a dst field as a boolean
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields."
  [date success err]
  (chenex/ex! [:tizen :wp7]
    (js/navigator.globalization.getNumberPattern date success err)))

(comment (throw (js/Error. "Browserific Error: globalization/daylight-savings? does not work on tizen or wp7")))

(defn number->string
  "Returns a number formatted as a string according to the client's user
  preferences.

  Doesn't work on FirefoxOS, Tizen, or WP7

  * number -- a number to format
  * success -- onSuccess callback, gives an object with a value field that holds
               the string
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with a :type field"
  [number success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:firefoxos :tizen :wp7]
      (js/navigator.globalization.numberToString number success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/number->string does not work on firefoxos, tizen, or wp7")))

(defn string->date
  "Parses a date formatted as a string, according to the client's user
  preferences and calendar using the time zone of the client, and returns the
  corresponding date object.

  Doesn't work on Tizen or WP7

  * dstr -- a date in string format
  * success -- onSuccess callback, gives an object with year, month, day, hour,
               minute, second, and millisecond fields
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with :formatLength and :selector fields

  quirks:
  - wp8: The formatLength option supports only short and full values. The
         pattern for 'date and time' selector is always a full datetime format.
         The inbound dstr parameter should be formed in compliance with a pattern
         returned by getDatePattern. This pattern may be not completely aligned
         with ICU depending on a user locale."
  [dstr success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:tizen :wp7]
      (js/navigator.globalization.stringToDate dstr success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/string->date does not work on tizen or wp7")))

(defn str->number
  "Parses a number formatted as a string according to the client's user
  preferences and returns the corresponding number.

  Doesn't work on Tizen or WP7

  * nstr -- a string with numbers
  * success -- onSuccess callback, gives an object with a value field that holds
               the number
  * err -- onError callback, gives a GlobalizationError object with code and
           message fields.
  * opts -- a map with a :type field

  quirks:
  - wp8: In case of percent type the returned value is not divided by 100."
  [nstr success err & opts]
  (let [o (clj->js (first opts))]
    (chenex/ex! [:tizen :wp7]
      (js/navigator.globalization.stringToNumber nstr success err o))))

(comment (throw (js/Error. "Browserific Error: globalization/str->number does not work on tizen or wp7")))
