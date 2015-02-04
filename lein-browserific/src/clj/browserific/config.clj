(ns browserific.config
  "Generates config files from config.edn"
  (:require [browserific.helpers.plist :as p]
            [browserific.helpers.utils :as u :refer [get-config]]
            [cheshire.core :as js]
            [clojure.data.xml :as xml]
            [clojure.string :as st]
            [clojure.java.io :as io]
            [leiningen.core.main :as l]))

;; TODO: Not sure if necessary but some things, such as Firefox Extension stuff,
;; can be injected with chenex's inner transformations. Error checking for
;; feature expressions could also be done using inner transformations.

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
linux32, linux64, osx32, osx64, windows32, windows64")))
    (doseq [mobile mobiles]
      (checker mobile u/mobile
               (str "Browserific Error: mobile system " mobile " not supported, options are:
 amazon-fire, android, blackberry, firefoxos, ios, ubuntu, wp7, wp8, tizen")))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config parsing

;; FIXME: This still feels yucky because my transformations are tied closely to
;; my data structure. There should be a more abstract, data-independent way to
;; handle this. That would be much better for the long term.
(defn- nested-options
  "Adds a nested option to the output config file."
  [[a ad d :as ks] val acc]
  (if (nil? d)
    (assoc-in acc [a ad] val) ; single nested
    (assoc-in acc [a ad d] val))) ; double nested

(defn- icon-parser
  "Formats the icons for output."
  [type name val acc]
  (let [v (reduce #(assoc % (keyword (:size %2)) (:src %2))
            {} val)]
    (if (seq? name)
      (nested-options [type (second name)] v acc)
      (assoc acc name v))))

(defn- parse-configs [[[k v] d :as item] type acc]
  (let [val (if (meta v) (first v)  ;required opts have tagged metadata
              (get-config v))
        name (if (re-seq #"!" (str k))
               (as-> (str k) sk
                 (subs sk 1)
                 (st/split sk #"!")
                 (map keyword sk))
               k)]
    (if (empty? item)
      (into {} acc)
      ;; add clause for required opts (was metadata)
      (cond

        ;; drop :action!type because it doesn't go into config files
        (= :action!type k) (recur (rest item) type acc)

        ;; drop any nil values
        (or (nil? val) (= {} val) (= "" val) (= [] val))
        (recur (rest item) type acc)

        ;; icons need to be formated and then sent to nested-options
        (or (= :icons name) (and (seq? name)
                             (some #(#{:default-icon :icons :splash} %) name)))
        (recur (rest item) type (icon-parser type name val acc))

        ;; page/browser actions require special formatting
        (and (seq? name) (some #(#{:action} %) name))
        (recur (rest item) type
          (nested-options [type (second name)] val acc))

        ;; generic nested options go to nested-options
        (seq? name) (recur (rest item) type (nested-options name val acc))

        :default
        (recur (rest item) type (assoc acc name val))))))

(defn- config-reader
  "This generates a config file from a given map. Enter ! to
  indicate a nested config option. Otherwise it will output a
  normal key value pair."
  [conf-map]
  (if (some #(= % :action!type) (keys conf-map)) ; format browser/page actions
      (let [type (keyword (str (get-config (:action!type conf-map)) "-action"))]
        (parse-configs (vec conf-map) type {}))
      (parse-configs (vec conf-map) nil {})))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config Builders

;; TODO: These configs are too verbose, let's just pass fields that need
;; special formatting to config-reader and pull the rest directly from
;; config.edn. The pulled values will be filtered with empty?.

;;; Browser Configs

;; NOTE: :key isn't really necessary so we'll leave it out for now (chrome + opera)
;;   and we're omitting :plugins because that is being phased out
(defn- chrome-config
  "Creates the chrome extension config file using config.edn"
  []
  (io/make-parents (str "resources/extension/chrome/" u/project-name "/manifest.json"))
  (spit (str "resources/extension/chrome/" u/project-name "/manifest.json")
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
           ;; FIXME default-icon
           :action!default-icon [:extensions :action :default-icon]
           :action!default-title [:extensions :action :default-title]
           :action!default-popup [:extensions :action :default-popup]
           :background!scripts [:extensions :background :scripts]
           :background!persistent [:extensions :background :persistent]
           :content-scripts!matches [:extensions :content :matches]
           :content-scripts!exclude [:extensions :content :exclude]
           :content-scripts!js [:extensions :content :js]
           :content-scripts!css [:extensions :content :css]
           :homepage [:extensions :homepage]
           :incognito [:extensions :incognito]
           :options-page [:extensions :options-page]
           :permissions [:extensions :permissions]
           :requirements [:extensions :requirements]
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
           :sandbox [:extensions :sandbox]})
         {:pretty true})))

(defn- firefox-config
  "Creates the firefox config file using config.edn"
  []
  (io/make-parents (str "resources/extension/firefox/" u/project-name "/package.json"))
  (spit (str "resources/extension/firefox/" u/project-name "/package.json")
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
           :permissions!cross-domain-content [:extensions :extra :firefox :permissions :cross-domain-content]
           :preferences [:extensions :extra :firefox :preferences]
           :tests [:extensions :extra :firefox :tests]
           :title [:extensions :extra :firefox :title]
           :translators [:extensions :extra :firefox :translators]
           :version [:version]})
         {:pretty true})))

(defn- opera-config
  "Creates the opera config file using config.edn"
  []
  (io/make-parents (str "resources/extension/opera/" u/project-name "/manifest.json"))
  (spit (str "resources/extension/opera/" u/project-name "/manifest.json")
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
           :incognito [:extensions :incognito]
           :options-page [:extensions :options-page]
           :permissions [:extensions :permissions]
           :requirements [:extensions :requirements]
           :web-accessible-resources [:extensions :web-accessible-resources]
           :update-url [:extensions :update-url :opera]
           :sandbox [:extensions :sandbox]})
         {:pretty true})))

;; TODO: make sure that plist output order is not imporant
(defn- safari-config
  "Creates the safari config file using config.edn"
  []
  (io/make-parents (str "resources/extension/safari/" u/project-name "/Info.plist"))
  (let [doctype "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"]
    (spit (str "resources/extension/safari/" u/project-name "/Info.plist")
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
                :Chrome!Popovers [:extensions :extra :safari :popovers]
                :Chrome!Global-.-Page [:extensions :extra :safari :global-page]
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

(defn get-image
  "Returns the icons or splashes for some platform."
  [type plat]
  (into [:platform {:name plat}]
    (if (= type :icon)
      (map #(conj [:icon] %)
        (get-config [:mobile :icons (keyword plat)]))
      (map #(conj [:splash] %)
        (get-config [:mobile :splash (keyword plat)])))))

;; FIXME: make each option output nothing if nil
(defn- mobile-config
  "Outputs the mobile config file from the config.edn data"
  [prefs]
  (let [loc (str "resources/mobile/" u/project-name "/config.xml")]
    (io/make-parents loc)
    (spit loc
      (xml/indent-str
        (xml/sexp-as-element
          (-> [:widget {:id (get-config [:mobile :id])
                        :version (get-config [:version])
                        :xmlns "http://www.w3.org/ns/widgets"
                        :xmlns:cdv "http://cordova.apache.org/ns/1.0"}
               [:name (get-config [:name])]
               [:description (get-config [:description])]
               [:author {:email (get-config [:author :email])
                         :href (get-config [:author :url])}
                (get-config [:author :author-name])]
               [:content {:src (get-config [:mobile :content])}]
               [:icon (get-config [:mobile :icons :global])]]
            (into (map #(conj [:access] {:origin %})
                    (get-config [:mobile :permissions])))
            (into
              (map (fn [[name val]]
                     [:preference {:name (subs (str name) 1) :value val}])
                (get-config [:mobile :preferences])))
            (into (map #(get-image :icon %) ["amazon-fire" "android"
                                             "blackberry" "ios" "tizen" "wp8"]))
            (into (map #(get-image :splash %) ["amazon-fire" "android"
                                               "blackberry" "ios" "wp8"]))))))))


(defn firefoxos-config
  "Creates the manifest.webapp for firefoxos"
  []
  (io/make-parents (str "resources/mobile/" u/project-name
                     "/merges/firefoxos/manifest.webapp"))
  (spit (str "resources/mobile/" u/project-name
          "/merges/firefoxos/manifest.webapp")
    (js/generate-string
      (config-reader
        {:name [:name]
         :description [:description]
         :type [:mobile :firefoxos :type]
         :launch-path [:mobile :content]
         :icons [:mobile :firefoxos :icons]
         :developer!name [:author :author-name]
         :developer!url [:author :url]
         :default-locale [:default-locale]
         :activities [:mobile :firefoxos :activities]
         :appcache-path [:mobile :firefoxos :appcache]
         :chrome [:mobile :firefoxos :chrome]
         :fullscreen [:mobile :firefoxos :fullscreen]
         :installs-allowed-from [:mobile :firefoxos :installs-allowed-from]
         :locales [:mobile :firefoxos :locales]
         :messages [:mobile :firefoxos :messages]
         :orientation [:mobile :firefoxos :orientation]
         :origin [:mobile :firefoxos :origin]
         :permissions [:mobile :firefoxos :permissions]
         :precompile [:mobile :firefoxos :precompile]
         :redirects [:mobile :firefoxos :redirects]
         :role [:mobile :firefoxos :role]
         :version [:version]})
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
           :user!%name [:desktop :user-agent :name]
           :user!%ver [:desktop :user-agent :ver]
           :user!%nwver [:desktop :user-agent :nwver]
           :user!%webkit_ver [:desktop :user-agent :webkit-ver]
           :user!%osinfo [:desktop :user-agent :osinfo]
           :node-remote [:desktop :permissions]
           :chromium-args [:desktop :chromium-args]
           :js-flags [:desktop :js-flags]
           :inject-js-start [:desktop :inject-js-start]
           :inject-js-end [:desktop :inject-js-end]
           :snapshot [:desktop :snapshot]
           :dom_storage_quota [:desktop :dom-storage-quota]
           :keywords [:desktop :keywords]
           :bugs [:desktop :bugs]
           :repositories [:desktop :repositories]
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
           :webkit!plugin [:desktop :webkit :plugin]
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
    (doseq [vendor mobile]
      (cond
        (= vendor "firefoxos") (firefoxos-config)
        :else
        (mobile-config vendor)))
    (doseq [vendor desktops]
      (cond
        (= vendor "linux32" ) (desktop-config "linux32")
        (= vendor "linux64" ) (desktop-config "linux64")
        (= vendor "osx32") (desktop-config "osx32")
        (= vendor "osx64") (desktop-config "osx64")
        (= vendor "windows32") (desktop-config "windows32")
        (= vendor "windows64") (desktop-config "windows64")))))
