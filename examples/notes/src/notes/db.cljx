(ns notes.db
  (:require [pldb-cache.core :as pc]
            [cljs.core.logic.pldb :as pldb])
  (:require-macros [cljs.core.logic.macros :as lm]
                   [cljs.core.logic.pldb :as pm]))

;; not actually very good, just for easy understanding
(defn uuid []
  (rand 100000000000000000000000))

(pm/db-rel note ^:index id title text)

;; namespacing is important if using web storage
;; there can be multiple db-atoms for different things
(def notes-atom
  (pc/db-atom "com.greenyouse.notes.notes"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Operations

;; Use views for databinding
(defn list-view
  "Fetches all the notes from memory"
  []
  (pm/with-db @notes-atom
    (lm/run* [q]
      (lm/fresh [id title text]
        (note id title text)
        (lm/== q [id title text])))))

(defn lookup
  "Gets a note using its id"
  [id]
  (pm/with-db @notes-atom
    (lm/run* [q]
      (lm/fresh [title text]
        (note id title text)
        (lm/== q [id title text])))))

(defn rm-note
  "Deletes a note"
  [id title text]
  (pc/rm-facts! notes-atom [note id title text]))

;; if note not in db, just add it
;; else swap the old version for the new
(defn save-note
  "Saves or updates a note"
  [id title text]
  (let [old (lookup id)
        [oid otitle otext] (first old)]
    (if (empty? old)
      (pc/add-facts! notes-atom [note id title text])
      (do (rm-note oid otitle otext)
          (pc/add-facts! notes-atom [note id title text])))))
