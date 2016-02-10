(ns browserific.config
  "Generates config files from config.edn"
  (:require [browserific.helpers.plist :as p]
            [browserific.helpers.utils :as u]
            [cheshire.core :as js]
            [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as st]
            [deepfns.core :refer [<=>]]
            [deepfns.transitive :as t]
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
 amazon-fireos, android, blackberry, firefoxos, ios, ubuntu, wp7, wp8, tizen")))))

(defn- with-config
  "Run some function f over the config file"
  [f]
  (let [conf (u/get-config)]
    (f conf)))

(defn- to-js-config
  "Uses a transitive for the configuration file called config-t,
  applies the trasitives, and generates JSON string of the results"
  [config-t]
  (js/generate-string
    (with-config config-t)
    {:pretty true}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config Transitives

(def chrome-opera-configs
  "Shared configs for Chrome and Opera"
  {:name :name
   :version :version
   :manifest_version 2
   :description :description
   :developer {:name (t/=> :author :author-name)
               :url (t/=> :author :url)}
   :default_locale :default-locale
   :action {:type (t/=> :extensions :action :type)
            ;; FIXME default-icon
            :default_icon (t/=> :extensions :action :default-icon)
            :default_title (t/=> :extensions :action :default-title)
            :default_popup (t/=> :extensions :action :default-popup)}
   :background {:scripts (t/=> :extensions :background :scripts)
                :persistent (t/=> :extensions :background :persistent)}
   :content_scripts {:matches (t/=> :extensions :content :matches)
                     :exclude (t/=> :extensions :content :exclude)
                     :js (t/=> :extensions :content :js)
                     :css (t/=> :extensions :content :css)}
   :homepage (t/=> :extensions :homepage)
   :incognito (t/=> :extensions :incognito)
   :options_page (t/=> :extensions :options-page)
   :permissions (t/=> :extensions :permissions)
   :requirements (t/=> :extensions :requirements)
   :web_accessible-resources (t/=> :extensions :web-accessible-resources)
   :sandbox (t/=> :extensions :sandbox)})

(def chrome-config>
  (<=>
    (assoc chrome-opera-configs
      :icons (t/=> :extensions :icons :chrome)
      :update_url (t/=> :extensions :update-url :chrome)
      :chrome_ui_overrides (t/=> :extensions :extra :chrome :chrome-ui-overrides)
      :chrome_settings_overrides (t/=> :extensions :extra :chrome :chrome-settings-overrides)
      :chrome_url_overrides (t/=> :extensions :extra :chrome :chrome-url-overrides)
      :commands (t/=> :extensions :extra :chrome :commands)
      :current_locale (t/=> :extensions :extra :chrome :current-locale)
      :devtools_page (t/=> :extensions :extra :chrome :devtools_page)
      :externally_connectable (t/=> :extensions :extra :chrome :externally-connectable)
      :file_browser_handlers (t/=> :extensions :extra :chrome :file-browser-handlers)
      :import (t/=> :extensions :extra :chrome :import)
      :input_components (t/=> :extensions :extra :chrome :input-components)
      :minimum_chrome_version (t/=> :extensions :extra :chrome :minimum-chrome-version)
      :nacl_modules (t/=> :extensions :extra :chrome :nacl-modules)
      :oauth2 (t/=> :extensions :extra :chrome :oauth2)
      :offline_enabled (t/=> :extensions :extra :chrome :offline-enabled)
      :omnibox (t/=> :extensions :extra :chrome :omnibox)
      :optional_permissions (t/=> :extensions :extra :chrome :optional-permissions)
      :platforms (t/=> :extensions :extra :chrome :platforms)
      :script_badge (t/=> :extensions :extra :chrome :script-badge)
      :short_name (t/=> :extensions :extra :chrome :short-name)
      :signature (t/=> :extensions :extra :chrome :signature)
      :spellcheck (t/=> :extensions :extra :chrome :spellcheck)
      :storage (t/=> :extensions :extra :chrome :storage)
      :system_indicator (t/=> :extensions :extra :chrome :system-indicator)
      :tts_engine (t/=> :extensions :extra :chrome :tts-engine))))

(def opera-config>
  (<=>  (assoc chrome-opera-configs
          :icons (t/=> :extensions :icons :opera)
          :update_url (t/=> :extensions :update-url :opera))))

(def firefox-config>
  (<=>
    {:author (t/=> :author :author-name)
     :contributors (t/=> :author :contributors)
     :dependencies (t/=> :extensions :extra :firefox :dependencies)
     :fullName :name
     :homepage (t/=> :extensions :homepage)
     :icon (t/=> :extensions :icons :firefox :48)
     :icon64 (t/=> :extensions :icons :firefox :64)
     :id (t/=> :author :author-id)
     :lib (t/=> :extensions :extra :firefox :lib)
     :license :license
     :main (t/=> :extensions :extra :firefox :main)
     :name (t/=> :extensions :extra :firefox :name)
     :packages (t/=> :extensions :extra :firefox :packages)
     :permissions {:private-browsing (t/=> :extensions :private)
                   :cross-domain-content (t/=> :extensions :extra :firefox :permissions :cross-domain-content)}
     :preferences (t/=> :extensions :extra :firefox :preferences)
     :tests (t/=> :extensions :extra :firefox :tests)
     :title (t/=> :extensions :extra :firefox :title)
     :translators (t/=> :extensions :extra :firefox :translators)
     :version :version}))

(def safari-config>
  (<=>
    {:Author (t/=> :author :author-name)
     :Builder-.-Version "6534.59.10" ; Shouldn't matter
     :CFBundleDisplayName :name
     :CFBundleIdentifier (t/=> :author :author-id)
     :CFBundleInfoDictionaryVersion "6.0" ; Shouldn't matter
     :CFBundleShortVersionString :version
     :CFBundleVersion "1.0"; Shouldn't matter
     :Chrome {:Bars (t/=> :extensions :extra :safari :bars)
              :Context-.-Menu-.-Items (t/=> :extensions :extra :safari :context-items)
              :Database-.-Quota (t/=> :extensions :extra :safari :database-quota)
              :Popovers (t/=> :extensions :extra :safari :popovers)
              :Global-.-Page (t/=> :extensions :extra :safari :global-page)
              ;; TODO: there should be a better handling for this
              :Menus (t/=> :extensions :extra :safari :menus
                       (fn [menus]
                         (mapv
                           (<=> {:Identifier :Identifier
                                 :Menu-.-Items {:Identifier :Menu-Identifier
                                                :Command :Command
                                                :Disabled :Disabled
                                                :Title :Title}})
                           menus)))}
     :Content {:Blacklist (t/=> :extensions :content :exclude)
               :Scripts {:Start (t/=> :extensions :extra :safari :start-script)
                         :End (t/=> :extensions :content :js)}
               :Content {:Stylesheets (t/=> :extensions :content :css)
                         :Whitelist (t/=> :extensions :content :matches)}}
     :Description :description
     :ExtensionInfoDictionaryVersion "1.0" ; Shouldn't matter
     ;; FIXME: are options like "tabs" just negligeable?
     :Permissions {:Website-.-Access
                   {:Allowed-.-Domains (t/=> :extensions :permissions)
                    :Include-.-Secure-.-Pages (t/=> :extensions :private)
                    :Level (t/=> :extensions :extra :safari :access-level)}}
     :Update-.-Manifest-.-URL (t/=> :extensions :update-url :safari)
     :Website (t/=> :extensions :homepage)}))

;; FIXME: make each option output nothing if nil
(def mobile-config>
  (<=>
    [(constantly :widget)
     {:id (t/=> :mobile :id)
      :version :version
      :xmlns "http://www.w3.org/ns/widgets"
      :xmlns:cdv "http://cordova.apache.org/ns/1.0"}
     [(constantly :name) :name]
     [(constantly :description) :description]
     [(constantly :author) {:email (t/=> :author :email)
                            :href (t/=> :author :url)}
      (t/=> :author :author-name)]
     [(constantly :content) {:src (t/=> :mobile :content)}]
     [(constantly :icon) (t/=> :mobile :icons :global)]
     [(constantly :description) :description]
     [(constantly :description) :description]
     (t/map> #(conj [:access] {:origin %})
       (t/=> :mobile :permissions))
     (t/map> (fn [[name val]]
               [:preference {:name (subs (str name) 1) :value val}])
       (t/=> :mobile :preferences))
     (t/map> (fn [[name icns]]
               [:platform {:name name} icns])
       (t/=> :mobile :icons))
     (t/map> (fn [[name icns]]
               [:platform {:name name} icns])
       (t/=> :mobile :splash))]))

(def firefoxos-config>
  (<=> {:name :name
        :description :description
        :type (t/=> :mobile :firefoxos :type)
        :launch_path (t/=> :mobile :content)
        :icons (t/=> :mobile :firefoxos :icons)
        :developer {:name (t/=> :author :author-name)
                    :url (t/=> :author :url)}
        :default_locale (t/=> :default-locale)
        :activities (t/=> :mobile :firefoxos :activities)
        :appcache_path (t/=> :mobile :firefoxos :appcache)
        :chrome (t/=> :mobile :firefoxos :chrome)
        :fullscreen (t/=> :mobile :firefoxos :fullscreen)
        :installs_allowed_from (t/=> :mobile :firefoxos :installs-allowed-from)
        :locales (t/=> :mobile :firefoxos :locales)
        :messages (t/=> :mobile :firefoxos :messages)
        :orientation (t/=> :mobile :firefoxos :orientation)
        :origin (t/=> :mobile :firefoxos :origin)
        :permissions (t/=> :mobile :firefoxos :permissions)
        :precompile (t/=> :mobile :firefoxos :precompile)
        :redirects (t/=> :mobile :firefoxos :redirects)
        :role (t/=> :mobile :firefoxos :role)
        :version :version}))

(def desktop-config>
  (<=> {:name :name
        :main (t/=> :desktop :main)
        :nodejs (t/=> :desktop :nodejs)
        :node-main (t/=> :desktop :node-main)
        :user {:%name (t/=> :desktop :user-agent :name)
               :%ver (t/=> :desktop :user-agent :ver)
               :%nwver (t/=> :desktop :user-agent :nwver)
               :%webkit_ver (t/=> :desktop :user-agent :webkit-ver)
               :%osinfo (t/=> :desktop :user-agent :osinfo)}
        :node-remote (t/=> :desktop :permissions)
        :chromium-args (t/=> :desktop :chromium-args)
        :js-flags (t/=> :desktop :js-flags)
        :inject-js-start (t/=> :desktop :inject-js-start)
        :inject-js-end (t/=> :desktop :inject-js-end)
        :snapshot (t/=> :desktop :snapshot)
        :dom_storage_quota (t/=> :desktop :dom-storage-quota)
        :keywords (t/=> :desktop :keywords)
        :bugs (t/=> :desktop :bugs)
        :repositories (t/=> :desktop :repositories)
        :window {:title (t/=> :desktop :window :title)
                 :width (t/=> :desktop :window :width)
                 :height (t/=> :desktop :window :height)
                 :toolbar (t/=> :desktop :window :toolbar)
                 :icon (t/=> :desktop :window :icon)
                 :position (t/=> :desktop :window :position)
                 :min_width (t/=> :desktop :window :min-width)
                 :min_height (t/=> :desktop :window :min-height)
                 :max_width (t/=> :desktop :window :max-width)
                 :max_height (t/=> :desktop :window :max-height)
                 :as_desktop (t/=> :desktop :window :as-desktop)
                 :resizable (t/=> :desktop :window :resizable)
                 :always_on_top (t/=> :desktop :window :always-on-top)
                 :fullscreen (t/=> :desktop :window :fullscreen)
                 :show_in_taskbar (t/=> :desktop :window :show-in-taskbar)
                 :frame (t/=> :desktop :window :frame)
                 :show (t/=> :desktop :window :show)
                 :kiosk (t/=> :desktop :window :kiosk)}
        :webkit {:plugin (t/=> :desktop :webkit :plugin)
                 :java (t/=> :desktop :webkit :java)
                 :page-cache (t/=> :desktop :webkit :page-cache)}}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Config Builders

;;; Browser Configs

;; NOTE: :key isn't really necessary so we'll leave it out for now (chrome + opera)
;;   and we're omitting :plugins because that is being phased out
(defn- chrome-config
  "Creates the chrome extension config file using config.edn"
  []
  (io/make-parents (str "resources/extension/chrome/" u/project-name "/manifest.json"))
  (spit (str "resources/extension/chrome/" u/project-name "/manifest.json")
    (to-js-config chrome-config>)))

(defn- firefox-config
  "Creates the firefox config file using config.edn"
  []
  (io/make-parents (str "resources/extension/firefox/" u/project-name "/package.json"))
  (spit (str "resources/extension/firefox/" u/project-name "/package.json")
        (to-js-config firefox-config>)))

(defn- opera-config
  "Creates the opera config file using config.edn"
  []
  (io/make-parents (str "resources/extension/opera/" u/project-name "/manifest.json"))
  (spit (str "resources/extension/opera/" u/project-name "/manifest.json")
        (to-js-config )))

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
              (safari-config> (u/get-config)))))
        "?>" (str "?>\n" doctype "\n")))))


;;; Mobile Configs

(defn- mobile-config
  "Outputs the mobile config file from the config.edn data"
  []
  (let [loc (str "resources/mobile/" u/project-name "/config.xml")]
    (io/make-parents loc)
    (spit loc
      (xml/indent-str
        (xml/sexp-as-element
          (mobile-config> (u/get-config)))))))


(defn- firefoxos-config
  "Creates the manifest.webapp for firefoxos"
  []
  (io/make-parents (str "resources/mobile/" u/project-name
                     "/merges/firefoxos/manifest.webapp"))
  (spit (str "resources/mobile/" u/project-name
          "/merges/firefoxos/manifest.webapp")
    (to-js-config firefoxos-config>)))


;;; Desktop Configs

(defn- desktop-config
  "Generates the node-webkit config file using config.edn"
  [platform]
  (io/make-parents (str "resources/desktop/deploy/" platform "/package.json"))
  (spit (str "resources/desktop/deploy/" platform "/package.json")
        (to-js-config desktop-config>)))


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
       (= vendor "firefoxos") (do (mobile-config)
                                  (firefoxos-config))
       :else
        (mobile-config)))
    (doseq [vendor desktops]
      (cond
        (= vendor "linux32" ) (desktop-config "linux32")
        (= vendor "linux64" ) (desktop-config "linux64")
        (= vendor "osx32") (desktop-config "osx32")
        (= vendor "osx64") (desktop-config "osx64")
        (= vendor "windows32") (desktop-config "windows32")
        (= vendor "windows64") (desktop-config "windows64")))))
