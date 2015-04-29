# Browserific

A library for JS framework interop.

This is a wrapper that allows for calling different JavaScript
frameworks. Right now the plan is to target
[Cordova](https://cordova.apache.org/) and then work on browser
extensions. 

## Status

There is a first draft of the cordova library but it will need some more
tweaking before it's ready for a first release.

## Usage

The Cordova section of Browserific is broken up into the lifecycle
events and various Cordova plugins. Each function in the library is
basically a ClojureScript wrapping of the Cordova API with chenex
feature expressions to help manage any inconsistencies between
platforms. For example let's look at the function for playing a song:

```clj
(defn play [media]
  (chenex/ex! [:amazon-fire :firefoxos] (.play media)))
```

It can be called from any platform but on AmazonFire and FirefoxOS
Cordova's media plugin won't work. To avoid littering your code with
feature expressions, the function will just evaluate to `nil` when it
can't be used.

Unfortunately there are still quite a few little conflicts between the
mobile platforms. Some functions that work on multiple systems may
produce slightly different results (like the beep plugin that makes
slightly different sounds depending on the mobile OS). I wrote quite a
bit of documentation in the function comments so if you're going to use
plugins, try reading each comment for an extensive list of quirks. 

The `browserific.cordova.core/events` macro handles all of the
lifecycle events for Cordova. It just takes a map with lifecycle event
names as keys and callbacks as values:

```clj
(ns example
  (:require [browserific.cordova.core.events :as ev]))
  
(defn app-start []
  (js/console.log "Hello World!"))
  
(ev/events {:ready app-start})
```

## License

Copyright © 2014 Ed Babcock

Distributed under the Eclipse Public License v1.0
