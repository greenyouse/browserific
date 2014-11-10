(ns browserific.config.file
  "The config.edn file is loaded here, passed through
  this Om schema to organize the GUI layout, and then
  served by browserific.config.server"
  (:require [browserific.helpers.utils :refer [get-config]])
  (:import (java.util.UUID)))


(defn uuid [] (java.util.UUID/randomUUID))

;; id, path-id, value, label, dom-type, help-text, help-url, getter, [optional items]
(def test-data
  "This is an OM schema that is used for rendering the configs."
  {:general
   [{:id (uuid) :path-id [:name] :value (get-config [:name]) :label "Name" :dom-type :name :help-text "The name of your app" :help-url nil :getter [:name]}
    {:id (uuid) :path-id [:author :author-name] :value (get-config [:author :author-name]) :label "Author Name" :dom-type :name :help-text "Your name." :help-url nil :getter [:author :author-name]}
    {:id (uuid) :path-id [:author :author-id] :value (get-config [:author :author-id]) :label "Author-ID" :dom-type :name :help-text "Your id or company id." :help-url nil :getter [:author :author-id]}
    {:id (uuid) :path-id [:author :email] :value (get-config [:author :email]) :label "Author's e-mail" :dom-type :name :help-text "Your email address." :help-url nil :getter [:author :email]}
    {:id (uuid) :path-id [:author :url] :value (get-config [:author :url]) :label "Project Website" :dom-type :name :help-text "Your website." :help-url nil :getter [:author :url]}
    {:id (uuid) :path-id [:author :contributors] :value (get-config [:author :contributors]) :label "Contributors" :dom-type :list :help-text "Any contributors on the project." :help-url nil :getter [:author :contributors]}
    {:id (uuid) :path-id [:version] :value (get-config [:version]) :label "Version" :dom-type :name :help-text "The version number of your project." :help-url nil :getter [:version]}
    {:id (uuid) :path-id [:license] :value (get-config [:license]) :label "License" :dom-type :name :help-text "The license on your project." :help-url nil :getter [:license]}
    {:id (uuid) :path-id [:description] :value (get-config [:description]) :label "Description" :dom-type :name :help-text "A short description of your project." :help-url nil :getter [:description]}
    {:id (uuid) :path-id [:default-locale] :value (get-config [:default-locale]) :label "Default Locale" :dom-type :name :help-text "A default locale for your app. \n\nThis can be a two digit language code (like en) or a regional code (like en_US or en_GB)." :help-url "https://developer.chrome.com/extensions/i18n#overview-locales" :getter [:default-locale]}]


   :desktop
   [{:id (uuid) :path-id [:desktop :platforms] :value (get-config [:desktop :platforms]) :label "Desktop Platforms" :dom-type :checkbox-list :help-text "The desktop platforms your app will be published to." :help-url nil :getter [:desktop :platforms] :boxes ["linux" "osx" "windows"]}
    {:id (uuid) :path-id [:desktop :main] :value (get-config [:desktop :main]) :label "Main Desktop Page" :dom-type :name :help-text "The app's starting page." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#main" :getter [:desktop :main]}
    {:id (uuid) :path-id [:desktop :nodejs] :value (get-config [:desktop :nodejs]) :label "NodeJS?" :dom-type :checkbox :help-text "A boolean, setting this to false will disable Node support in WebKit." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#nodejs" :getter [:desktop :nodejs]}
    {:id (uuid) :path-id [:desktop :node-main] :value (get-config [:desktop :node-main]) :label "Node Main" :dom-type :name :help-text "Specifies the path to a node.js script file which gets loaded before the DOM." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#node-main" :getter [:desktop :node-main]}
    {:id (uuid) :path-id [:desktop :single-instance] :value (if (nil? (get-config [:desktop :single-instance])) true (get-config [:desktop :single-instance])) :label "Single Instance?" :dom-type :checkbox :help-text "By default node-webkit only allows one instance of your app if your app is a standalone package, if you want to allow multiple instances of your app running at the same time, make this false." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#single-instance" :getter [:desktop :single-instance]}
    {:id (uuid) :path-id [:desktop :user-agent :name] :value (get-config [:desktop :user-agent :name]) :label "User-Agent Name" :dom-type :name :help-text "Overrides the User-Agent header in HTTP requests made from the application." :help-url "https://en.wikipedia.org/wiki/User_agent_string" :getter [:desktop :user-agent :name]}
    {:id (uuid) :path-id [:desktop :user-agent :ver] :value (get-config [:desktop :user-agent :ver]) :label "User-Agent Version" :dom-type :name :help-text "Overrides the User-Agent header in HTTP requests made from the application." :help-url "https://en.wikipedia.org/wiki/User_agent_string" :getter [:desktop :user-agent :ver]}
    {:id (uuid) :path-id [:desktop :user-agent :nwver] :value (get-config [:desktop :user-agent :nwver]) :label "User-Agent NWVersion" :dom-type :name :help-text "Overrides the User-Agent header in HTTP requests made from the application." :help-url "https://en.wikipedia.org/wiki/User_agent_string" :getter [:desktop :user-agent :nwver]}
    {:id (uuid) :path-id [:desktop :user-agent :webkit-ver] :value (get-config [:desktop :user-agent :webkit-ver]) :label "User-Agent Webkit Version" :dom-type :name :help-text "Overrides the User-Agent header in HTTP requests made from the application." :help-url "https://en.wikipedia.org/wiki/User_agent_string" :getter [:desktop :user-agent :webkit-ver]}
    {:id (uuid) :path-id [:desktop :user-agent :osinfo] :value (get-config [:desktop :user-agent :osinfo]) :label "User-Agent OS Info" :dom-type :name :help-text "Overrides the User-Agent header in HTTP requests made from the application." :help-url "https://en.wikipedia.org/wiki/User_agent_string" :getter [:desktop :user-agent :osinfo]}
    {:id (uuid) :path-id [:desktop :permissions] :value (get-config [:desktop :permissions]) :label "Node Remote Permissions" :dom-type :list :help-text "Whitelist of domains that can connect with your app." :help-url "noe-remote" :getter [:desktop :permissions]}
    {:id (uuid) :path-id [:desktop :chromium-args] :value (get-config [:desktop :chromium-args]) :label "Chromium Args" :dom-type :name :help-text "Chromium command line arguments for custom settings." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#chromium-args" :getter [:desktop :chromium-args]}
    {:id (uuid) :path-id [:desktop :js-flags] :value (get-config [:desktop :js-flags]) :label "JS Flags" :dom-type :name :help-text "Flags to pass to the v8 JavaScript engine" :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#js-flags" :getter [:desktop :js-flags]}
    {:id (uuid) :path-id [:desktop :inject-js-start] :value (get-config [:desktop :inject-js-start]) :label "Inject JS Start" :dom-type :file :help-text "The injected JavaScript code is to be executed after any files from css, but before any other DOM is constructed or any other script is run" :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#inject-js-start-inject-js-end" :getter [:desktop :inject-js-start]}
    {:id (uuid) :path-id [:desktop :inject-js-end] :value (get-config [:desktop :inject-js-end]) :label "Inject JS End" :dom-type :file :help-text "The injecting JavaScript code is to be executed after the document object is loaded, before onload event is fired." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#inject-js-start-inject-js-end" :getter [:desktop :inject-js-end]}
    {:id (uuid) :path-id [:desktop :snapshot] :value (get-config [:desktop :snapshot]) :label "Snapshot (Experimental)" :dom-type :file :help-text "EXPERIMENTAL -- Loads a snapshot file that has been compiled down to native code." :help-url "https://github.com/rogerwang/node-webkit/wiki/Protect-JavaScript-source-code-with-v8-snapshot" :getter [:desktop :snapshot]}
    {:id (uuid) :path-id [:desktop :dom-storage-quota] :value (get-config [:desktop :dom-storage-quota]) :label "DOM Storage Quota" :dom-type :name :help-text "Number of megabytes for the quota of the DOM storage. The suggestion is to put double the value you want." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#dom-storage-quota" :getter [:desktop :dom-storage-quota] :number true}
    {:id (uuid) :path-id [:desktop :keywords] :value (get-config [:desktop :keywords]) :label "Keywords" :dom-type :list :help-text "An array of keywords to assist users searching for the package in catalogs." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#keywords" :getter [:desktop :keywords]}
    {:id (uuid) :path-id [:desktop :bugs] :value (get-config [:desktop :bugs]) :label "Bug URL" :dom-type :name :help-text "URL for submitting bugs. Can be mailto or http." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#bugs" :getter [:desktop :bugs]}
    {:id (uuid) :path-id [:desktop :repositories :type] :value (get-config [:desktop :repositories :type]) :label "Repository Type" :dom-type :name :help-text "Array of repositories where the package can be located. Each repository is a hash with properties for the :type and :url location of the repository to clone/checkout the package. A :path property may also be specified to locate the package in the repository if it does not reside at the root." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#repositories" :getter [:desktop :repositories :type]}
    {:id (uuid) :path-id [:desktop :repositories :url] :value (get-config [:desktop :repositories :url]) :label "Repository URL" :dom-type :name :help-text "Array of repositories where the package can be located. Each repository is a hash with properties for the :type and :url location of the repository to clone/checkout the package. A :path property may also be specified to locate the package in the repository if it does not reside at the root." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#repositories" :getter [:desktop :repositories :url]}
    {:id (uuid) :path-id [:desktop :repositories :path] :value (get-config [:desktop :repositories :path]) :label "Repository Path" :dom-type :name :help-text "Array of repositories where the package can be located. Each repository is a hash with properties for the :type and :url location of the repository to clone/checkout the package. A :path property may also be specified to locate the package in the repository if it does not reside at the root." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#repositories" :getter [:desktop :repositories :path]}
    {:id (uuid) :path-id [:desktop :window :title] :value (get-config [:desktop :window :title]) :label "Window Title" :dom-type :name :help-text "The default title of window created by node-webkit, it's very useful if you want to show your own title when the app is starting." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#title" :getter [:desktop :window :title]}
    {:id (uuid) :path-id [:desktop :window :height] :value (get-config [:desktop :window :height]) :label "Window Height" :dom-type :name :help-text "The initial height of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#widthheight" :getter [:desktop :window :height] :number true}
    {:id (uuid) :path-id [:desktop :window :width] :value (get-config [:desktop :window :width]) :label "Window Width" :dom-type :name :help-text "The initial width of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#widthheight" :getter [:desktop :window :width] :number true}
    {:id (uuid) :path-id [:desktop :window :toolbar] :value (get-config [:desktop :window :toolbar]) :label "Navigation Toolbar?" :dom-type :checkbox :help-text "Should the navigation toolbar be shown?" :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#toolbar" :getter [:desktop :window :toolbar]}
    {:id (uuid) :path-id [:desktop :window :icon] :value (get-config [:desktop :window :icon]) :label "Icon" :dom-type :name :help-text "Path to window's icon." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#toolbar" :getter [:desktop :window :icon]}
    {:id (uuid) :path-id [:desktop :window :position] :value (get-config [:desktop :window :position]) :label "Window Position" :dom-type :name :help-text "Either null, center or mouse. Controls where window will be put." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#position" :getter [:desktop :window :position]}
    {:id (uuid) :path-id [:desktop :window :min-height] :value (get-config [:desktop :window :min-height]) :label "Minimum Window Height" :dom-type :name :help-text "The minimum height of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#min_widthmin_height" :getter [:desktop :window :min-height] :number true}
    {:id (uuid) :path-id [:desktop :window :min-width] :value (get-config [:desktop :window :min-width]) :label "Minimum Window Width" :dom-type :name :help-text "The minimum width of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#min_widthmin_height" :getter [:desktop :window :min-width] :number true}
    {:id (uuid) :path-id [:desktop :window :max-height] :value (get-config [:desktop :window :max-height]) :label "Maximum Window Height" :dom-type :name :help-text "The maximum height of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#max_widthmax_height" :getter [:desktop :window :max-width] :number true}
    {:id (uuid) :path-id [:desktop :window :max-width] :value (get-config [:desktop :window :max-width]) :label "Maximum Window Width" :dom-type :name :help-text "The maximum width of the main window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#max_widthmax_height" :getter [:desktop :window :max-width] :number true}
    {:id (uuid) :path-id [:desktop :window :as-desktop] :value (get-config [:desktop :window :as-desktop]) :label "Desktop?" :dom-type :checkbox :help-text "Show as desktop background window under X11 environment." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#as_desktop" :getter [:desktop :window :as-desktop]}
    {:id (uuid) :path-id [:desktop :window :resizable] :value (get-config [:desktop :window :resizable]) :label "Resizable?" :dom-type :checkbox :help-text "Whether window is resizable." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#resizable" :getter [:desktop :window :resizable]}
    {:id (uuid) :path-id [:desktop :window :always-on-top] :value (get-config [:desktop :window :always-on-top]) :label "Always On Top?" :dom-type :checkbox :help-text "Whether the window should always stay on top of." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#always-on-top" :getter [:desktop :window :always-on-top]}
    {:id (uuid) :path-id [:desktop :window :fullscreen] :value (get-config [:desktop :window :fullscreen]) :label "Fullscreen?" :dom-type :checkbox :help-text "Whether the window is fullscreen." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#fullscreen" :getter [:desktop :window :fullscreen]}
    {:id (uuid) :path-id [:desktop :window :show-in-taskbar] :value (if (nil? (get-config [:desktop :window :show-in-taskbar])) true  (get-config [:desktop :window :show-in-taskbar])) :label "Show in Taskbar?" :dom-type :checkbox :help-text "whether the window is shown in taskbar or dock. The default is true." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#show_in_taskbar" :getter [:desktop :window :show-in-taskbar]}
    {:id (uuid) :path-id [:desktop :window :frame] :value (get-config [:desktop :window :frame]) :label "Frame?" :dom-type :checkbox :help-text "Make this false to have a frameless window." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#frame" :getter [:desktop :window :frame]}
    {:id (uuid) :path-id [:desktop :window :show] :value (get-config [:desktop :window :show]) :label "Show?" :dom-type :checkbox :help-text "Make this false if you want your app to be hidden on startup" :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#show" :getter [:desktop :window :show]}
    {:id (uuid) :path-id [:desktop :window :kiosk] :value (get-config [:desktop :window :kiosk]) :label "Window Kiosk?" :dom-type :checkbox :help-text  "Whether to use Kiosk mode. In Kiosk mode, the app will be fullscreen and try to prevent users from leaving the app, so you should remember to provide a way in app to leave Kiosk mode. This mode is mainly used for presentation on public displays." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#kiosk" :getter [:desktop :window :kiosk]}
    {:id (uuid) :path-id [:desktop :webkit :plugin] :value (get-config [:desktop :webkit :plugin]) :label "Webkit Plugins?" :dom-type :checkbox :help-text "Whether to load external browser plugins like Flash, default to false." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#plugin" :getter [:desktop :webkit :plugin]}
    {:id (uuid) :path-id [:desktop :webkit :java] :value (get-config [:desktop :webkit :java]) :label "Webkit Java?" :dom-type :checkbox :help-text "Whether to load Java applets, default to false." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#java" :getter [:desktop :webkit :java]}
    {:id (uuid) :path-id [:desktop :webkit :page-cache] :value (get-config [:desktop :webkit :page-cache]) :label "Webkit Page Cache?" :dom-type :checkbox :help-text "Whether to enable page cache, default to false." :help-url "https://github.com/rogerwang/node-webkit/wiki/Manifest-format#page-cache" :getter [:desktop :webkit :page-cache]}]


   :mobile
   [{:id (uuid) :path-id [:mobile :id] :value (get-config [:mobile :id]) :label "App Id" :dom-type :name :help-text "A unique identifier for your app." :help-url "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File" :getter [:mobile :id]}
    {:id (uuid) :path-id [:mobile :platforms] :value (get-config [:mobile :platforms]) :label "Mobile Platforms" :dom-type :checkbox-list :help-text "The mobile platforms to deploy your app on." :help-url "https://cordova.apache.org/docs/en/4.0.0/guide_support_index.md.html#Platform%20Support" :getter [:mobile :platforms] :boxes ["amazon-fire" "android" "blackberry" "firefox-os" "ios" "ubuntu" "wp7" "wp8" "tizen" "webos"]}
{:id (uuid) :path-id [:mobile :preferences] :value (get-config [:mobile :preferences]) :label "Mobile Preferences" :dom-type :cordova-pref :help-text "This is for various cordova preferences. Enter the name of the preference in the left input and its corresponding value in the right input." :help-url "https://cordova.apache.org/docs/en/3.0.0/config_ref_index.md.html#Configuration%20Reference" :getter [:mobile :preferences]}
{:id (uuid) :path-id [:mobile :plugins] :value (get-config [:mobile :plugins]) :label "Cordova Plugins" :dom-type :cordova-plugin :help-text "Official plugins are listed as checkboxes and third party plugins can be added with the text input. Look here for more third-party addons:  http://plugins.cordova.io/" :help-url "https://cordova.apache.org/docs/en/4.0.0/cordova_plugins_pluginapis.md.html#Plugin%20APIs" :getter [:mobile :plugins] :boxes ["org.apache.cordova.battery-status" "org.apache.cordova.camera" "org.apache.cordova.media-capture" "org.apache.cordova.console" "org.apache.cordova.contacts" "org.apache.cordova.device" "org.apache.cordova.device-motion" "org.apache.cordova.device-orientation" "org.apache.cordova.dialogs" "org.apache.cordova.file" "org.apache.cordova.file-transfer" "org.apache.cordova.geolocation" "org.apache.cordova.globalization" "org.apache.cordova.inappbrowser" "org.apache.cordova.media" "org.apache.cordova.network-information" "org.apache.cordova.splashcreen" "org.apache.cordova.vibration"]}
{:id (uuid) :path-id [:mobile :icons] :value (get-config [:mobile :icons]) :label "Mobile Icons" :dom-type :cordova-logos :help-text "Icon files tend vary for each platform. This is a list of all necessary formats. The location is required but the other four fields are optional." :help-url "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens" :getter [:mobile :icons]}
{:id (uuid) :path-id [:mobile :splash] :value (get-config [:mobile :splash]) :label "Mobile Splash Screens" :dom-type :cordova-logos :help-text "Some platforms take a few seconds to load. For those, a splash screen can be used. Location is the only required field to fill in." :help-url "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens" :getter [:mobile :splash]}
{:id (uuid) :path-id [:mobile :permissions] :value (get-config [:mobile :permissions]) :label "Permissions" :dom-type :list :help-text "Whitelist of domains that can connect with your app." :help-url "https://cordova.apache.org/docs/en/4.0.0/guide_appdev_whitelist_index.md.html#Whitelist%20Guide" :getter [:mobile :permissions]}
{:id (uuid) :path-id [:mobile :content] :value (get-config [:mobile :content]) :label "Content" :dom-type :name :help-text "The optional <content> element defines the app's starting page in the top-level web assets directory. The default value is index.html, which customarily appears in a project's top-level www directory." :help-url "https://cordova.apache.org/docs/en/4.0.0/config_ref_index.md.html#The%20config.xml%20File" :getter [:mobile :content]}]})
