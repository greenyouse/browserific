(ns browserific.config.pages.browser.safari
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent])
  (:require-macros [browserific.config.macros :refer [multi-input-template]]))

(defn safari-page
  "The page for Safari configurations"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :multi :data (reagent/cursor [:extensions :extra :safari :bars] config-db) :label "Extension Bars"
      :htxt "An extension bar is space below the bookmarks bar and above the tab bar reserved for your extension. If you want to display persistent data in the browser frame, create an extension bar."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/AddingExtensionToolbars/AddingExtensionToolbars.html#//apple_ref/doc/uid/TP40009977-CH5-SW2"
      :multi-c (multi-input-template :vec [{:type :name :label "Filename"}
                                           {:type :name :label "Identifier"}
                                           {:type :name :label "Label"}])}
     {:type :multi :data (reagent/cursor [:extensions :extra :safari :context-items] config-db) :label "Context Items"
      :htxt "Items your extension adds to contextual menus."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/AddingContextualMenuItems/AddingContextualMenuItems.html#//apple_ref/doc/uid/TP40009977-CH4-SW1"
      :multi-c (multi-input-template :vec [{:type :name :label "Command"}
                                           {:type :name :label "Identifier"}
                                           {:type :name :label "Title"}])}
     {:type :multi :data (reagent/cursor [:extensions :extra :safari :menu] config-db) :label "Menus"
      :htxt "Pop-up menus you associate with a toolbar item you have created."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/UsingExtensionBuilder/UsingExtensionBuilder.html#//apple_ref/doc/uid/TP40009977-CH2-SW1"
      :multi-c (multi-input-template :vec [{:type :name :label "Identifier"}
                                           {:type :name :label "Command"}
                                           {:type :checkbox :label "Disabled?"}
                                           {:type :name :label "Menu-Identifier"} ;doesn't match exactly with output (should be identifier too)
                                           {:type :name :label "Title"}])}
     {:type :multi :data (reagent/cursor [:extensions :extra :safari :popovers] config-db) :label "Popovers"
      :htxt "Pop-up windows containing HTML content that you associate with a toolbar item you have created."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/UsingExtensionBuilder/UsingExtensionBuilder.html#//apple_ref/doc/uid/TP40009977-CH2-SW1"
      :multi-c (multi-input-template :vec [{:type :name :label "Filename"}
                                           {:type :name :label "Height" :number true}
                                           {:type :name :label "Width" :number true}
                                           {:type :name :label "Identifier"}])}
     {:type :multi :data (reagent/cursor [:extensions :extra :safari :toolbar] config-db) :label "Toolbar"
      :htxt "Buttons you are adding to the main Safari toolbar (not an extension bar). The code that listens for the button click and executes the command goes in either your global HTML page or an extension bar."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/AddingButtonstotheMainSafariToolbar/AddingButtonstotheMainSafariToolbar.html#//apple_ref/doc/uid/TP40009977-CH3-SW1"
      :multi-c (multi-input-template :vec [{:type :name :label "Command"}
                                           {:type :name :label "Identifier"}
                                           {:type :name :label "Image"}
                                           {:type :name :label "Label"}
                                           {:type :name :label "Menu"}
                                           {:type :name :label "Pallete"}
                                           {:type :name :label "Popover"}
                                           {:type :name :label "Tool-.-Tip"}])} ;FIXME: byproduct of tokenizing
     ;; FIXME: make this in MB and multiply somewhere in the workflow
     {:type :name :data (reagent/cursor [:extensions :extra :safari :database-quota] config-db) :label "Database Quota (in bytes)"
      :htxt "The space you want to allocate for HTML5 client-side database storage for your extension (in bytes)."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/ExtensionSettings/ExtensionSettings.html#//apple_ref/doc/uid/TP40009977-CH11-SW1"
      :number true}
     {:type :name :data (reagent/cursor [:extensions :extra :safari :start-script] config-db) :label "Start Script"
      :htxt "Scripts to execute before a webpage is interpreted, usually a script that blocks unwanted content."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/UsingExtensionBuilder/UsingExtensionBuilder.html#//apple_ref/doc/uid/TP40009977-CH2-SW1"}
     {:type :name :data (reagent/cursor [:extensions :extra :safari :global-page] config-db) :label "Global Page"
      :htxt "A global HTML page is a place for you to put JavaScript code, data tables, and other resources requiring no user interface that your extension needs to load only once per Safari session. The global page is not mandatory. You can have at most one global page per extension."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/AddingaGlobalHTMLPage/AddingaGlobalHTMLPage.html#//apple_ref/doc/uid/TP40009977-CH16-SW2"}
     ;; TODO: do we need to add regex patterns if the choice is some?
     {:type :select :data (reagent/cursor [:extensions :extra :safari :access-level] config-db) :label "Access Level"
      :htxt "What level of access your extension has to external webpages."
      :hurl "https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/ExtensionPermissions/ExtensionPermissions.html#//apple_ref/doc/uid/TP40009977-CH8-SW1"
      :options ["None" "Some" "All"]}]))
