(ns browserific.parser
  "Parser for processing Feature Expressions"
  (:require [instaparse.core :as insta]
            [clojure.string :as st]
            [browserific.helpers.utils :as u]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io])
  (:import java.io.File))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Tokenization

(defn- sexp-wrap
  "Tokenizes a collection"
  [coll]
  (let [token "\u6D3B\u6CC9"]
    (str token coll token)))
(comment (println (sexp-wrap "(+ 1 (- 2 (reduce #(+ 1 %) [1 2 3])))")))

;; TODO: add a ns option for delimiting a whole file for output
(defn- sexp-check
  "Parses an s-exp, eliding if there is no feature expression present."
  [coll loc]
  (let [rcoll (read-string coll)]
    (if (vector? rcoll)
      (if (some #(= % (first rcoll)) ['!+ '!-])
        (str (first rcoll)
             (second rcoll)
             (sexp-wrap (nth rcoll 2)))
        (if (some #(= (or '!+ '!-) %) rcoll)
          (throw (Error. (str "Browserific Error: Feature Expression out of position in file " loc ", move expression to beginning of s-exp .")))))
      (sexp-wrap rcoll))))
(comment
  (println (sexp-check "[!+ [firefox woot] (+ 1 1)]" "/some/file"))
  (println (sexp-check "[!- [firefox android d] (+ 1 1)]" "/some/file"))
  (sexp-check "[(+ 1 1) !+ [firefox woot]]" "/some/file")
  (println (sexp-check "(+ 1 1)" "/some/file")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Parser

(def ^:private parser
  (insta/parser
   "
<EXPR> = FE* <SPACE*> | SEXP* <SPACE*>
FE = SPACE* Pred <SPACE*> Platforms <SPACE*> SEXP
<Pred> = Or | Neg
Or = '!+'
Neg = '!-'
<Platforms> = <'['> Env <']'>
<Env> = Browser <SPACE*> Env* | Desktop <SPACE*> Env* |  Mobile <SPACE*> Env* |
        Meta <SPACE*> Env*
Browser = 'chrome' | 'firefox' | 'opera' | 'safari'
Desktop = Linux | OSX | 'windows'
Linux = 'linux32' | 'linux64' | 'linux'
OSX = 'osx32' | 'osx64' | 'osx'
Mobile = 'amazon-fire' | 'android' | 'blackberry' | 'firefox-os' | 'ios' |
         'ubuntu' | 'wp7' | 'wp8' | 'tizen' | 'webos'
(* Denotes all browsers, all mobile, or all desktop *)
Meta = 'b' | 'm' | 'd' | 'mobile' | 'dekstop' | 'browser'

(* let's just skip parsing cljs and leave that to the actual compiler :) *)
SEXP = #'\u6D3B\u6CC9.*?\u6D3B\u6CC9'
<SPACE> = <#'[ \t\n,]+'>
"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;  Transform fns

(defn- transform-meta [node]
  (case node
    "b" (:browsers u/systems)
    "browser" (:browsers u/systems)
    "m" (:mobile u/systems)
    "mobile" (:mobile u/systems)
    "d" (:desktop u/systems)
    "desktop" (:desktop u/systems)
    "linux" ["linux32" "linux64"]
    "osx" ["osx32" "osx64"]
    node))

(def ^:private transform
  {:FE (comp vector)
   :Neg identity
   :Or identity
   :Browser #(vector (identity %))
   :Desktop #(transform-meta %)
   :Linux #(transform-meta %)
   :OSX #(transform-meta %)
   :Mobile #(transform-meta %)
   :Meta #(transform-meta %)
   :SEXP #(-> %
              (st/replace-first
               #"\u6D3B\u6CC9" "")
              (st/replace
               #"\u6D3B\u6CC9" ""))})

(defn- post-transform [expr]
  (let [pred (first expr)
        plats (butlast (rest expr))
        sexp (last expr)
        p (set (flatten plats))]
    (cond
     (= "!+" pred) p
     (= "!-" pred) (reduce disj (set u/platforms) p))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Parse + io fns

(defn- post-parse [a] a
  (-> (reduce conj [] (first a))
      (vector (second a))))

(defn- parse [input loc]
  (let [intermed (-> input
                     (sexp-check loc)
                     (parser))
        done (->> intermed
                  (insta/transform transform)
                  (first))]
    (cond
     ;; normal sexp
     (string? done) [u/platforms done]
     ;; parse error
     (insta/failure? intermed) (throw (Error. (str "Browserific Error: Invalid expression in file: " loc)))
     ;; browserific features expression
     (vector? done) (let [pred (first done)
                          plats (post-transform done)
                          sexp (last done)]
                      (post-parse [plats sexp])))))
(comment (parse "(+ 1 1)" "woot")
         (parse "[!+ [osx d firefox m] (+ 1 1)]" "woot")
         (parse "[!- [linux32 firefox m] (+ 1 1)]" "woot")
         (parse "[!+ [ios firefox-os] (+ 1 1)]" "woot")
         (parse "[!- [mobile ios android safari] (+ 1 1)]" "woot"))

(defn- write-files [expr filename]
  (letfn [(process [fes plat loc]
            (let [fname (u/sub-file-location (str loc))
                  dest (str "intermediate/" plat "/" fname)
                  contents (reduce (fn [acc fe]
                                     (if (some #(= plat %) (first fe))
                                       (str acc "\n\n" (second fe))
                                       acc))
                                   "" fes)]
              (if (not= "" contents)
                (do
                  (io/make-parents dest)
                  (spit dest contents)))))]
    (doseq [plat #{"chrome" "firefox" "opera" "safari" "ubuntu" "wp7" "wp8"
                   "amazon-fire" "android" "blackberry" "firefox-os" "ios"
                   "tizen" "webos" "linux32" "linux64" "osx32" "osx64" "windows"}]
      (process expr plat filename))))

(defn get-file
  "Opens a file, parses its contents, and then passes the
  output to write-files."
  [filename]
  (with-open
      [rdr (java.io.PushbackReader. (io/reader filename))]
    (loop [c (read rdr false :end) acc []]
      (if (= :end c)
        (write-files acc filename)
        (recur (read rdr false :end) (conj acc (parse (str c) filename)))))))

(defn parse-files [files]
  (doseq [f files]
    (get-file f)))
(comment (fs/delete-dir "intermediate")
         (parse-files ["test/fake-src/background/fake-input.cljs"]))
