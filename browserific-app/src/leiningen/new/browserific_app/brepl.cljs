(ns {{name}}.brepl
  (:require [weasel.repl :as repl]))


(when-not (repl/alive?)
  (repl/connect "ws://localhost:9001"))
