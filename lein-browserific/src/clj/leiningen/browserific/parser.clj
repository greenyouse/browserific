(ns leiningen.browserific.parser
  "Parser for processing Feature Expressions"
  (:require [instaparse.core :as insta]
            [clojure.string :as st]
            [leiningen.browserific.config :as conf]
            [clojure.pprint :refer [pprint]]))

;; use sexp-check to tokenize each s-exp so they can be elided later
;; convert all the expressions back after transformation (when dumping back to a cljs file)

;;; Parse Files
;; ~ 1. Finish transform fn
;; 2. Merge output with platforms and dump result into prefixed files
;; ~ 3. Add file reading to parse and test with a sparse, example file
;; 4. Write a few tests for these new fns
;; 5. Rig this to work with the main browserific.clj

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; First Pass (tokenizes sexprs and saves FE)

(defn sexp-wrap
  "Wraps coll in tokens, dropping the first and last parenthesis"
  [coll]
  (let [token "\u6D3B\u6CC9"
        pcoll (-> (str coll)
                  (.substring 1)
                  (st/reverse)
                  (.substring 1)
                  (st/reverse))]
    (str token pcoll token)))
(comment (println (sexp-wrap "(+ 1 (- 2 (reduce #(+ 1 %) [1 2 3])))")))


;; if FE then save FE and tokenize the sexp
;; if FE not in first position throw error
;; else tokenize the sexp without any FE header
;; make a ns parser too for delimiting whole files?
(defn sexp-check
  "Parses an s-exp, eliding if there is no feature expression present."
  [coll]
  (let [rcoll (read-string coll)]
    (if (vector? rcoll)
      (if (some #(= % (first rcoll)) ['!+ '!-])
        (str (first rcoll)
             (second rcoll)
             (sexp-wrap (nthrest rcoll 2)))
        (if (some #(= (or '!+ '!-) %) rcoll)
          (Error. "Browserific Error: Feature Expression out of position, move expression to beginning of s-exp .")))
      (sexp-wrap rcoll))))
(comment
  (println (sexp-check "[!+ [firefox woot] (+ 1 1)]"))
  (println (sexp-check "[!- [firefox android d] (+ 1 1)]"))
  (sexp-check "[(+ 1 1) !+ [firefox woot]]")
  (sexp-check "(+ 1 1)"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Second Pass (transforms only items with FE at head)
(def parser
  (insta/parser
   "
FE = Pred Exp Prog
<Pred> = Or | Neg
Or = '!+'
Neg = '!-'
<Exp> = <'['> Env <']'>
<Env> = (Browser <sp+> Env) | (Mobile <sp+> Env) | (Meta <sp+> Env)
  (Desktop <sp+> Env) | Browser | Mobile | Meta | Desktop
<sp> = ' '
Browser = 'chrome' | 'firefox' | 'opera' | 'safari'
Mobile = 'amazon-fire' | 'android' | 'blackberry' | 'firefox-os' | 'ios' |
   'ubuntu' | 'wp7' | 'wp8' | 'webos' | 'tizen'
Desktop = 'win8' | 'osx' | 'qt'
(* Denotes all envs, all browsers, all mobile, or all desktop *)
Meta = 'all' | 'b' | 'm' | 'd'

(* let's just skip parsing cljs and leave that to the actual compiler :) *)
Prog = #'\u6D3B\u6CC9.*?\u6D3B\u6CC9'
"))

(println "[!-[firefox chrome android d] (+ 1 1)]")

(->> "[!-[firefox chrome android d] (+ 1 1)]"
    (sexp-check)
    (parser)
    (insta/transform transfom))

(def platforms
  `[~@(:browsers conf/systems)
    ~@(:mobile conf/systems)])

;; this should be post-transform
(defn transfom-exp [coll]
  (loop [coll coll [] acc]
    (if-not (seq coll)
      (remove (set acc) platforms)
      (recur (rest coll) ))))

(defn transfom-meta [node]
  (case node
    'all [[:browser ""]]
    'b
    'm
    'd))

(let [l "(+ 1 1) (* 2 2)"]
  (reduce #(conj %1 %2)
          [] (read-string l)))
;; something like this when we get to the
(reduce #(conj %1 (second %2)) [[:browser 23] [:browser 22]])
;; this dictates what output goes into which files
;; filter file outputs using (platforms and values of :Neg or :Or
(def transfom
  {:FE (comp vector)
   :Neg (comp not)
   :Or (comp =)
   :Browser [:browser %]
   :Mobile [:mobile %]
   :Desktop [:desktop %]
   :Meta #(transform-meta %)
   :Prog #(-> %
              (st/replace-first
               #"\u6D3B\u6CC9" "\\(")
              (st/replace
               #"\u6D3B\u6CC9" "\\)"))})


(remove (set ["firefox" "chrome" "ios"]) platforms)
;; FIXME: put file-reading into parse
;; TODO: put output dispatch in parse
(defn parse [input]
  (->> (sexp-check input)
       (str)
       (parser)
       (insta/transform transfom)))


(println
 (count (sexp-check "[!-[firefox chrome android d] (+ 1 1)]")))



(second #{'chrome 'firefox 'opera})

;; this is what a feature expression will look like
#_  [!+ [firefox chrome m]
     (defn woot []
       (+ 1 1))]

;; parse each s-exp from a file, and dump everything into a cljs file
(spit "prefixed/location.cljs"
      (reduce #(str %1 (parse %2))
              [1 2 3 4]))

(let [fe
      [:FE [:Neg '!-]
        [:Browser 'firefox]
        [:Browser 'chrome]
        [:Mobile 'android]
        [:Meta 'd]
        ]]
  (println fe))

;; output
()

[:Prog 活泉+ 1 1活泉]
