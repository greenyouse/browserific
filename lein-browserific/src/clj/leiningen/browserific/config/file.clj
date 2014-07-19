(ns woot.test)

(def data
  (read-string (slurp "resources/public/data/config.edn")))

(defn getter [opt]
  (get-in data opt))

;; re-write this with no fancy id?
;; id, val, label, dom-type, help-text, help-url
(def test-data
  "This is an OM schema that is used for rendering the configs."
  {:general
   [{:id :name :value (getter [:name]) :label "Name" :dom-type :name-entry :help-text "The name of your app" :help-url nil}
    {:id :author:author-name :value (getter [:author :author-name]) :label "Author Name" :dom-type :name-entry :help-text "Your name." :help-url nil}
    {:id :author:author-id :value (getter [:author :author-id]) :label "Author-ID" :dom-type :name-entry :help-text "Your id or company id." :help-url nil}
    {:id :author:email :value (getter [:author :email]) :label "Author's e-mail" :dom-type :name-entry :help-text "Your email address." :help-url nil}
    {:id :author:url :value (getter [:author :url]) :label "Project Website" :dom-type :name-entry :help-text "Your website." :help-url nil}
    {:id :author:contributors :value (getter [:author :contributors]) :label "Contributors" :dom-type :list :help-text "Any contributors on the project." :help-url nil}]})


;;; Create OM WebPage
;; X 1. Translate incoming EDN
;; X 2. Lookup each node in the database to (assoc their page + position + DOM type + id + help
;; ~ 3. Pass to OM and make a cursor for each page
;; ~ 4. Build each page based on the position of each item (little cursors)
;; ~ 5. For each little cursor, pass to a generic render fn that dispatches on DOM type

;;; Updating the Page
;; ~ 1. Update or delete items using DOM buttons and inputs
;; 2. Pass the new value back to the sever side and update the config-state
;; 3. Overwrite the old config.edn file with the new state

;;; Add to plugin
;; 1. Write a little command in browserific.clj that calls this
;; 2. Write some tests and test it by hand too
