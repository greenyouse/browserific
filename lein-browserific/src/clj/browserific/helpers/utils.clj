(ns browserific.helpers.utils
  (:require [clojure.set :as s]
            [clojure.string :as st]
            [leiningen.core.main :as l]
            [plugin-helpers.core :as p]))

(def project-opts
  "Map of browserific options"
  (or (p/get-project-value :browserific)
      (p/get-project-value :profiles :dev :browserific)))

(def config-file
  "Path of the selected config.edn file to use"
  (or (:config project-opts) "src/config.edn"))

(defn get-config
  "Helper fn that returns the contents of the config file"
  []
  (-> config-file slurp read-string))

(defn select-config
  "Looks up a value from the config file"
  [& ks]
  (get-in (get-config) ks))

(defn- config-warning [e]
  (p/warning (str e "\n\n")))


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
  (clojure.set/union browsers mobile desktop))

;; these are all available platforms
(def all-browsers
  #{"firefox" "chrome" "opera" "safari"})

(def all-mobile
  #{"amazon-fireos" "android" "blackberry" "firefoxos" "ios"
    "ubuntu" "wp7" "wp8" "tizen"})

(def all-desktop
  #{"osx32" "osx64" "windows32" "windows64" "linux32" "linux64"})

(def project-name
  (p/get-project-name))

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
   (p/abort (str "Browserific Error: Could not compile file: " file
              "\n\nBrowserific source files must be in either the background or content directores.\n"))))
