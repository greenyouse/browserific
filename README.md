# Browserific

Browserific is a framework for building apps in ClojureScript. 

This is intended to be a one-stop for ClojureScript apps that run
on mobile, desktop, or as browser-extensions (wip). Currently,
browserific targets Cordova for mobile, NodeJS for desktop, and
FirefoxOS (mobile). Browser extensions that run on Chrome, Firefox, 
Opera, and Safari are future goals for this project.


## Status

So far the `browserific-app` template and `lein-browserific` plugin are
ready as alpha software, however, the `browserific` library is just 
getting started.


## Contributing

I set up a [Trello](https://trello.com/b/hDlRgiHo/browserific) board
to organize the developement workflow, foster ideas, and discuss some of
the design decisions. If you're thinking of contributing please take
look at the board.

The biggest item to help with right now would be the browser extension
parts. My plan is to work through desktop and mobile first and then
focus on the extensions. Any extension work in the meantime would be
much appreciated! 

Please give me some time to work on the `browserific` library portion
before submitting pull requests for it. Ideas or design input on this
are welcome though.


## License

Copyright © 2014 Ed Babcock

Distributed under the Eclipse Public License version 1.0.
