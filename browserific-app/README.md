# Browserific-app template
--------------------------
A Leiningen template for ClojureScript app developement.


## Status
---------

A basic template has been written for now but it will probably be rewritten
later on.

## Usage

This project has not been released yet, so it will need to be cloned and
installed manually for now.


This template uses [Om](https://github.com/swannodette/om) by default
because I like to use it in my own apps. To get a basic project with
browserific, Om, and a few other goodies enter this:

```sh
lein new browserific-app example
```

If using [sablono](https://github.com/r0man/sablono),
[kioo](https://github.com/ckirkendall/kioo), or
[secretary](https://github.com/gf3/secretary) are more your style, you
can include those too! Add a vector after your project name with keys
for each tool you want to use. Here's an example of a project with
sablono and secretary:

```sh
lein new browserific-app example [:sablono :secretary]
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

Distributed under the Eclipse Public License version 1.0.
