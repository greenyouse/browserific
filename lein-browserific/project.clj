(defproject lein-browserific "0.1.0-SNAPSHOT"
  :description "Pre-compiler to convert browserific code to API code"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[environ "0.4.0"]
                 [org.clojure/data.xml "0.0.7"]]
  :plugins [[lein-environ "0.4.0"]]

  ;; TODO: delete the example :browserific when finished

  ;; create this in the template with `lein new browserific-app [:firefox ... ]
  ;; use this for running the compiler, one for each option
  ;; default is failure, give help map. Use helpers for: all, mobile, and extensions
  :browserific {:platforms [:firefox :opera :blackberry
                            :ios :android]
                :config "test/test-config.edn"}
  :eval-in-leiningen true)
