(ns browserific.config.pages.browser
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.trans :as t]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(def ^:private icon-c
  (multi-input-template :vec [{:type :name :label "src"}
                              {:type :name :label "size"}]))

(defn browser-page
  "The page for browser options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :checkbox-list :data (reagent/cursor [:extensions :platforms] config-db) :label "Platforms"
      :htxt "The platforms to build browser extensions on."
      :boxes ["firefox" "chrome" "opera" "safari"]}
     {:type :checkbox :data (reagent/cursor [:extensions :private] config-db) :label "Private?"
      :htxt "Whether to support private browsering mode (for Firefox + Safari)."
      :hurl "https://developer.mozilla.org/en-US/Add-ons/SDK/High-Level_APIs/private-browsing"}
     {:type :select :data (reagent/cursor [:extensions :action :type] config-db) :label "Action Type"
      :htxt "Browser actions allow for putting stuff in the browser chrome (like adding icons to a toolbar) while Page actions only interact with a webpage (for DOM interaction). Only for Chrome + Opera."
      :hurl ["https://dev.opera.com/extensions/browserAction.html" "https://dev.opera.com/extensions/pageAction.html"]
      :options ["browser" "page"]}
     {:type :multi :data (reagent/cursor [:extensions :action :default-icon] config-db) :label "Action Icons"
      :htxt "Relative path to a icon PNG file for the action. You're encouraged to use both 19px and 38px icons but only one is required. Only for Chrome + Opera."
      :hurl ["https://developer.chrome.com/extensions/browserAction#icon" "https://developer.chrome.com/extensions/pageAction"]
      :multi-c icon-c}
     {:type :name :data (reagent/cursor [:extensions :action :default-title] config-db) :label "Action Title"
      :htxt "A title for the action that will be shown in a tooltip. Only for Chrome + Opera."
      :hurl "https://developer.chrome.com/extensions/browserAction#tooltip"}
     {:type :name :data (reagent/cursor [:extensions :action :default-popup] config-db) :label "Action Popup"
      :htxt "If a browser action has a popup, the popup appears when the user clicks the icon. The popup can contain any HTML contents that you like, and it's automatically sized to fit its contents. Only for Chrome + Opera."
      :hurl "https://developer.chrome.com/extensions/browserAction#popups"}
     {:type :select :data (reagent/cursor [:extensions :background :scripts] config-db) :label "Background Scripts"
      :htxt "Whether to use a background page or an event page. Only for Chrome + Opera."
      :hurl ["https://developer.chrome.com/extensions/background_pages" "https://developer.chrome.com/extensions/event_pages"]
      :options ["eventPage.js" "background.js"]}
     {:type :checkbox :data (reagent/cursor [:extensions :background :persistent] config-db) :label "Persistent Background?"
      :htxt "Set this to true if using a background page and false for an event page. Only for Chrome + Opera."
      :hurl ["https://developer.chrome.com/extensions/background_pages" "https://developer.chrome.com/extensions/event_pages"]}
     {:type :strings :data (reagent/cursor [:extensions :content :matches] config-db) :label "Content Whitelist"
      :htxt "Specifies which pages a content script will be injected into."
      :hurl ["https://developer.chrome.com/extensions/content_scripts" "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/ExtensionPermissions/ExtensionPermissions.html#//apple_ref/doc/uid/TP40009977-CH8-SW7"]}
     {:type :strings :data (reagent/cursor [:extensions :content :exclude] config-db) :label "Content Blacklist"
      :htxt "Excludes pages that a content script would otherwise be injected into."
      :hurl ["https://developer.chrome.com/extensions/content_scripts" "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/ExtensionPermissions/ExtensionPermissions.html#//apple_ref/doc/uid/TP40009977-CH8-SW7"]}
     {:type :strings :data (reagent/cursor [:extensions :content :css] config-db) :label "Content CSS"
      :htxt " The list of CSS files to be injected into matching pages. These are injected in the order they appear in this array, before any DOM is constructed or displayed for the page."
      :hurl ["https://developer.chrome.com/extensions/content_scripts" "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/AccessingResourcesWithinYourExtensionFolder/AccessingResourcesWithinYourExtensionFolder.html#//apple_ref/doc/uid/TP40009977-CH18-SW2"]}
     {:type :strings :data (reagent/cursor [:extensions :content :js] config-db) :label "Content JS"
      :htxt "The list of JavaScript files to be injected into matching pages. These are injected in the order they appear in this array."
      :hurl ["https://developer.chrome.com/extensions/content_scripts" "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/ExtensionPermissions/ExtensionPermissions.html#//apple_ref/doc/uid/TP40009977-CH8-SW1"]}
     {:type :multi :data (reagent/cursor [:extensions :icons :chrome] config-db) :label "Chrome Icon"
      :htxt "Icons for Chrome to use. Suggested sizes are 16px, 48px, and 128px (128 is required)."
      :hurl "https://developer.chrome.com/extensions/manifest/icons"
      :multi-c icon-c}
     {:type :multi :data (reagent/cursor [:extensions :icons :firefox] config-db) :label "Firefox Icon"
      :htxt "Icons for Firefox. Suggested sizes are 32px, 48px, and 64px."
      :hurl "https://developer.mozilla.org/en-US/Add-ons/Install_Manifests#iconURL"
      :multi-c icon-c}
     {:type :multi :data (reagent/cursor [:extensions :icons :opera] config-db) :label "Opera Icon"
      :htxt "Opera icons. Suggested sizes are 16px, 48px, and 128px (128 is required)."
      :hurl "https://dev.opera.com/extensions/manifest.html#icons"
      :multi-c icon-c}
     {:type :multi :data (reagent/cursor [:extensions :icons :safari] config-db) :label "Safari Icon"
      :htxt "Safari icons. Suggested sizes are 32px, 48px, and 64px."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/UsingExtensionBuilder/UsingExtensionBuilder.html#//apple_ref/doc/uid/TP40009977-CH2-SW1"
      :multi-c icon-c}
     {:type :name :data (reagent/cursor [:extensions :hompage] config-db) :label "Homepage"
      :htxt "A link to the add-on's home page - intended for display in the user interface."
      :hurl "https://developer.mozilla.org/en-US/Add-ons/Install_Manifests#homepageURL"}
     {:type :name :data (reagent/cursor [:extensions :options-page] config-db) :label "Options Page"
      :htxt "To allow users to customize the behavior of your extension, you may wish to provide an options page. Only for Chrome + Opera."
      :hurl "https://developer.chrome.com/extensions/options"}
     {:type :strings :data (reagent/cursor [:extensions :permissions] config-db) :label "Permissions"
      :htxt "To use most chrome.* APIs, your extension or app must declare its intent in the \"permissions\" field of the manifest. Each permission can be either one of a list of known strings (such as \"geolocation\") or a match pattern that gives access to one or more hosts."
      :hurl "https://developer.chrome.com/extensions/declare_permissions"}
     {:type :name :data (reagent/cursor [:extensions :update-url :chrome] config-db) :label "Chrome Update URL"
      :htxt "Only applies to extensions not using the Chrome Web Store. Will provide an update to your extension through the given URL."
      :hurl "https://developer.chrome.com/extensions/autoupdate"}
     {:type :name :data (reagent/cursor [:extensions :update-url :opera] config-db) :label "Opera Update URL"
      :htxt "The URL for updating with Opera."
      :hurl "https://dev.opera.com/extensions/manifest.html"}
     ;; NOTE: Firefox has one but it's in the install.rdf file and we're currently
     ;; ouputting package.json. Not necessary if using AMO
     {:type :name :data (reagent/cursor [:extensions :update-url :safari] config-db) :label "Safari Update URL"
      :htxt "Updates your safari extensions with the given URL."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/UpdatingExtensions/UpdatingExtensions.html#//apple_ref/doc/uid/TP40009977-CH12-SW1"}
     {:type :strings :data (reagent/cursor [:extensions :web-accessible-resources] config-db) :label "Web Accessible Resources"
      :htxt "An array of strings specifying the paths of packaged resources that are expected to be usable in the context of a web page. These paths are relative to the package root, and may contain wildcards."
      :hurl "https://developer.chrome.com/extensions/manifest/web_accessible_resources"}
     {:type :strings :data (reagent/cursor [:extensions :requirements :3D :features] config-db) :label "3D Features"
      :htxt " Technologies required by the app or extension. Hosting sites such as the Chrome Web Store may use this list to dissuade users from installing apps or extensions that will not work on their computer. Supported requirements currently include \"3D\" and \"plugins\"; additional requirements checks may be added in the future. "
      :hurl "https://developer.chrome.com/extensions/manifest/requirements"}
     {:type :strings :data (reagent/cursor [:extensions :sandbox :page] config-db) :label "Sandbox Pages"
      :htxt "Defines an collection of app or extension pages that are to be served in a sandboxed unique origin."
      :hurl "https://developer.chrome.com/extensions/manifest/sandbox"}
     {:type :name :data (reagent/cursor [:extensions :sandbox :content-security-policy] config-db) :label "Content Security Policy"
      :htxt "Enter a Content Security Policy for the sandboxed pages. If none is given, the default is sandbox allow-scripts allow-forms."
      :hurl ["https://developer.chrome.com/extensions/contentSecurityPolicy" "https://developer.chrome.com/extensions/manifest/sandbox"]}]))
