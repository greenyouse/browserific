(ns browserific.parser
  "Parser for processing Feature Expressions"
  (:require [instaparse.core :as insta]
            [clojure.string :as st]
            [browserific.helpers.utils :as u]
            [clojure.java.io :as io]))

;;; Parse Files
;; x 1. Finish transform fn
;; x 2. Merge output with platforms and dump result into prefixed files
;; x 3. Add file reading to parse and test with a sparse, example file
;; 4. Write a few tests for these new fns
;; x 5. Rig this to work with the main browserific.clj

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
<Env> = Browser <SPACE*> Env* | Desktop <SPACE*> Env* |  Meta <SPACE*> Env*
Browser = 'chrome' | 'firefox' | 'opera' | 'safari'
Desktop = 'linux' | 'osx' | 'windows'
(* Denotes all browsers, all mobile, or all desktop *)
Meta = 'b' | 'm' | 'd'

(* let's just skip parsing cljs and leave that to the actual compiler :) *)
SEXP = #'\u6D3B\u6CC9.*?\u6D3B\u6CC9'
<SPACE> = <#'[ \t\n,]+'>
"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;  Transform fns

(def ^:private platforms
  `[~@(:browsers u/systems)
    "mobile"
    ~@(:desktop u/systems)])

(defn- transform-meta [node]
  (case node
    "b" (:browsers u/systems)
    "m" ["mobile"]
    "d" (:desktop u/systems)))

(def ^:private transfom
  {:FE (comp vector)
   :Neg identity
   :Or identity
   :Browser #(vector (identity %))
   :Desktop #(vector (identity %))
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
     (= "!-" pred) (reduce disj (set platforms) p))))

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
                  (insta/transform transfom)
                  (first))]
    (cond
     ;; normal sexp
     (string? done) [platforms done]
     ;; parse error
     (insta/failure? intermed) (throw (Error. (str "Browserific Error: Invalid expression in file: " loc)))
     ;; browserific features expression
     (vector? done) (let [pred (first done)
                          plats (post-transform done)
                          sexp (last done)]
                      (post-parse [plats sexp])))))
(comment (parse "(+ 1 1)" "woot")
         (parse "[!- [firefox m] (+ 1 1)]" "woot"))

(defn- write-files [expr filename]
  (letfn [(process [fes plat loc]
            (let [dest (str "intermediate/" plat "/" loc)
                  contents (reduce (fn [acc fe]
                                     (if (some #(= plat %) (first fe))
                                       (str acc "\n\n" (second fe))
                                       acc))
                                   "" fes)]
              (if (not= "" contents)
                (do
                  (io/make-parents dest)
                  (spit dest contents)))))]
    (map #(process expr % filename) ["chrome" "firefox" "opera" "safari"
                                     "mobile" "linux" "osx" "windows"])))

(defn parse-files [files]
  (letfn [(get-file [filename]
            (with-open
                [rdr (java.io.PushbackReader. (io/reader filename))]
              (loop [c (read rdr false :end) acc []]
                (if (= :end c)
                  (write-files acc filename)
                  (recur (read rdr false :end) (conj acc (parse (str c) filename)))))))]
    (map get-file files)))
(comment (fs.core/delete-dir "intermediate")
         (parse-files ["test/fake-input.cljs"]))
