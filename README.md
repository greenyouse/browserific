# Browserific

Browserific is a framework for building apps in ClojureScript. 

This is intended to be a one-stop shop for ClojureScript apps that 
run on mobile, desktop, or as browser-extensions (wip). Currently,
browserific targets Cordova for mobile and NW.js for desktop. Browser
extensions that run on Chrome, Firefox, Opera, and Safari are future
goals for this project. 


## Quick + Dirty Setup

Make a new browserific project:
```sh
lein new browserific-app awesome-project
```

Set the configuration stuff for browserific:
```sh
lein browserific config
```

Generate build info:
```sh
lein browserific build
```

Run the compiler commands in separate shells:
```sh
lein auto browserific compile
lein cljsbuild auto [platform-dev]
```

Write app.

Deploy it:
```sh
cd resources/[platform-dir]
[insert Cordova or NW.js commands here]
```

## Status

So far the `browserific-app` template, `lein-browserific` plugin, and
the `browserific` library are ready as alpha software.

[![Clojars Project](http://clojars.org/browserific/latest-version.svg)](http://clojars.org/browserific)

[![Clojars Project](http://clojars.org/lein-browserific/latest-version.svg)](http://clojars.org/lein-browserific)


## Contributing

I set up a [Trello](https://trello.com/b/hDlRgiHo/browserific) board
to organize the developement workflow, foster ideas, and discuss some of
the design decisions. If you're thinking of contributing please take
look at the board.

The biggest item to help with right now would be the browser extension
parts. My plan is to work through desktop and mobile first and then
focus on the extensions. Any extension work in the meantime would be
much appreciated! 

This project is meant to
[assimilate](https://www.youtube.com/watch?v=AyenRCJ_4Ww) any noteworthy
frameworks, so if you have suggestions, file an issue to let me
know. Facebook's ReactNative, Mozilla's OpenWebApps, and Google's Chrome
Apps are potential candidates for the future.
