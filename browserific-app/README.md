# Browserific-app template
--------------------------
A Leiningen template for ClojureScript browser extensions


## Status
---------

A basic template has been written for now but it will probably be rewritten
later on.

## Usage

This project has not been released yet, so it will need to be cloned and
installed manually for now.

To make a new browserific project, just do this:

```sh
lein new browserific-app example
```


## Layout

```
├── README.md
├── project.clj
├── release
    └── [browser vendor]
├── resources
    └── [browser vendor]
├── intermediate
    └── [browser vendor]
├── src
    ├── background
        └── background.cljs
    ├── content
        └── content.cljs
    └── config.edn
├── dev
    └── brepl.cljs
```

## License
----------

Copyright © 2014 Ed Babcock

Distributed under the Eclipse Public License 1.0.
