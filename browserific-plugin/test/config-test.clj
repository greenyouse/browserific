(ns config-test
  (:require [clojure.java.io :as f]
            [clojure.java.shell :as sh])
  (:use [leiningen.browserific-plugin.config]
        [clojure.test]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; fixtures

(defmacro with-private-fns [[ns fns] & tests]
  "Refers private fns from ns and runs tests in context."
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] fns)
     ~@tests))

(with-private-fns [leiningen.browserific-plugin.config [name-get]]
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

(sh/sh "mkdir" "-p" (str "resources/mobile/" )
           "resources/extension/firefox"
           "resources/extension/chrome"
           "resources/extension/opera"
           "resources/extension/safari")

(safari-config)

(sh/sh "rm" "-r" "resources")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; tests

(deftest extension-config-test []
  ())

(deftest mobile-config-test []
  (mobile-config "ios")
  (is (= "config.xml\n" ((sh/sh "ls"
                                "resources/mobile/browserific-plugin") :out))))

(deftest build-mobile-config-test
  "This tests for mobile apps"
  []
  (build-config)
  (is (= "config.xml\n" ((sh/sh "ls"
                                "resources/mobile/browserific-plugin") :out))))

(deftest build-chome-config-test
  "This tests for chrome extensions"
  []
  (chrome-config)
  (is (= "manifest.json\n" ((sh/sh "ls"
                                   "resources/extension/chrome")
                    :out))))

(deftest build-firefox-config-test
  "This tests for firefox extensions"
  []
  (firefox-config)
  (is (= "package.json\n" ((sh/sh "ls"
                                   "resources/extension/firefox")
                    :out))))

(deftest build-opera-config-test
  "This tests for opea extensions"
  []
  (opera-config)
  (is (= "manifest.json\n" ((sh/sh "ls"
                                   "resources/extension/opera")
                    :out))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; try it out

(use-fixtures :each fake-project-fix)

(run-tests)
