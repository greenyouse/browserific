(ns leiningen.browserific-plugin.config
  "This namespace is for generating config files from config.edn"
  (:require [clojure.data.json :as js]
            [clojure.data.xml :as xml]
            [clojure.string :as st]
            [utils :as u])
  (:use [environ.core :only [env]]))

;; TODO: Certain build options must be injected at compile time. Generate
;; a data file somewhere (dot file?) to keep track of what needs injecting.
;; ex. content-scripts for Firefox

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper Fns

;; FIXME: Add leiningen error if no file is found?
(defn- config-get
  "Helper fn for getting data from the config-file"
  [coll]
  (let [config-file (read-string (slurp
                                  (or (env :config) "src/config.edn")))]
    (get-in config-file coll)))

(defn- nested-options
  "Adds a nested option to the output config file."
  [opt val acc]
  (let [base (first opt)
        nest (second opt)]
    (assoc-in acc [base nest] val)))

(defn- parse-configs [item type acc]
  (let [raw-val (first (vals item)) ; non-optional configs have metadata
        val (if (:browserific (meta raw-val))
              (first raw-val) (config-get raw-val))
        raw-name (first (keys item))
        name (if (re-seq #"!" (str raw-name))
               (map keyword
                    (st/split
                     (subs (str raw-name) 1) #"!"))
               raw-name)]
    (if (empty? item)
      acc
      (if-not (or (false? val) val)
        (recur (rest item) type acc) ; Option wasn't given in config.edn
        (cond
         ;; page/browser action, use special type for name
         (and (seq? name)
              (some #(= % (second name))
                    '(:default-icon :default-popup :default-title)))
         (recur (rest item) type (nested-options (list type (second name)) val acc))
         ;; generic nested option goto nested-options
         (seq? name) (recur (rest item) type (nested-options name val acc))
         :default
         (recur (rest item) type (assoc acc name val)))))))

(defn- config-reader
  "This is a config file DSL for browser extensions. Enter !:
   to indicate a nested config option. Otherwise it will output
   a normal key value pair."
  [conf-map]
  (if (some #(= % :action!type) (keys conf-map)) ; format browser/page actions
      (let [type (keyword (str (config-get (:action!type conf-map)) "-action"))]
        (parse-configs conf-map type {}))
      (parse-configs conf-map nil {})))

;; FIXME: will this work or will it just read the plugin's project.clj?
(defn- name-get
  "Helper fn for finding the project's name"
  []
  (second (read-string (slurp "project.clj"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Config Builders
;;; NOTE: this is a work in progress, not done at all yet

; FIXME: add _all_ available options to these config generators!
; FIXME: use js/write instead so output is readable
(defn- chrome-config
  "Creates the chrome extension config files using config.edn"
  []
  (spit "resources/extension/chrome/manifest.json"
        (js/write-str {})))

(defn- firefox-config
  "Creates the firefox config files using config.edn"
  []
  (spit "resources/extension/firefox/package.json"
        (js/write-str {:name (config-get [:name])
                       :title (config-get [:name])
                       :version (config-get [:version])
                       :id "" ; TODO: Generate valid ids here
                       :description (config-get [:description])
                       :author (config-get [:author :author-name])
                       :license (config-get [:license])})))

(defn- opera-config
  "Creates the opera config files using config.edn"
  []
  (spit "resources/extension/opera/manifest.json"
        (js/write-str (config-reader
                       {:name [:name]
                        :version [:version]
                        :manifest-version ^{:browserific "config"} [2]
                        :description [:description]
                        :developer!name [:author :author-name]
                        :developer!url [:author :url]
                        :icons [:extensions :icons :opera]
                        :default-locale [:default-locale]
                        :action!type [:extensions :action :type]
                        :action!default-icon [:extensions :action :default-icon :opera]
                        :action!default-title [:extensions :action :default-title]
                        :action!default-popup [:extensions :action :default-popup]
                        :background!scripts [:extensions :background :scripts]
                        :background!persistent [:extensions :background :persistent]
                        :web-accessible-resources [:extensions :accessible-content]
                        :content-scripts!matches [:extensions :content :matches]
                        :content-scripts!exclude [:extensions :content :exclude]
                        :content-scripts!js [:extensions :content :js]
                        :content-scripts!css [:extensions :content :css]
                        :homepage [:extensions :homepage]
                        :incognito [:extensions :private]
                        :options-page [:extensions :options-page]
                        :permissions [:extensions :permissions]
                        :update-url [:extensions :update-url :opera]}))))

(defn safari-config
  "Creates the safari config files using config.edn"
  []
  (let [doctype "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"]
    (spit "resources/extension/safari/Info.plist"
          (xml/indent-str (xml/sexp-as-element
                           [:widget {:id (config-get [:mobile :id])
                                     :version (config-get [:version])}])))))


;; FIXME: problem with multiple content + access
;; FIXME: add manifest.webapp file?
(defn- mobile-config
  "Outputs the mobile config files from the config.edn data"
  [system]
  (spit (str "resources/mobile/" (name-get)  "/config.xml")
        (xml/indent-str (xml/sexp-as-element
                         [:widget {:id (config-get [:mobile :id])
                                   :version (config-get [:version])
                                   :xmlns "http://www.w3.org/ns/widgets"
                                   :xmlns:cdv "http://cordova.apache.org/ns/1.0"}
                          [:name (config-get [:name])]
                          [:description (config-get [:description])]
                          [:author {:email (config-get [:author :email])
                                    :href (config-get [:author :url])}
                           (config-get [:author :author-name])]
                          [:content {:src (config-get [:mobile :content])}]
                          [:access {:origin (config-get [:access :permissions])}]]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dispatch Fn

;;FIXME: add leiningen error printouts
(defn build-config
  "Processes the config.edn file to generate JavaScript
   output for the given platforms"
  []
  (let [browsers (config-get [:extensions :browsers])
        mobile (config-get [:mobile :systems])]
    (doseq [vendor browsers]
      (cond
       (= vendor "chrome") '()
       (= vendor "firefox") '()
       (= vendor "opera") '()
       (= vendor "safari") '()
       :else
       (println (str "ERROR: browser " vendor " not supported, options are:\nfirefox, chrome, opera, safari"))))
    (doseq [system mobile] ; FIXME: Remove this loop if we rely on Cordova!
      (cond ;FIXME: add more here?
       (contains? #{"amazon-fire" "ios" "android" "firefox-mobile" "wp7"
                    "wp8" "ubuntu" "blackberry" "tizen"} system)
       (mobile-configs (first system))
       :else
       (println (str "ERROR: mobile system " system " not supported, options are:
 amazon-fire, ios, android, firefox-mobile, wp7, wp8, ubuntu, blackberry, tizen"))))))
