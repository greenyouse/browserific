(ns browserific.config.pages.desktop
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn desktop-page
  "The page for desktop options"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :checkbox-list :data (reagent/cursor [:desktop :platforms] config-db)
      :label "Desktop Platforms" :htxt "The desktop platforms your app will be published to."
      :hurl nil :boxes ["linux32" "linux64" "osx32" "osx64" "windows32" "windows64"]}
     {:type :name :data (reagent/cursor [:desktop :main] config-db) :label "Main Desktop Page" :htxt "The app's starting page."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#main"}
     {:type :checkbox :data (reagent/cursor [:desktop :nodejs] config-db) :label
      "NodeJS?" :htxt "Checking this will enable Node support in WebKit."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#nodejs"}
     {:type :name :data (reagent/cursor [:desktop :node-main] config-db) :label "Node Main" :htxt
      "Specifies the path to a node.js script file which gets loaded before the DOM."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#node-main"}
     ;; NOTE: When a checkbox defaults to true, give it a tag saying so
     {:type :checkbox :data (reagent/cursor [:desktop :single-instance] config-db) :label "Single Instance?"
      :htxt "By default node-webkit only allows one instance of your app if your app is a standalone package, if you want to allow multiple instances of your app running at the same time, make this false."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#single-instance"
      :default true}
     {:type :name :data (reagent/cursor [:desktop :user-agent :name] config-db) :label "User-Agent Name"
      :htxt "Overrides the User-Agent's name in HTTP requests made from the application."
      :hurl "https://en.wikipedia.org/wiki/User_agent_string"}
     {:type :name :data (reagent/cursor [:desktop :user-agent :ver] config-db) :label "User-Agent Version"
      :htxt "Overrides the User-Agent's version in HTTP requests made from the application."
      :hurl "https://en.wikipedia.org/wiki/User_agent_string"}
     {:type :name :data (reagent/cursor [:desktop :user-agent :nwver] config-db) :label "User-Agent NW Version"
      :htxt "Overrides the User-Agent's node-webkit version in HTTP requests made from the application."
      :hurl "https://en.wikipedia.org/wiki/User_agent_string"}
     {:type :name :data (reagent/cursor [:desktop :user-agent :webkit-ver] config-db) :label "User-Agent Webkit Version"
      :htxt "Overrides the User-Agent's webkit version in HTTP requests made from the application."
      :hurl "https://en.wikipedia.org/wiki/User_agent_string"}
     {:type :name :data (reagent/cursor [:desktop :user-agent :osinfo] config-db) :label "User-Agent OS Info"
      :htxt "Overrides the User-Agent header in HTTP requests made from the application."
      :hurl "https://en.wikipedia.org/wiki/User_agent_string"}
     {:type :strings :data (reagent/cursor [:desktop :permissions] config-db) :label "Node Remote Permissions"
      :htxt "Whitelist of domains that can connect with your app."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#node-remote"}
     {:type :name :data (reagent/cursor [:desktop :chromium-args] config-db) :label "Chromium Args"
      :htxt "Chromium command line arguments for custom settings."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#chromium-args"}
     {:type :name :data (reagent/cursor [:desktop :js-flags] config-db) :label "JS Flags"
      :htxt "Flags to pass to the V8 JavaScript engine"
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#js-flags"}
     {:type :name :data (reagent/cursor [:desktop :inject-js-start] config-db) :label "Inject JS Start"
      :htxt "Relative path to a injected JavaScript file that is to be executed after any files from css, but before any other DOM is constructed or any other script is run."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#inject-js-start-inject-js-end"}
     {:type :name :data (reagent/cursor [:desktop :inject-js-end] config-db) :label "Inject JS End"
      :htxt  "Relative path to a JavaScript file to be executed after the document object is loaded and before the onload event is fired."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#inject-js-start-inject-js-end"}
     {:type :name :data (reagent/cursor [:desktop :snapshot] config-db) :label "Snapshot"
      :htxt "Relative path to a JavaScript file that has been compiled down to native code."
      :hurl "https://github.com/nwjs/nw.js/wiki/Protect-JavaScript-source-code-with-v8-snapshot"}
     {:type :name :data (reagent/cursor [:desktop :dom-storage-quota] config-db) :label "DOM Storage Quota"
      :htxt "Number of megabytes for the quota of the DOM storage. The suggestion is to put double the value you want."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#dom-storage-quota"
      :number true}
     {:type :strings :data (reagent/cursor [:desktop :keywords] config-db) :label "Keywords"
      :htxt "Keywords to assist users searching for the package in catalogs."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#keywords"}
     {:type :name :data (reagent/cursor [:desktop :bugs] config-db) :label "Bug URL"
      :htxt "URL for submitting bugs. Can be mailto or http."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#bugs"}
     {:type :name :data (reagent/cursor [:desktop :repositories 0 :type] config-db) :label "Repository Type"
      :htxt "The type of your repo. For example, it could be git or Hg."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#repositories"}
     {:type :name :data (reagent/cursor [:desktop :repositories 0 :url] config-db) :label "Repository URL"
      :htxt "URL for your repo."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#repositories"}
     {:type :name :data (reagent/cursor [:desktop :repositories 0 :path] config-db) :label "Repository Path"
      :htxt "If your package is not at the root of your repo, use this to designate the path to the package."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#repositories"}
     {:type :name :data (reagent/cursor [:desktop :window :title] config-db) :label "Window Title"
      :htxt "The default title of window created by node-webkit, it's very useful if you want to show your own title when the app is starting."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#title"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :toolbar] config-db) :label "Navigation Toolbar?"
      :htxt "Should the navigation toolbar be shown?"
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#toolbar"}
     {:type :name :data (reagent/cursor [:desktop :window :icon] config-db) :label "Icon"
      :htxt "Relative path to window's icon."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#toolbar"}
     {:type :name :data (reagent/cursor [:desktop :window :position] config-db) :label "Window Position"
      :htxt "Either null, center or mouse. Controls where window will be put."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#position"}
     {:type :name :data (reagent/cursor [:desktop :window :min-height] config-db) :label "Minimum Window Height"
      :htxt "The minimum height of the main window."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#min_widthmin_height"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :height] config-db) :label "Window Height"
      :htxt "The initial height of the main window."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#widthheight"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :width] config-db) :label "Window Width"
      :htxt "The initial width of the main window."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#widthheight"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :max-height] config-db) :label "Maximum Window Height"
      :htxt "The maximum height of the main window, leave blank for none."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#max_widthmax_height"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :max-width] config-db) :label "Maximum Window Width"
      :htxt "The maximum width of the main window, leave blank for none."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#max_widthmax_height"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :min-height] config-db) :label "Minimum Window Height"
      :htxt "The minimum height of the main window, leave blank for none."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#min_widthmin_height"
      :number true}
     {:type :name :data (reagent/cursor [:desktop :window :min-width] config-db) :label "Minimum Window Width"
      :htxt "The minimum width of the main window, leave blank for none."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#min_widthmin_height"
      :number true}
     {:type :checkbox :data (reagent/cursor [:desktop :window :as-desktop] config-db) :label "X11 Background?"
      :htxt "Show as desktop background window under X11 environment."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#as_desktop"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :resizable] config-db) :label "Resizable?"
      :htxt "Whether the window is resizable."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#resizable"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :always-on-top] config-db) :label "Always On Top?"
      :htxt "Whether the window should always stay on top of."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#always-on-top"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :fullscreen] config-db) :label "Fullscreen?"
      :htxt "Whether the window is fullscreen."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#fullscreen"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :show-in-taskbar] config-db) :label "Show in Taskbar?"
      :htxt "Whether the window is shown in taskbar or dock. The default is true."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#show_in_taskbar"
      :default true}
     {:type :checkbox :data (reagent/cursor [:desktop :window :frame] config-db) :label "Frame?"
      :htxt "Make this false to have a frameless window."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#frame"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :show] config-db) :label "Show?"
      :htxt "Make this false if you want your app to be hidden on startup"
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#show"}
     {:type :checkbox :data (reagent/cursor [:desktop :window :kiosk] config-db) :label "Window Kiosk?"
      :htxt "Whether to use Kiosk mode. In Kiosk mode, the app will be fullscreen and try to prevent users from leaving the app, so you should remember to provide a way in app to leave Kiosk mode. This mode is mainly used for presentation on public displays."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#kiosk"}
     {:type :checkbox :data (reagent/cursor [:desktop :webkit :plugin] config-db) :label "Webkit Plugins?"
      :htxt "Whether to load external browser plugins like Flash, default to false."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#plugin"}
     {:type :checkbox :data (reagent/cursor [:desktop :webkit :java] config-db) :label "Webkit Java?"
      :htxt "Whether to load Java applets, default to false."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#java"}
     {:type :checkbox :data (reagent/cursor [:desktop :webkit :page-cache] config-db) :label "Webkit Page Cache?"
      :htxt "Whether to enable page cache, default to false."
      :hurl "https://github.com/nwjs/nw.js/wiki/Manifest-format#page-cache"}]))
