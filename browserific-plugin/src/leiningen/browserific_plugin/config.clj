(ns leiningen.browserific-plugin.config
  "This namespace is for generating config files from config.edn"
  (:require [leiningen.browserific-plugin.helpers.plist :as p]
            [clojure.data.json :as js]
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
    (if (= 2 (count opt))
      (assoc-in acc [base nest] val) ; single nested vs double nested
      (assoc-in acc [base nest (nth opt 2)] val))))

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
  "This is a config file DSL for browser extensions. Enter !
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
;;; FIXME: I'm including full paths for common options so I can edit config.edn names
;;;  without having the system break (otherwise it won't output correct JSON names).
;;;  Eventually we should take lots of ! nesting out to make this more succinct.

;;; TODO: Could we give an all option that, if it exists, takes precedence over anything else?
;;;  To allow cross-browser apps have a less verbose config.edn.

;; NOTE: :key isn't really necessary so we'll leave it out for now (chrome + opera)
;; we're omitting :plugins because that is being phased out
;; FIXME: add _all_ available options to these config generators!
;; FIXME: use js/write (or something better) instead so output is readable
(defn- chrome-config
  "Creates the chrome extension config files using config.edn"
  []
  (spit "resources/extension/chrome/manifest.json"
        (js/write-str
         (config-reader
          {:name [:name]
           :version [:version]
           :manifest-version ^{:browserific "config"} [2]
           :description [:description]
           :developer!name [:author :author-name]
           :developer!url [:author :url]
           :icons [:extensions :icons :chrome]
           :default-locale [:default-locale]
           :action!type [:extensions :action :type]
           :action!default-icon [:extensions :action :default-icon :chrome]
           :action!default-title [:extensions :action :default-title]
           :action!default-popup [:extensions :action :default-popup]
           :background!scripts [:extensions :background :scripts]
           :background!persistent [:extensions :background :persistent]
           :content-scripts!matches [:extensions :content :matches]
           :content-scripts!exclude [:extensions :content :exclude]
           :content-scripts!js [:extensions :content :js]
           :content-scripts!css [:extensions :content :css]
           :homepage [:extensions :homepage]
           :incognito ^{:browserific "config"} ["spanning"] ; split is for legacy
           :options-page [:extensions :options-page]
           :permissions [:extensions :permissions]
           :requirements [:extensions :extra :shared :requirements]
           :web-accessible-resources [:extensions :web-accessible-resources]
           :update-url [:extensions :update-url :chrome]

           :chrome-ui-overrides [:extensions :extra :chrome :chrome-ui-overrides]
           :chrome-settings-overrides [:extensions :extra :chrome :chrome-settings-overrides]
           :chrome-url-overrides [:extensions :extra :chrome :chrome-url-overrides]
           :commands [:extensions :extra :chrome :commands]
           :current-locale [:extensions :extra :chrome :current-locale]
           :devtools_page [:extensions :extra :chrome :devtools_page]
           :externally-connectable [:extensions :extra :chrome :externally-connectable]
           :file-browser-handlers [:extensions :extra :chrome :file-browser-handlers]
           :import [:extensions :extra :chrome :import]
           :input-components [:extensions :extra :chrome :input-components]
           :minimum-chrome-version [:extensions :extra :chrome :minimum-chrome-version]
           :nacl-modules [:extensions :extra :chrome :nacl-modules]
           :oauth2 [:extensions :extra :chrome :oauth2]
           :offline-enabled [:extensions :extra :chrome :offline-enabled]
           :omnibox [:extensions :extra :chrome :omnibox]
           :optional-permissions [:extensions :extra :chrome :optional-permissions]
           :platforms [:extensions :extra :chrome :platforms]
           :script-badge [:extensions :extra :chrome :script-badge]
           :short-name [:extensions :extra :chrome :short-name]
           :signature [:extensions :extra :chrome :signature]
           :spellcheck [:extensions :extra :chrome :spellcheck]
           :storage [:extensions :extra :chrome :storage]
           :system-indicator [:extensions :extra :chrome :system-indicator]
           :tts-engine [:extensions :extra :chrome :tts-engine]
           :sandbox [:extensions :extra :shared :sandbox]}))))

(defn- firefox-config
  "Creates the firefox config files using config.edn"
  []
  (spit "resources/extension/firefox/package.json"
        (js/write-str
         (config-reader
          {:author [:author :author-name]
           :contributors [:author :contributors]
           :dependencies [:extensions :extra :firefox :dependencies]
           :fullName [:name]
           :homepage [:extensions :homepage]
           :icon [:extensions :icons :firefox :48]
           :icon64 [:extensions :icons :firefox :64]
           :id [:author :author-id]
           :lib [:extensions :extra :firefox :lib]
           :license [:license]
           :main [:extensions :extra :firefox :main]
           :name [:extensions :extra :firefox :name]
           :packages [:extensions :extra :firefox :packages]
           :permissions!private-browsing [:extensions :private]
           :permissions!cross-domain-content [:extensions :extra :firefox :permissions]
           :preferences [:extensions :extra :firefox :preferences]
           :tests [:extensions :extra :firefox :tests]
           :title [:extensions :extra :firefox :title]
           :translators [:extensions :extra :firefox :translators]
           :version [:version]}))))

(defn- opera-config
  "Creates the opera config files using config.edn"
  []
  (spit "resources/extension/opera/manifest.json"
        (js/write-str
         (config-reader
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
           :content-scripts!matches [:extensions :content :matches]
           :content-scripts!exclude [:extensions :content :exclude]
           :content-scripts!js [:extensions :content :js]
           :content-scripts!css [:extensions :content :css]
           :homepage [:extensions :homepage]
           :incognito ^{:browserific "config"} ["spanning"] ; split is for legacy
           :options-page [:extensions :options-page]
           :permissions [:extensions :permissions]
           :requirements [:extensions :extra :shared :requirements]
           :web-accessible-resources [:extensions :web-accessible-resources]
           :update-url [:extensions :update-url :opera]}))))

(defn safari-config
  "Creates the safari config files using config.edn"
  []
  (let [doctype "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"]
    (spit "resources/extension/safari/Info.plist"
          (xml/indent-str (xml/sexp-as-element
                           [:widget {:id (config-get [:mobile :id])
                                     :version (config-get [:version])}])))))


;; TODO: inject special safari header into the plist
;; TODO: formating this into the DSL isn't critical but it may be nice for
;; when the configs need to be tweaked, plus it's good programming practice
(p/plist)
(config-reader
 {:author [:author :author-name]
  :Builder-.-Version ^{:browserific "config"} ["6534.59.10"] ; Shouldn't matter
  :CFBundleDisplayName [:name]
  :CFBundleIdentifier [:author :author-id]
  :CFBundleInfoDictionaryVersion ^{:browserific "config"} ["6.0"] ; Shouldn't matter
  :CFBundleShortVersionString [:version]
  :CFBundleVersion ^{:browserific "config"} ["1.0"]; FIXME: lookup what this value signifies
  :Chrome!Bars [:extensions :extra :safari :bars]
  :Chrome!Context-.-Menu-.-Items [:extensions :extra :safari :context-items]
  :Chrome!Database-.-Quota [:extensions :extra :safari :database-quota]
  :Chrome!Menus [:extensions :extra :safari :menus]
  :Content!Blacklist [:extensions :content :exclude]
  :Content!Scripts!Start[:extensions :extra :safari :start-script]
  :Content!Scripts!End [:extensions :content :js]
  :Content!Stylesheets [:extensions :content :css]
  :Content!Whitelist [:extensions :content :matches]
  :Description [:description]
  :ExtensionInfoDictionaryVersion ^{:browserific "config"} ["1.0"] ; FIXME: what is this?
  :Permissions!Website-.-Access!Allowed-.-Domains [:extensions :permissions] ; FIXME: are options like "tabs" just negligeable?
  :Permissions!Website-.-Access!Include-.-Secure-.-Pages [:extensions :private]
  :Permissions!Website-.-Access!Level [:extensions :extra :safari :access-level]
  :Update-.-Manifest-.-URL [:extensions :update-url :safari]
  :Website [:extensions :homepage]})


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
