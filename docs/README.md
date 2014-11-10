# General

This project is an attempt at building browser extensions, mobile apps,
and desktop apps in one unified format. The design is in flux so please 
take it with a grain of salt. Things will likely shift around as we hit 
obstacles. 

## Templating

The browserific-app project is a leiningen template to setup the build
environment. This doesn't do anything particularly fancy. Here is the
structure of a typical browserific-app:

```
[project-name]
|-------project.clj
|
|-------src
|       |-------background
|       |        |-------background.cljs ;main project file
|       |
|       |-------content
|       |        |-------content.cljs ;dom code, widgets, etc.
|       |
|       |-------config.edn ;the structure of the project 
|
|-------resources
|       |--------extensions
|       |         |-------[browser vendor] ;holds app files for browsers
|       |
|       |--------mobile
|       |         |-------[project-name] ;holds app files for mobile
|       |
|       |--------desktop
|                 |-------[project-name] ;holds app files for desktop
|
|-------intermediate
|       |-------[platform-name] 
|                 |-------background
|                 |        |-------background.cljs ;main project file
|                 |-------content
|                          |-------content.cljs ;dom code, widgets, etc.
|
|-------README.md
|
|-------doc
|       |--------intro.md
|
|-------test
|       |--------test.clj
|
|-------release ;where the final, production builds go
|-------dev
        |--------brepl.cljs
```

ClojureScript source code goes into `src` along with a special config
file that is used to generate the native app config (e.g. manifest.json or
config.xml).

## Plugin

The plugin is a LISP pre-processor much like cljx that will convert browser
agnostic code (containing optional LISP feature expressions) into browser
specific code. It works by first reading the code, then addressing the feature
expressions, and finally outputting normal ClojureScript code that can be
compiled by the ClojureScript compiler. 

`src -> intermediate (auto-generated) -> js files`

Unfortunately browser extensions usually have a separate JS file for DOM,
a `content script`, and browser chrome, a `background/event page`. The 
content script sends messages using JS callbacks over `ports`. This would be 
great to abstract away with the library later and `^:export` hints should be 
put on the callback functions so advanced compilation can still be used.

The second thing that the plugin does is convert a global configuration file
called `config.edn`. This will write the respective config files for each app
environment. There are three main browser configs (opera is a subset of chrome) and
the Cordova config to deal with. Unfortunately, not all the browser configs are
the same. For example, the Firefox content scripts are written into the app
itself using the `page-mod` SDK module. In the other browsers, this feature is
written in the config file. For instances like this it may be best to
auto-generate a special namespace that specifically injects the necessary
code. For this example that would mean adding a piece of code that looks like
this (although we would have a ClojureScript version not JS):

```js
var data = require("sdk/self").data;
var pageMod = require("sdk/page-mod");

pageMod.PageMod({
  include: "*.mozilla.org",
  contentScriptFile: [data.url("jquery-1.7.min.js"),
                      data.url("my-script.js")]
});
```

More details on content scripts will be written later (i.e. writing specialized
content script files in ClojureScript that can be used for specific url addresses).

## Library
To work with the 5 JavaScript APIs (chrome, firefox, opera, safari, and cordova)
we'll need to create a high-level, abstract library to write our ClojureScript 
code in. The goal is to have a unified API in ClojureScript that doesn't deal 
with the incidental complexity of things like JS callbacks. I want to code apps
without having to think about the specifics of the browser APIs or low-level JS
details. 

The first step in the creation of the library will be writing a ClojureScript
API proposal. This wouldn't be the actual API but more of an exercise in
thinking about what should go into the API for later. Each of the five
environments should have wrappers that conform to this unified API in order to
achieve cross-browser interop. This standardized codebase could then serve as a
platform for constructing a more abstract, public API that would be used by app
developers.


## Good Sites to Bookmark
Here are some useful resources:

* [Chrome Extensions](https://developer.chrome.com/extensions/api_index)
* [Firefox Extensions](https://developer.mozilla.org/en-US/Add-ons)
* [Opera Extensions](http://dev.opera.com/extension-docs/)
* [Safari Extensions](https://developer.apple.com/library/safari/documentation/UserExperience/Reference/SafariExtensionsReference/_index.html#//apple_ref/doc/uid/TP40009800)
* [Cordova Apps](http://cordova.apache.org/docs/en/3.4.0/)
* [node-webkit](https://github.com/rogerwang/node-webkit/wiki)
