(ns leiningen.browserific-plugin.config
  "This namespace is for generating config files from config.edn"
  (:require [clojure.data.json :as js]
            [clojure.data.xml :as xml]
            [clojure.string :as st]
            [utils :as u])
  (:use [environ.core :only [env]]
        [dev] ; delete me
        ))

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


(defn nest-parser [acc base nest val]
  )

(defmacro tester
  "Adds a nested option to the output config file."
  [opt val acc]
  (let [base (first opt)
        nest (rest opt)]
    (if (contains? acc base) ; base keyword exists in acc's map
      (if (vector? (base acc)) ; nest is a vector
        '())
      `(case base
         (or "developer" "icons" "browser-action"
             "page-action" "background" "requirements") (assoc acc base
                                                               (map nest val))
             (or "content-scripts" "permissions"
                 "web-accessable-resources" "sandbox") (assoc acc base
                                                              (vec nest val))))))

(defn option-parser
  "Adds a nested map or nested vector depending on the value of base"
  [base nest val acc]
  (cond
   (= base (or :developer :icons :browser-action
               :page-action :background :requirements))
   (assoc acc nest val)
   (= base (or :content-scripts :permissions
               :web-accessable-resources :sandbox))
   (assoc acc nest val)))

(defn nested-options
  "Adds a nested option to the output config file."
  [opt val acc]
  (let [base (first opt)
        nest (first (rest opt))]
    (assoc acc nest val)))

(assoc-in {:content {:action-type "browser"}} [:content :hi] "dude")


(conj (:hi {:hi [:stuff "here"]}) :hi "hi")

(config-reader {:name [:name]
                :icons [:extensions :icons :opera]})

(config-reader {:content-scripts!matches [:extensions :content :icons :opera]
                :content-scripts!browser-action [:extensions :content :action-type]})


(defn config-reader
  "This is a config file DSL for browser extensions. Enter !:
   to indicate a nested config option. Otherwise it will output
   a normal key value pair."
  [conf-map]
  (loop [item (reverse conf-map) acc {}]
    (let [name (first (keys item))
          val (config-get (first (vals item)))]
      (if-not val
        (recur (rest item) acc) ; Option wasn't given in config.edn
        (if (empty? item)
          acc
          (if (re-seq #"!" (str name))
            (recur (rest item) (nested-options
                                (map keyword
                                     (st/split (subs (str name) 1) #"!"))
                                val acc))
            (recur (rest item) (assoc acc name val))))))))



;; parse a map
;; if key has !: then it is nested, so output a nested array
;; else output a normal map with the supplied val

;; get the file contents, find if the thing exists in config.edn
;; if true, give it the key and value
;; else, don't emit anything and move to next value

(defn- name-get
  "Helper fn for finding the project's name"
  []
  (second (read-string (slurp "project.clj"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Config Builders

; FIXME: add _all_ available options to these config generators!
; FIXME: use js/write instead so output is readable
(defn- chrome-config
  "Creates the chrome extension config files using config.edn"
  []
  (spit "resources/extension/chrome/manifest.json"
        (js/write-str {:name (config-get [:name])
                       :version (config-get [:version])
                       :description (config-get [:description])
                       :permissions [:access :permissions]
                                        ; TODO: adjust this later
                       :background {:page "background.html"}
                       :content-scripts
                       [:matches (config-get
                                  [:extensions :activations :urls])
                        :js ["content.js"]
                        :css []]
                       :manifest-version 2})))

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

(case "browser"
  "browser" (do (identity :browser-action)
                (identity "browser"))
  "page" '(:page-action "page"))

;; TODO: add an elide function for when data is missing, put in front of config-get
(defn- opera-config
  "Creates the opera config files using config.edn"
  []
  (spit "resources/extension/opera/manifest.json"
        (js/write-str {:name (config-get [:name])
                       :version (config-get [:version])
                       :description (config-get [:description])
                       :developer {:name (config-get [:author :author-name])
                                   :url (config-get [:author :url])}
                       :icons (config-get [:extensions :icons :opera])
                       :default-locale (config-get [:default-locale])
                       (cond (config-get [:extensions :content :action-type])
                             "browser" (identity :browser-action ))

                       :permissions (config-get [:access :permissions])
                                        ; TODO: adjust this later
                       :background {:page "background.html"}
                       :content-scripts
                       [:matches (config-get
                                  [:extensions :activations :urls])
                        :js ["content.js"]
                        :css []]
                       :manifest-version 2})))

(defn safari-config
  "Creates the safari config files using config.edn"
  []
  (let [doctype "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"]
    (spit "resources/extension/safari/Info.plist"
          (xml/indent-str (xml/sexp-as-element
                           [:widget {:id (config-get [:mobile :id])
                                     :version (config-get [:version])
                                     :xmlns "http://www.w3.org/ns/widgets"
                                     :xmlns:cdv "http://cordova.apache.org/ns/1.0"}])))))


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
