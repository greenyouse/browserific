# browserific-app template

A Leiningen template for making new apps with browserific.

## Usage

This template uses [Reagent](https://github.com/reagent-project/reagent) by default
because I like to use it in my own apps. To get a basic project with
browserific, Reagent, and a few other goodies enter this:

```sh
$ lein new browserific-app [app-name]
```

All of the builds for lein-cljsbuild are stored outside of `project.clj`
in a special directory called `builds`. This is meant to save you from
micro-managing builds for all the platforms (there are 19 and counting
so far :D).   

To populate `builds`, first build your `config.edn` file using
lein-browserific:

```sh
lein browserific config
```

After that, run the `build` command from lein-browserific:

```sh
lein browserific build
```

The `build` command will auto-generate builds for all of the platforms
you specified in the configuration. See
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

Distributed under the GNU General Public License version 3.0
