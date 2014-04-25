(ns leiningen.test.browserific.config
  (:require [clojure.java.io :as f]
            [clojure.java.shell :as sh]
            [leiningen.browserific.config :refer :all]
            [clojure.test :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; fixtures

(defmacro with-private-fns [[ns fns] & tests]
  "Refers private fns from ns and runs tests in context."
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] fns)
     ~@tests))

(with-private-fns [leiningen.browserific.config [name-get]]
  (defn fake-project-fix
    "generate a fake project for testing"
    [do-tests]
    (sh/sh "mkdir" "-p" (str "resources/mobile/" (name-get))
           "resources/extension/firefox"
           "resources/extension/chrome"
           "resources/extension/opera"
           "resources/extension/safari")
    (do-tests)
    (sh/sh "rm" "-r" "resources")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; tests

(deftest build-mobile-test
  "Test for mobile config files"
  []
  (with-private-fns [leiningen.browserific.config [mobile-config]]
    (do (mobile-config "ios")
        (is (= "config.xml\n" ((sh/sh "ls"
                                      "resources/mobile/lein-browserific")
                               :out))))))

(deftest build-chrome-test
  "This tests for chrome extensions"
  []
  (with-private-fns [leiningen.browserific.config [chrome-config]]
    (do (chrome-config)
        (is (= "manifest.json\n" ((sh/sh "ls"
                                         "resources/extension/chrome")
                                  :out))))))

(deftest build-firefox-test
  "This tests for firefox extensions"
  []
  (with-private-fns [leiningen.browserific.config [firefox-config]]
    (do (firefox-config)
        (is (= "package.json\n" ((sh/sh "ls"
                                        "resources/extension/firefox")
                                 :out))))))

(deftest build-opera-test
  "This tests for opea extensions"
  []
  (with-private-fns [leiningen.browserific.config [opera-config]]
    (do (opera-config)
        (is (= "manifest.json\n" ((sh/sh "ls"
                                         "resources/extension/opera")
                                  :out))))))

(deftest build-safari-test
  "This tests for opea extensions"
  []
  (with-private-fns [leiningen.browserific.config [safari-config]]
    (do (safari-config)
        (is (= "Info.plist\n" ((sh/sh "ls"
                                      "resources/extension/safari")
                               :out))))))

(deftest build-config-test
  "Test for the build-config command"
  []
  (do (build-config)
      (is (= "config.xml\n" ((sh/sh "ls"
                                    "resources/mobile/lein-browserific")
                             :out)))
      (is (= "manifest.json\n" ((sh/sh "ls"
                                       "resources/extension/chrome")
                                :out)))
      (is (= "package.json\n" ((sh/sh "ls"
                                      "resources/extension/firefox")
                               :out)))
      (is (= "manifest.json\n" ((sh/sh "ls"
                                       "resources/extension/opera")
                                :out)))
      (is (= "Info.plist\n" ((sh/sh "ls"
                                    "resources/extension/safari")
                             :out)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; try it out

(use-fixtures :each fake-project-fix)

(run-tests)
