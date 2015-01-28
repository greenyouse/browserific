(ns browserific.config.pages.browser.chrome
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn chrome-page
  "The page for Chrome options."
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :chrome-ui-overrides :bookmark-ui :remove-button] config-db) :label "Remove Bookmark Button?"
      :htxt "Removes  the \"star\" button that is used to bookmark pages."
      :hurl "https://developer.chrome.com/extensions/ui_override"}
     {:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :chrome-ui-overrides :bookmark-ui :remove-bookmark-shortuct]config-db) :label "Remove Bookmark Shortcut?"
      :htxt "Removes the shortcut key that is used to bookmark a page (Ctrl-D on Windows)."
      :hurl "https://developer.chrome.com/extensions/ui_override"}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :homepage] config-db) :label "Override Homepage"
      :htxt "New value for the homepage. "
      :hurl "https://developer.chrome.com/extensions/settings_override#homepage"}
     {:type :strings :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :startup-pages] config-db) :label "Override Startup Pages"
      :htxt " An array of length one containing a URL to be used as the startup page. WARNING: Only enter one string, multiple entries will cause errors!"
      :hurl "https://developer.chrome.com/extensions/settings_override#startup_pages"}
     {:label "Override Search Provider"
      :htxt "Overrides the default search provider with new settings."
      :hurl "https://developer.chrome.com/extensions/settings_override#search_provider"
      :prefs
      [{:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :name] config-db) :label "Search Provider Name"
        :help " Name of the search engine displayed to user. This may only be ommitted if prepopulated_id is set."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :keyword] config-db) :label "Search Keyword"
        :help "Omnibox keyword for the search engine. This may only be ommitted if prepopulated_id is set."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :favicon-url] config-db) :label "Search Favicon"
        :help "An icon URL for the search engine. This may only be ommitted if prepopulated_id is set."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :search-url] config-db) :label "Search URL"
        :help "An search URL used by the search engine."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :encoding] config-db) :label "Search Encoding"
        :help "Encoding of the search term. This may only be ommitted if prepopulated_id is set."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :suggest-url] config-db) :label "Search Suggest URL"
        :help "If omitted, this engine does not support suggestions."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :instant-url] config-db) :label "Search Instant"
        :help "If omitted, this engine does not support instant."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :image-url] config-db) :label "Search Image URL"
        :help "If omitted, this engine does not support image search."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :search-url-post-params] config-db) :label "Search URL POST Params"
        :help "The string of post parameters to search_url"}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :suggest-url-post-params] config-db) :label "Suggest URL POST Params"
        :help "The string of post parameters to suggest_url"}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :instant-url-post-params] config-db) :label "Instant URL POST Params"
        :help "The string of post parameters to instant_url"}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :image-url-post-params] config-db) :label "Image URL POST Params"
        :help "The string of post parameters to image_url "}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :alternate-urls] config-db) :label "Alternate URLs"
        :help "A list of URL patterns that can be used, in addition to |search_url|."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :encoding] config-db) :label "Encoding"
        :help "Encoding of the search term. This may only be ommitted if prepopulated_id is set."}
       {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :prepopulated-id] config-db) :label "Prepopulated ID"
        :help "An ID of the built-in search engine in Chrome."
        :number true}
       {:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :chrome-settings-overrides :search-provider :is-default] config-db) :label "Is Default?"
        :help "Specifies if the search provider should be default."}]}
     ;; TODO: get these overrides!
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome :chrome-url-overrides ] config-db) :label "Chrome URL Overrides"
          :htxt "NOT COMPLETE"
          :hurl ""}
     ;; TODO: enter commands with name, description, and shortcut (suggest-key)
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome :commands] config-db) :label "Commands"
          :htxt "NOT COMPLETE"
          :hurl ""}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :current-locale] config-db) :label "Current Locale"
      :htxt "The locale specified."
      :hurl "https://developer.chrome.com/extensions/manifest"}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :devtools-page] config-db) :label "DevTools Page"
      :htxt "An instance of the extension's DevTools page is created each time a DevTools window opens. The DevTools page exists for the lifetime of the DevTools window. The DevTools page has access to the DevTools APIs and a limited set of extension APIs. Specifically, the DevTools page can:"
      :hurl "https://developer.chrome.com/extensions/devtools"}
     {:label "Externally Connectable"
      :htxt " The externally_connectable manifest property declares which extensions, apps, and web pages can connect to your extension via runtime.connect and runtime.sendMessage. "
      :hurl "https://developer.chrome.com/extensions/manifest/externally_connectable"
      :prefs
      [{:type :strings :data (reagent/cursor [:extensions :extra :chrome :externally-connectable :ids] config-db) :label "IDs"
        :help "List of extension and app IDs that are authorized to connect."}
       {:type :strings :data (reagent/cursor [:extensions :extra :chrome :externally-connectable :matches] config-db) :label "Matches"
        :help "Allows the specified webpages to connect."}
       {:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :externally-connectable :accepts-tls-channel-id] config-db) :label "Accept TLS?"
        :help "Indicates that the extension would like to make use of the TLS channel ID of the web page connecting to it. The web page must also opt to send the TLS channel ID to the extension via setting includeTlsChannelId to true in runtime.connect's connectInfo or runtime.sendMessage's options."}]}
     ;; TODO: file-browser-handlers (only for ChromeOS)
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome :file-browser-handlers] config-db) :label "File Browser Handlers"
          :htxt "NOT COMPLETE"
          :hurl ""}
     ;; TODO: figure out what import does (maybe it's something with importing bookmarks?)
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :import] config-db) :label "Import"}
     ;; TODO: implement input components
     #_  {:label "Input Components"
          :htxt "NOT COMPLETE"
          :hurl "http://dev.chromium.org/developers/design-documents/extensions/proposed-changes/apis-under-development/input-method-editor"}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :minimum-chrome-version] config-db) :label "Minimum Chrome Version"
      :htxt "The version of Chrome that your extension, app, or theme requires, if any. The format for this string is the same as for the version field."
      :hurl "https://developer.chrome.com/extensions/manifest/minimum_chrome_version"}
     ;; TODO: finish NACL modules
     {:label "OAuth2"
      :htxt "Use OAuth2 for interacting with official Google APIs."
      :hurl "https://developer.chrome.com/extensions/app_identity"
      :prefs
      [{:type :name :data (reagent/cursor [:extensions :extra :chrome :oauth2 :client-id] config-db) :label "OAuth2 Client ID"
        :help "A Client ID that was previously registered through the Google API Console (https://code.google.com/apis/console/)"}
       {:type :strings :data (reagent/cursor [:extensions :extra :chrome :oauth2 :scopes] config-db) :label "OAuth2 Scopes"
        :help "URLs of Google services that your Client ID is authorized to use."}]}
     {:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :offline-enabled] config-db) :label "Offline Enabled?"
      :htxt "Whether the app or extension is expected to work offline. When Chrome detects that it is offline, apps with this field set to true will be highlighted on the New Tab page."
      :hurl "https://developer.chrome.com/extensions/manifest/offline_enabled"
      :default true}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :omnibox :keyword] config-db) :label "Omnibox Keyword"
      :htxt "When the user enters your extension's keyword, the user starts interacting solely with your extension. Each keystroke is sent to your extension, and you can provide suggestions in response."
      :hurl "https://developer.chrome.com/extensions/omnibox"}
     {:type :strings :data (reagent/cursor [:extensions :extra :chrome :optional-permissions] config-db) :label "Optional Permissions"
      :htxt "Optional access URLs for optional features in your extension."
      :hurl "https://developer.chrome.com/extensions/permissions"}
     ;; TODO: need info for :platforms
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome :platforms] config-db) :label "Platforms"
          :htxt "NOT COMPLETE"
          :hurl ""}
     ;; TODO: find what :script-badge does
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome :script-badge] config-db) :label "Script Badge"
          :htxt "NOT COMPLETE"
          :hurl ""}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :short-name] config-db) :label "Short Name"
      :htxt "The short_name (maximum of 12 characters recommended) is a short version of the extension's name. It is an optional field and if not specified, the name will be used, though it will likely be truncated. The short name is typically used where there is insufficient space to display the full name."
      :hurl "https://developer.chrome.com/extensions/manifest/name#short_name"}
     {:type :name :data (reagent/cursor [:extensions :extra :chrome :storage :managed-schema] config-db) :label "Managed Schema"
      :htxt "The storage.managed_schema property indicates a file within the extension that contains the policy schema."
      :hurl "https://developer.chrome.com/extensions/manifest/storage"}
     ;; TODO: get this too
     #_  {:label "Text-to-Speech Engine"
          :htxt "Use the chrome.ttsEngine API to implement a text-to-speech(TTS) engine using an extension. If your extension registers using this API, it will receive events containing an utterance to be spoken and other parameters when any extension or Chrome App uses the tts API to generate speech. Your extension can then use any available web technology to synthesize and output the speech, and send events back to the calling function to report the status. "
          :hurl "https://developer.chrome.com/extensions/ttsEngine"
          :prefs
          [{:type :name :data (reagent/cursor [:extensions :extra :chrome :tts-engine] config-db) :label ""
            :help ""}
           {:type :name :data (reagent/cursor [:extensions :extra :chrome :tts-engine] config-db) :label ""
            :help ""}
           {:type :name :data (reagent/cursor [:extensions :extra :chrome :tts-engine] config-db) :label ""
            :help ""}
           {:type :strings :data (reagent/cursor [:extensions :extra :chrome :tts-engine] config-db) :label ""
            :help ""}]}
     ;; TODO: find out what all of these do and how to implement them
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome ] config-db) :label "Signature"
          :htxt ""
          :hurl ""}
     #_  {:type :checkbox :data (reagent/cursor [:extensions :extra :chrome :spellcheck] config-db) :label "Spellcheck"}
     #_  {:type :name :data (reagent/cursor [:extensions :extra :chrome ] config-db) :label "System Indicator"
          :htxt ""
          :hurl ""}]))
