(ns browserific.helpers.utils
  (:require [leiningen.core.main :as l]
            [clojure.string :as st]))

;; TODO: look in all profiles, any other spots I missed?
(def options
  "Map of browserific options"
  (let [p (-> "project.clj" slurp read-string (nthrest 3))
        psplit (partition 2 p)
        m (reduce #(into %1 [(into [] %2)])
            {} psplit)]
    (or (:browserific m)
      (get-in m [:profiles :dev :browserific]))))

(def config-file
  "Path of the selected config.edn file to use"
  (or (:config options) "src/config.edn"))

(defn get-config
  "Helper fn that returns the contents of the config file"
  []
  (-> config-file slurp read-string))

(defn select-config
  "Looks up a value from the config file"
  [& ks]
  (get-in (get-config) ks))

(defn yellow-text
  [msg & more]
  (if more
    (str "\033[33m" (reduce #(str % %2) msg more) "\033[0m")
    (str "\033[33m" msg "\033[0m")))

(defn red-text
  [msg & more]
  (if more
    (str "\033[31m" (reduce #(str % %2) msg more) "\033[0m")
    (str "\033[31m" msg "\033[0m")))

(defn- config-warning [e]
  (l/warn (str (red-text e) "\n\n")))


;; here are the different platforms we're targeting
(def browsers
  (-> (try (select-config :extensions :platforms)
           (catch Exception e (config-warning e)))
      set))

(def mobile
  (-> (try (select-config :mobile :platforms)
           (catch Exception e (config-warning e)))
      set))

(def desktop
  (-> (try (select-config :desktop :platforms)
           (catch Exception e (config-warning e)))
      set))

(def platforms
  `#{~@browsers
    ~@mobile
    ~@desktop})

;; these are lists of every platform
(def all-browsers
  #{"firefox" "chrome" "opera" "safari"})

(def all-mobile
  #{"amazon-fireos" "android" "blackberry" "firefoxos" "ios"
    "ubuntu" "wp7" "wp8" "tizen"})

(def all-desktop
  #{"osx32" "osx64" "windows32" "windows64" "linux32" "linux64"})

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
   (l/abort (red-text "Browserific Error: Could not compile file: " file
                      "\n\nBrowserific source files must be in either the background or content directores.\n"))))
