(ns leiningen.browserific
  (:require [leiningen.help :as lhelp]
            [leiningen.core.main :as lmain]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [browserific.config :as conf]
            [browserific.helpers.utils :as u :refer [yellow-text red-text]]
            [browserific.config.server :as server]
            [browserific.builds.browserific :as bbuilds]
            [browserific.builds.chenex :as cb]
            [clojure.java.browse :as b]
            [chenex.core :as cc])
  (:refer-clojure :exclude [compile])
  (:import java.io.File))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Helper fns

(defn- write-dirs []
  (doseq [plat u/platforms]
    (.mkdirs (File. (str "intermediate/" plat)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Leiningen fns

(defn- build
  "Write lein-cljsbuilds for all relevant platforms"
  ([]
   (let [{:keys [draft multi] :as opts} u/options]
     (lmain/info (yellow-text "Writing a new lein-cljsbuild configuration.\n"))
     (cond
       (and draft (not (contains? u/platforms draft))) ;invalid draft platform
       (lmain/abort (red-text "Browserific Error: " draft
                      " is not a valid platform.\n\nOptions are:\n\n" u/platforms))
       (empty? draft)
       (do (lmain/warn
             (red-text "Warning: no draft platform specified but building config anyway.\n"))
           (fs/delete-dir "builds")
           (bbuilds/write-browserific-builds)
           (cb/write-chenex-builds))
       :else
       (do (fs/delete-dir "builds")
           (bbuilds/write-browserific-builds opts)
           (cb/write-chenex-builds draft))))))

(defn- compile
  "Compile the browserific files"
  [project]
  (lmain/info (yellow-text "Compiling Browserific files.\n"))
  (fs/delete-dir "intermediate")
  (write-dirs)
  (conf/build-configs)
  (cc/run (get-in project [:chenex :builds])))

;; FIXME: should this clean each lein-cjlsbuild file too?
(defn- clean
  "Remove files previously generated by browserific, does not remove config files"
  []
  (lmain/info (yellow-text "Deleting files generated by Browserific.\n"))
  (fs/delete-dir "intermediate")
  (fs/delete-dir "builds"))

(defn- sample
  "Shows a sample config.edn for either extension, mobile, or desktop.
   If no platform is specified, a sample with all three platforms will be
   shown."
  [& type]
  (case (first type)
    "extension" (-> (io/resource "samples/extension-sample.edn") slurp lmain/info)
    "mobile" (-> (io/resource "samples/mobile-sample.edn") slurp lmain/info)
    "desktop" (-> (io/resource "samples/desktop-sample.edn") slurp lmain/info)
    (-> (io/resource "samples/all-samples.edn") slurp lmain/info)))

(defn- config
  "Launch a server over port 50000 that has a GUI for building a config.edn file"
  []
  (lmain/info (yellow-text "Starting a new browserific config server on port 50000."))
  (future (Thread/sleep 2000)
          (b/browse-url "http://localhost:50000"))
  (server/config-server))

(defn browserific
  "Run lein-browserific"
  {:help-arglists '([build compile clean sample config])
   :subtasks [#'build #'compile #'clean #'sample #'config]}
  ([project]
     (lmain/info
      (lhelp/help-for "browserific"))
     (lmain/abort))
  ([project subtask & args]
     (case subtask
       "build" (if args (apply build args) (build))
       "compile" (compile project)
       "clean" (clean)
       "sample" (apply sample args)
       "config" (config)
       (do
         (lmain/warn (red-text "Browserific Error: Subtask " subtask " not found.")
          (lhelp/subtask-help-for *ns* #'browserific))
         (lmain/abort)))))
