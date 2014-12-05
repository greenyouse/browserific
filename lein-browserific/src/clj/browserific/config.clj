(ns browserific.config
  "Generates config files from config.edn"
  (:require [browserific.helpers.plist :as p]
            [browserific.helpers.utils :as u :refer [get-config]]
            [cheshire.core :as js]
            [clojure.data.xml :as xml]
            [clojure.string :as st]
            [clojure.java.io :as io]
            [leiningen.core.main :as l]))

;; TODO: Certain build options must be injected at compile time. Generate
;; a data file somewhere (dot file?) to keep track of what needs injecting.
;; ex. content-scripts for Firefox
;;
;; Got the injection capability now. Don't forget to add this. It would probably make
;; sense to write special, inner-transition functions in browserific (library) and just
;; call them during the chenex compilation.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Helper Fns

(defn- config-check
  "Validates whether the config file has proper platforms listed"
  [browsers desktops mobiles]
  (letfn [(checker [platform platform-types err]
            (if-not (contains? platform-types platform)
              (l/abort (u/red-text err))))]
    (doseq [browser browsers]
      (checker browser u/browsers
                (str "Browserific Error: browser " browser " not supported, options are:
firefox, chrome, opera, safari")))
    (doseq [desktop desktops]
      (checker desktop u/desktop
               (str "Browserific Error: desktop system " desktop " not supported, options are:
linux32, linux64, osx32, osx64, windows")))
    (doseq [mobile mobiles]
      (checker mobile u/mobile
               (str "Browserific Error: mobile system " mobile " not supported, options are:
 amazon-fire, android, blackberry, firefoxos, ios, ubuntu, wp7, wp8, tizen")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config parsing

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
              (first raw-val) (get-config raw-val))
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
  "This generates a config file from a given map. Enter ! to
  indicate a nested config option. Otherwise it will output a
  normal key value pair."
  [conf-map]
  (if (some #(= % :action!type) (keys conf-map)) ; format browser/page actions
      (let [type (keyword (str (get-config (:action!type conf-map)) "-action"))]
        (parse-configs conf-map type {}))
      (parse-configs conf-map nil {})))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config Builders

;;; Browser Configs

;; NOTE: :key isn't really necessary so we'll leave it out for now (chrome + opera)
;;   and we're omitting :plugins because that is being phased out
(defn- chrome-config
  "Creates the chrome extension config file using config.edn"
  []
  (io/make-parents "resources/extension/chrome/manifest.json")
  (spit "resources/extension/chrome/manifest.json"
        (js/generate-string
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
           :sandbox [:extensions :extra :shared :sandbox]})
         {:pretty true})))

(defn- firefox-config
  "Creates the firefox config file using config.edn"
  []
  (io/make-parents "resources/extension/firefox/package.json")
  (spit "resources/extension/firefox/package.json"
        (js/generate-string
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
           :version [:version]})
         {:pretty true})))

(defn- opera-config
  "Creates the opera config file using config.edn"
  []
  (io/make-parents "resources/extension/opera/manifest.json")
  (spit "resources/extension/opera/manifest.json"
        (js/generate-string
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
           :update-url [:extensions :update-url :opera]})
         {:pretty true})))

;; TODO: make sure that plist output order is not imporant
(defn- safari-config
  "Creates the safari config file using config.edn"
  []
  (io/make-parents "resources/extension/safari/Info.plist")
  (let [doctype "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"]
    (spit "resources/extension/safari/Info.plist"
          (st/replace
           (xml/indent-str
            (xml/sexp-as-element
             (p/plist
              (config-reader
               {:Author [:author :author-name]
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
                :Website [:extensions :homepage]}))))
           "?>" (str "?>\n" doctype "\n")))))

;;; Mobile Configs
(defn- mobile-config
  "Outputs the mobile config file from the config.edn data"
  []
  (let [loc (str "resources/mobile/" u/project-name "/config.xml")]
    (io/make-parents loc)
    (spit loc
          (xml/indent-str (xml/sexp-as-element
                           (-> [:widget {:id (get-config [:mobile :id])
                                         :version (get-config [:version])
                                         :xmlns "http://www.w3.org/ns/widgets"
                                         :xmlns:cdv "http://cordova.apache.org/ns/1.0"}
                                [:name (get-config [:name])]
                                [:description (get-config [:description])]
                                [:author {:email (get-config [:author :email])
                                          :href (get-config [:author :url])}
                                 (get-config [:author :author-name])]
                                [:content {:src (get-config [:mobile :content])}]]
                               (into (map #(identity [:access {:origin %}]) (get-config [:mobile :permissions])))
                               (into (reduce #(conj %1 [:preference %2])
                                             [] (get-config [:mobile :preferences])))
                               (into (reduce #(conj %1 [:cdv:plugin %2])
                                             [] (get-config [:mobile :plugins])))
                               (into (reduce #(conj %1 [:icon %2])
                                             [] (get-config [:mobile :icons])))
                               (into (reduce #(conj %1 [:splash %2])
                                             [] (get-config [:mobile :splash])))))))))

;; TODO: not doing for now, let's get cordova working first
(comment (defn firefoxos-config
  "Creates the manifest.webapp for firefoxos"
  []
  (js/generate-string
 (config-reader ; only a little bit done
  {:name [:name]
   :description [:description]
   :launch-path [:mobile :content]
   :icons [:mobile :firefoxos]
   :developer!name [:author :author-name]
   :developer!url [:author :url]
   :default-locale [:default-locale]
   :activities [:mobile :firefoxos :activities]
   :appcache-path [:mobile :firefoxos :appcache]
   :chrome [:mobile :firefoxos :chrome]})
 {:pretty true})))


;;; Desktop Configs

(defn- desktop-config
  "Generates the node-webkit config file using config.edn"
  [platform]
  (io/make-parents (str "resources/desktop/deploy/" platform "/package.json"))
  (spit (str "resources/desktop/deploy/" platform "/package.json")
        (js/generate-string
         (config-reader
          {:name [:name]
           :main [:desktop :main]
           :nodejs [:desktop :nodejs]
           :node-main [:desktop :node-main]
           :single-instance [:desktop :single-instance]
           :user!%name [:desktop :user :name]
           :user!%ver [:desktop :user :ver]
           :user!%nwver [:desktop :user :nwver]
           :user!%webkit_ver [:desktop :user :webkit-ver]
           :user!%osinfo [:desktop :user :osinfo]
           :node-remote [:desktop :permissions]
           :chromium-args [:desktop :chromium-args]
           :js-flags [:desktop :js-flags]
           :inject-js-start [:desktop :inject-js-start]
           :inject-js-end [:desktop :inject-js-end]
           :snapshot [:desktop :snapshot]
           :dom_storage_quota [:desktop :dom-storage-quota]
           :keywords [:desktop :keywords]
           :bugs [:desktop :bugs]
           :repositories!type [:desktop :repositories :type]
           :repositories!url [:desktop :repositories :url]
           :repositories!path [:desktop :repositories :path]
           :window!title [:desktop :window :title]
           :window!width [:desktop :window :width]
           :window!height [:desktop :window :height]
           :window!toolbar [:desktop :window :toolbar]
           :window!icon [:desktop :window :icon]
           :window!position [:desktop :window :position]
           :window!min_width [:desktop :window :min-width]
           :window!min_height [:desktop :window :min-height]
           :window!max_width [:desktop :window :max-width]
           :window!max_height [:desktop :window :max-height]
           :window!as_desktop [:desktop :window :as-desktop]
           :window!resizable [:desktop :window :resizable]
           :window!always_on_top [:desktop :window :always-on-top]
           :window!fullscreen [:desktop :window :fullscreen]
           :window!show_in_taskbar [:desktop :window :show-in-taskbar]
           :window!frame [:desktop :window :frame]
           :window!show [:desktop :window :show]
           :window!kiosk [:desktop :window :kiosk]
           :webkit!plugin [:desktop :webkit :plugins]
           :webkit!java[:desktop :webkit :java]
           :webkit!page-cache [:desktop :webkit :page-cache]})
         {:pretty true})))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Dispatch Fn

(defn build-configs
  "Processes the config.edn file to generate JavaScript
   output for the given platforms"
  []
  (let [browsers u/browsers
        desktops u/desktop
        mobile u/mobile]
    (config-check browsers desktops mobile)
    (doseq [vendor browsers]
      (cond
       (= vendor "chrome") (chrome-config)
       (= vendor "firefox") (firefox-config)
       (= vendor "opera") (opera-config)
       (= vendor "safari") (safari-config)))
    (cond
     (= [] mobile) '()
     :else
     (mobile-config))
    (doseq [vendor desktops]
      (cond
       (= vendor "linux32" ) (desktop-config "linux32")
       (= vendor "linux64" ) (desktop-config "linux64")
       (= vendor "osx32") (desktop-config "osx32")
       (= vendor "osx64") (desktop-config "osx64")
       (= vendor "windows") (desktop-config "windows")))))
