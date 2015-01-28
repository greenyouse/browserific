(ns browserific.config.pages.general
  (:require [browserific.config.db :refer [config-db]]
            [browserific.config.components :as co]
            [reagent.core :as reagent]))

(defn general-page
  "The page for common, general settings"
  []
  (reduce (fn [div c]
            (conj div ^{:key (gensym)} (co/generic-c c)))
    [:div]
    [{:type :name :data (reagent/cursor [:name] config-db) :label "Name"
      :htxt "The name of your app"}
     {:type :name :data (reagent/cursor [:author :author-name] config-db)
      :label "Author Name" :htxt "Your name."}
     {:type :name :data (reagent/cursor [:author :author-id] config-db)
      :label "Author-ID" :htxt "Your id or company's id."}
     {:type :name :data (reagent/cursor [:author :email] config-db)
      :label "Author's e-mail" :htxt "Your e-mail address"}
     {:type :name :data (reagent/cursor [:author :url] config-db)
      :label "Project Website" :htxt "Your website for this project."}
     {:type :strings :data (reagent/cursor [:author :contributors] config-db)
      :label "Contributors" :htxt "Any contributors on the project."}
     {:type :name :data (reagent/cursor [:version] config-db)
      :label "Version" :htxt "The version of your project."}
     {:type :name :data (reagent/cursor [:license] config-db) :label
      "License " :htxt "The license of your project."}
     {:type :name :data (reagent/cursor [:description] config-db)
      :label "Description" :htxt "A short description of your project."}
     {:type :name :data (reagent/cursor [:default-locale] config-db)
      :label "Default Locale" :htxt "A default locale for your project.\n\nThis can be a two digit language code (like en) or a regional code (like en_US or en_GB)."
      :hurl "https://developer.chrome.com/extensions/i18n#overview-locales"}]))
