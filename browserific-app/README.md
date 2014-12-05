# Browserific-app template
--------------------------
A Leiningen template for making new browserific ClojureScript apps.

## Usage

This template uses [Om](https://github.com/swannodette/om) by default
because I like to use it in my own apps. To get a basic project with
browserific, Om, and a few other goodies enter this:

```sh
$ lein new browserific-app app-name
```

If using [sablono](https://github.com/r0man/sablono),
[kioo](https://github.com/ckirkendall/kioo), or
[secretary](https://github.com/gf3/secretary) are more your style, you
can include those too! Add a vector after the project name with keys
for each tool you want to use. Here's an example of a project with
sablono and secretary:

```sh
$ lein new browserific-app example [:sablono :secretary]
```

All of the builds for lein-cljsbuild are stored outside of `project.clj`
in a special file called `builds.clj`. This is meant to save you from
micro-managing builds for all the platform (there are 16 target
platforms and counting so far :D). 

To populate `builds.clj`, first build your `config.edn` file using
lein-browserific:

```sh
lein browserific config
```

After that, run the `build` command from lein-browserific:

```sh
lein browserific build
```

The build command will auto-generate builds for all of the platforms you
specified in the configuration. See
[lein-browserific](https://github.com/greenyouse/browserific/lein-browserific)
for more information. 


## Layout

```
├── README.md
├── project.clj
├── builds.clj
├── resources
    ├── public
    ├── desktop
    └── mobile
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
