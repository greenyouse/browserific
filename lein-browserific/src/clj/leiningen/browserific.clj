(ns leiningen.browserific
  (:require [leiningen.help :as lhelp]
            [leiningen.core.main :as lmain]
            [clojure-watch.core :refer [start-watch]]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [browserific.config :as conf]
            [browserific.parser :as parse]
            [browserific.helpers.utils :as u :refer [yellow-text red-text]]
            [browserific.config.server :as server]
            [clojure.java.browse :as b]
            [clojure.java.shell :as sh])
  (:import java.io.File))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Helper fns

(defn- write-dirs []
  (doseq [plat #{"chrome" "firefox" "opera" "safari" "mobile"
                 "linux32" "linux64" "osx32" "osx64" "windows"}]
    (let [loc (File. (str "intermediate/" plat))]
      (if-not (.isDirectory loc)
        (do (.mkdirs (File. (str loc "/content")))
            (.mkdirs (File. (str loc "/background"))))))))

(defn get-source-files
  "Finds any clojure or clojurescript source files
   in a directory. Returns a seq strings."
  [dir]
  (filter #(u/member? (fs/extension %) [".clj" ".cljs"])
          (map str (file-seq (File. dir)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Leiningen fns

;; TODO: Detect when a build has already been done and throw error
(comment (defn- init
  "Build the basic project outline for Node-Webkit or Cordova.
  When called without arguments, both Node-Webkit and Cordova will
  be built.

  init options are: node, cordova, or *blank*"
  [project & args]
  (letfn [(display [msg] (lmain/info (yellow-text "Building " msg)))
          (build [b]
            (try b (catch Exception e (lmain/warn (red-text e)))))]
    (let [p-name (second project)
          mobile  (str "mkdir -p resources/mobile/" p-name "/ && cd /resources/mobile/" p-name " && cordova create " p-name)
          desktop (str "mkdir -p resources/desktop/" p--name "/ && cd /resources/desktop/" p-name " && " )]
      (case args
        "cordova" (build (do (display "a new Cordova project")
                             (sh/sh mobile)))
        "node" (build (do (display " a new Node-Webkit project")
                          (sh/sh desktop)))
        (build (do (display " Cordova and Node-Webkit projects")
                   (sh/sh mobile) (sh/sh desktop))))))))

(defn- once
  "Compile the browserific files once "
  [project]
  (lmain/info (yellow-text "Compiling Browserific files.\n"))
  (fs/delete-dir "intermediate")
  (write-dirs)
  (conf/build-configs)
  (let [src (or (get-in project [:browserific :source-paths]) "src")
        files (vec (get-source-files src))]
    (parse/parse-files files)))

(defn- auto
  "Automatically recompile browserific files when they are changed"
  [project]
  (start-watch [{:path (or (get-in project [:browserific :source-paths]) "src")
                 :event-types [:create :modify :delete]
                 :callback (fn [e f] (once project))
                 :options {:recursive true}}]))

;; FIXME: should this clean each lein-cjlsbuild file too?
(defn- clean
  "Remove files previously generated by browserific"
  []
  (lmain/info (yellow-text "Deleting files generated by Browserific.\n"))
  (fs/delete-dir "intermediate"))

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

;; FIXME: should load the webpage without needing to manually reload!
(defn- config
  "Launch a server over port 50000 that has a GUI for building a config.edn file"
  []
  (lmain/info (yellow-text "Starting a new browserific config server. Don't forget to reload the page!"))
  (b/browse-url "http://localhost:50000")
  (server/config-server))


(defn browserific
  "Run lein-browserific"
  {:help-arglists '([once auto clean sample config])
   :subtasks [#'once #'auto #'clean #'sample #'config]}
  ([project]
     (lmain/info
      (lhelp/help-for "browserific"))
     (lmain/abort))
  ([project subtask & args]
     (case subtask
       "once" (once project)
       "auto" (auto project)
       "clean" (clean)
       "sample" (apply sample args)
       "config" (config)
       (do
         (lmain/warn (red-text (str "ERROR: Subtask " subtask " not found."))
          (lhelp/subtask-help-for *ns* #'browserific))
         (lmain/abort)))))


(comment "init" (init args))
