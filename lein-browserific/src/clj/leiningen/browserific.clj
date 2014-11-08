(ns leiningen.browserific
  (:require [leiningen.help :as lhelp]
            [leiningen.core.main :as lmain]
            [clojure-watch.core :refer [start-watch]]
            [clojure.java.io :as io]
            [fs.core :as fs]
            [browserific.config :as conf]
            [browserific.parser :as parse])
  (:import java.io.File))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Helper fns

;; Taken from cljx
(defn- clojure-source-file?
  "Returns true if the file has a .clj or .cljs extension."
  [^File file]
  (and (.isFile file)
       (or (.endsWith (.getName file) ".clj")
           (.endsWith (.getName file) ".cljs"))))

(defn- find-clojure-sources-in-dir
  "Searches recursively under dir for clojure and clojurescript files.
Returns a sequence of File objects, in breadth-first sort order."
  [^File dir]
  ;; Use sort by absolute path to get breadth-first search.
  (sort-by #(.getAbsolutePath ^File %)
           (filter clojure-source-file? (file-seq (io/file dir)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Leiningen fns

(defn- once [project]
  (println "Compiling Browserific files.")
  (fs/delete-dir "intermediate")
  (conf/build-configs)
  (let [src (or (get-in project [:browserific :source-paths]) "src")
        files (find-clojure-sources-in-dir src)
        f (parse/parse-files files)]
    ;; HACK: why doesn't leiningen like parse-files?!
    ;; There should be some way around this...
    (print f)))

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
  (println "Deleting files generated by browserific.")
  (fs/delete-dir "intermediate"))

(defn- sample
  "Shows a sample config.edn for either extension, mobile, or desktop.
   If no platform is specified, a sample with all three platforms will be
   shown."
  [& type]
  (case (first type)
    "extension" (-> (io/resource "samples/extension-sample.edn") slurp println)
    "mobile" (-> (io/resource "samples/mobile-sample.edn") slurp println)
    "desktop" (-> (io/resource "samples/desktop-sample.edn") slurp println)
    (-> (io/resource "samples/all-samples.edn") slurp println)))

(defn- config
  "Launch a server over port 4242 that has a GUI for building a config.edn file"
  [])


(defn browserific
  "Run lein-browserific"
  {:help-arglists '([once auto clean sample config])
   :subtasks [#'once #'auto #'clean #'sample #'config]}
  ([project]
     (println
      (lhelp/help-for "browserific"))
     (lmain/abort))
  ([project subtask & args]
     (case subtask
       "once" (once project)
       "auto" (auto project)
       "clean" (clean)
       "sample" (apply sample args)
       "config" (println "not done yet...")
       (do
         (println
          "\033[31mERROR: Subtask " (str subtask ) " not found.\033[0m"
          (lhelp/subtask-help-for *ns* #'browserific))
         (lmain/abort)))))
