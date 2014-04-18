# General

This project is an attempt at building browser extensions and mobile
apps in one unified format.

## Templating

The browserific-app project is a leiningen template to setup the build
environment. This doesn't do anything particularly fancy. Here is the
structure of a typical browserific-app:

```
[project-name]
|-------project.clj
|-------src
|       |-------background
|       |        |-------background.cljs ;main project file
|       |-------content
|       |        |-------content.cljs ;dom code, widgets, etc.
|       |
|       |-------config.edn ;the structure of the browser extension,
|-------resources
|       |--------[browser vendor] ;holds development files for browsers
|-------intermediate
|       |-------[browser-name] ;used for creating the app's logic
|       |         |-------background
|       |         |        |-------background.cljs ;main project file
|       |         |-------content
|       |                  |-------content.cljs ;dom code, widgets, etc.
|-------README.md
|-------doc
|       |--------intro.md
|-------test
|       |--------test.clj
|-------release ;where the final, production builds go for browsers
|-------dev
        |--------brepl.cljs
```

ClojureScript source code goes into `src` along with a special config
file that is used to generate the native app config. (e.g. manifest.json or
config.xml)
