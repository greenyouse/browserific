(ns browserific.config.db
  (:refer-clojure :exclude [atom])
  (:require [reagent.core :as reagent :refer [atom]]))

(def config-db
  "The whole config.edn in memory"
  (atom {}))
