(ns browserific.config.pages.mobile.ios
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn ios-page
  "Configurations for ios"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:label "iOS Preferences"
      :htxt "Preferences for iOS"
      :hurl "https://cordova.apache.org/docs/en/4.0.0/guide_platforms_ios_config.md.html#iOS%20Configuration"
      :prefs
      [{:type :checkbox :data (reagent/cursor [:mobile :preferences :ios :EnableViewportScale] config-db) :label "Viewport Scale?"
        :help "Set to true to allow a viewport meta tag to either disable or restrict the range of user scaling, which is enabled by default."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ios :MediaPlaybackRequiresUserAction] config-db) :label "UserAction for Media?"
        :help "Set to true to prevent HTML5 videos or audios from playing automatically with the autoplay attribute or via JavaScript."}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ios :AllowInlineMediaPlayback] config-db) :label "Inline MediaPlayback?"
        :help "Set to true to allow HTML5 media playback to appear inline within the screen layout, using browser-supplied controls rather than native controls. For this to work, add the webkit-playsinline attribute to any <video> elements."}
       {:type :select :data (reagent/cursor [:mobile :preferences :ios :BackupWebStorage] config-db) :label "Backup Web Storage"
        :help "Set to cloud to allow web storage data to backup via iCloud. Set to local to allow only local backups via iTunes sync. Set to none prevent web storage backups. The default is cloud."
        :options ["none" "local" "cloud"]}
       {:type :select :data (reagent/cursor [:mobile :preferences :ios :TopActivityIndicator] config-db) :label "Top Activity Indicator"
        :help "Controls the appearance of the small spinning icon in the status bar that indicates significant processor activity."
        :options ["whiteLarge" "white" "gray"]}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ios :KeyboardDisplayRequiresUserAction] config-db) :label "Keyboard UserAction?"
        :help "Set to false to allow the keyboard to appear when calling focus() on form inputs."
        :default true}
       {:type :checkbox :data (reagent/cursor [:mobile :preferences :ios :SuppressesIncrementalRendering] config-db) :label "Supress Incremental Rendering?"
        :help "Set to true to wait until all content has been received before it renders to the screen."}
       {:type :name :data (reagent/cursor [:mobile :preferences :ios :GapBetweenPages] config-db) :label "Gap Between Pages"
        :help "The size of the gap, in points, between pages."}
       {:type :name :data (reagent/cursor [:mobile :preferences :ios :PageLength] config-db) :label "Page Length"
        :help "The size of each page, in points, in the direction that the pages flow. When PaginationMode is right to left or left to right, this property represents the width of each page. When PaginationMode is topToBottom or bottomToTop, this property represents the height of each page. The default value is 0, which means the layout uses the size of the viewport to determine the dimensions of the page."}
       {:type :select :data (reagent/cursor [:mobile :preferences :ios :PaginationBreakingMode] config-db) :label "Pagination Breaking Mode"
        :help "The manner in which column- or page-breaking occurs. This property determines whether certain CSS properties regarding column- and page-breaking are honored or ignored. When this property is set to column, the content respects the CSS properties related to column-breaking in place of page-breaking."
        :options ["page" "column"]}
       {:type :select :data (reagent/cursor [:mobile :preferences :ios :PaginationMode] config-db) :label "Pagination Mode"
        :help "This property determines whether content in the web view is broken up into pages that fill the view one screen at a time, or shown as one long scrolling view. If set to a paginated form, this property toggles a paginated layout on the content, causing the web view to use the values of PageLength and GapBetweenPages to relayout its content."
        :options ["unpaginated" "leftToRight" "topToBottom" "bottomToTop" "rightToLeft"]}
       {:type :select :data (reagent/cursor [:mobile :preferences :ios :UIWebViewDecelerationSpeed] config-db) :label "UI WebView Decelartion Speed"
        :help "This property controls the deceleration speed of momentum scrolling. normal is the default speed for most native apps, and fast is the default for Mobile Safari."
        :options ["normal" "fast"]}]}
     {:type :cordova-multi :data (reagent/cursor [:mobile :icons :ios] config-db) :label "iOS Icons"
      :htxt "Icons for iOS."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "ios"}
     {:type :cordova-multi :data (reagent/cursor [:mobile :splash :ios] config-db) :label "iOS Splashscreen"
      :htxt "Splashscreen for iOS."
      :hurl "https://cordova.apache.org/docs/en/4.0.0/config_ref_images.md.html#Icons%20and%20Splash%20Screens"
      :plat "ios"}]))
