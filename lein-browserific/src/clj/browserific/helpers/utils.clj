(ns browserific.helpers.utils
  (:require [leiningen.core.main :as l]
            [clojure.string :as st]))

(defn member? [i coll]
  (some #(= i %) coll))

(def ^:private options
  "Map of browserific options"
  (let [p (-> "project.clj" slurp read-string (nthrest 3))
        psplit (partition 2 p)]
    (reduce #(into %1 [(into [] %2)])
            {} psplit)))

(def config-file
  "Path of the selected config.edn file to use"
  (or (:config (:browserific options)) "src/config.edn"))

(defn get-config
  "Helper fn for getting data from the config-file"
  [coll]
  (let [conf (-> config-file slurp read-string)]
    (get-in conf coll)))

(defn yellow-text [msg] (str "\033[33m" msg "\033[0m"))
(defn red-text [msg] (str "\033[31m" msg "\033[0m"))

(defn- config-warning [e]
  (l/warn (str (red-text e) "\n\n")))

(def systems
  {:browsers (try (get-config [:extensions :platforms]) (catch Exception e (config-warning e)))
   :mobile (try (get-config [:mobile :platforms]) (catch Exception e (config-warning e)))
   :desktop (try (get-config [:desktop :platforms]) (catch Exception e (config-warning e)))})

(def platforms
  `[~@(:browsers systems)
    ~@(:mobile systems)
    ~@(:desktop systems)])

(def project-file
  (-> "project.clj" slurp))

;; HACK: matches the project name using this crappy regex, works OK
(def project-name
  (-> (re-find #"\(defproject [A-Za-z1-9-_/.]+" project-file)
      (st/split #" ")
      (second)))

(defn sub-file-location
  "Gives the base name of a file and the path from
  the background or content directory. This helps
  lein-cljsbuild parse files correctly."
  [file]
  (cond
   (not-empty (re-seq #"background/" file)) (->> (.indexOf file "background/")
                                                 (subs file))
   (not-empty (re-seq #"content/" file)) (->> (.indexOf file "content")
                                              (subs file))
   :default
   (l/abort (red-text (str "Browserific Error: Could not compile file: " file
                           "\n\nBrowserific source files must be in either the background or content directores.\n")))))
