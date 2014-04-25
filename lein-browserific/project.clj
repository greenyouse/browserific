(defproject lein-browserific "0.1.0-SNAPSHOT"
  :description "Pre-compiler to convert browserific code to API code"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[environ "0.4.0"]
                 [org.clojure/data.xml "0.0.7"]]
  :plugins [[lein-environ "0.4.0"]]

  ;; delete the test config when finished
  :profiles {:dev {:env {:config "test/test-config.edn"}}}

  :eval-in-leiningen true)
