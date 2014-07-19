(ns leiningen.test.browserific.config
  (:require [clojure.java.io :as f]
            [clojure.java.shell :as sh]
            [leiningen.browserific.config :refer :all]
            [clojure.test :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; fixtures

(defmacro with-private-fns [[ns fns] & tests]
  "Refers private fns from ns and runs tests in context."
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] fns)
     ~@tests))

(defn fake-project-fix
  "generate a fake project for testing"
  [do-tests]
  (sh/sh "mkdir" "-p"
         "resources/extension/firefox"
         "resources/extension/chrome"
         "resources/extension/opera"
         "resources/extension/safari"
         "resources/mobile"
         "resources/desktop/deploy/linux"
         "resources/desktop/deploy/osx"
         "resources/desktop/deploy/windows")
  (do-tests)
  (sh/sh "rm" "-r" "resources"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; extension tests

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
  "This tests for opera extensions"
  []
  (with-private-fns [leiningen.browserific.config [safari-config]]
    (do (safari-config)
        (is (= "Info.plist\n" ((sh/sh "ls"
                                      "resources/extension/safari")
                               :out))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; mobile tests

(deftest build-mobile-test
  "This tests for mobile"
  []
  (with-private-fns [leiningen.browserific.config [mobile-config]]
    (do (mobile-config)
        (is (= "config.xml\n" ((sh/sh "ls"
                                         "resources/mobile")
                                  :out))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; desktop tests

(deftest build-desktop-test
  "This tests for desktop"
  []
  (with-private-fns [leiningen.browserific.config [desktop-config]]
    (do (desktop-config "linux")
        (is (= "package.json\n" ((sh/sh "ls"
                                         "resources/desktop/deploy/linux")
                                  :out))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; all tests

(deftest build-config-test
  "Test for the build-config command"
  []
  (do (build-configs)
      (is (= "config.xml\n" ((sh/sh "ls"
                                    "resources/mobile")
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
