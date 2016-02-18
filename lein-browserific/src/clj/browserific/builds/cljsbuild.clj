(ns browserific.builds.cljsbuild
  "Writes the builds for cljsbuild"
  (:require [browserific.helpers.utils :as u]
            [deepfns.core :refer [<=>]]
            [deepfns.transitive :as t]
            [plugin-helpers.core :as p]))

;; FIXME: have :pretty-print false
;; FIXME: watch out! the browser extensions may need a web_accessible_resource (aka permissions)
;; in manifest.json (or whatever) pointing to the sourcemap file
;; http://stackoverflow.com/questions/15097945/do-source-maps-work-for-chrome-extensions
;; https://developer.chrome.com/extensions/manifest/web_accessible_resources

;; shared compiler settings are put here
(def compiler-default
  {:optimizations :none
   :mail :main
   :output-to :out-file
   :target (t/when> (t/eq> :env "desktop")
             (constantly :nodejs))
   :output-dir :out-dir
   :source-map true
   :pretty-print false})

(defn draft>
  "Default settings for the draft build"
  [id]
  (<=> {(if (= id "app")
          :draft
          (keyword (str "draft-" id)))
        {:source-paths [:src "dev"]
         :figwheel true
         :compiler (merge compiler-default
                     {:main (if (= "app" id)
                              :main
                              (str u/project-name "." id))
                      :output-to (t/format> "resources/public/js/%s.js" :id)
                      :output-dir (t/format> "resources/public/js/%s" :id)
                      :asset-path (t/format> "js/%s" :id)})}}))

(defn- build-id [profile type]
  (keyword (str profile "-" type)))

(defn dev>
  "Default settings for dev builds"
  [profile]
  (<=> {(build-id profile "dev")
        {:source-paths [:src "dev"]
         :figwheel true
         :compiler (merge compiler-default
                     {:asset-path :asset})}}))

(defn test>
  "Default settings for test builds"
  [profile]
  (<=> {(build-id profile "test")
        {:source-paths [:src (t/format> "test/%s" :platform)]
         :compiler (merge compiler-default
                     {:output-dir (t/format> "%s-test" :out-dir)})}}))

(defn release>
  "Default settings for release builds"
  [profile]
  (<=> {(build-id profile "release")
        {:source-paths [:src]
         :compiler (merge compiler-default
                     {:optimizations :advanced
                      :output-dir (t/format> "%s-release" :out-dir)
                      :source-map (t/format> "%s.map" :out-file)})}}))

(defn to-build
  "Returns a transitive that matches the build type"
  [id profile type]
  (case type
    "draft" (draft> id)
    "dev" (dev> profile)
    "test" (test> profile)
    "release" (release> profile)))


(defn get-root-dir
  "Returns the proper root directory for JavaScript output"
  [id env type platform]
  (cond
    (and (= type "draft") (= id "background"))
    (str "resources/public/js/background")

    (and (= type "draft") (= id "content"))
    (str "resources/public/js/content")

    (= type "draft")
    (str "resources/public/js/out")

    (= env "mobile")
    (str "resources/" env "/" u/project-name "/platforms/" platform "/www/js/")

    (= env "desktop")
    (str "resources/" env "/deploy/" platform "/js/")

    (= env "extension")
    (str "resources/" env "/" platform "/" u/project-name "/js/")))

;; Easier to test if not dependent on external config
(defn get-platform-env [platform]
  (cond
    (u/all-mobile platform) "mobile"
    (u/all-desktop platform) "desktop"
    (u/all-browsers platform) "extension"))

(def build-fmt
  (<=> {:src (t/if> (not= "app" :id)
               (t/format> "target/intermediate/%s/%s" :platform :id)
               (t/format> "target/intermediate/%s/" :platform))
        :root :root
        :out-file (t/str> :root :id ".js")
        :out-dir (t/str> :root :profile "-" :id)
        :asset (t/format> "js/%s-%s" :profile :id)
        :main (str u/project-name ".core")
        :id :id
        :profile :profile
        :env :env}))

(defn to-build-fmt [id env profile type platform]
  {:root (get-root-dir id env type platform)
   :id id
   :env env
   :profile profile
   :platform platform})

;; env := 'extension' | 'desktop' | 'mobile'
;; platform := [vendor-id]
;; id := 'app' | 'background' | 'content'
;; profile := platform | 'all' | env
;; type := 'draft' | 'dev' | 'test' | 'release'
(defn write-platform
  "Writes all applicable cljsbuild ids for a given platform (aka vendor
  id, like android or ios)."
  ;; for normal builds
  ([platform]
   (let [env (get-platform-env platform)
         profile [platform "all" env]]
     (write-platform platform env profile ["dev" "test" "release"])))

  ;; for the draft build
  ([platform type]
   (let [env (get-platform-env platform)]
     (write-platform platform env [platform] ["draft"])))

  ([platform env profile type]
   (let [id (if (= "extension" env)
              ["background" "content"]
              ["app"])]
     (for [id id
           profile profile
           type type]
       (let [build> (to-build id profile type)]
         ((comp build> build-fmt to-build-fmt)
          id env profile type platform))))))

(defn write-builds
  "Inserts builds for cljsbuild into the project.clj."
  [{:keys [draft custom]}]
  (let [seed (if draft
               (write-platform draft "draft")
               [])
        standard-builds (reduce #(into % (write-platform %2))
                          seed u/platforms)
        with-custom (if custom
                      standard-builds
                      (into standard-builds custom))
        builds (reduce into with-custom)]
    (p/assoc-in-project [:cljsbuild :builds] builds)))
