(ns brepl
  (:require [weasel.repl :as ws-repl]))

(ws-repl/connect "http://localhost:9001/repl")
