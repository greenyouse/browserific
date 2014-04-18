(ns utils)


(defmacro with-gensym
  "Binds new variables to gensym to prevent accidental
   variable capture in macros (for places where # won't work well)"
  [syms & body]
  `(let [~@(reduce #(conj %1 %2 '(gensym))
                  [] syms)]
     ~@body))

(defmacro group
  "Groups a coll into lists of a designated size"
  [item gparam]
  (with-gensym [nums]
    `(loop [~'nums (into []~item) ~'acc []]
       (if (empty? ~'nums)
         ~'acc
         (recur (subvec ~'nums ~gparam)
                (conj ~'acc (take ~gparam ~'nums)))))))

(defmacro mac [expr]
  `(pprint (macroexpand '~expr)))

(defmacro mac-1 [expr]
  `(pprint (macroexpand-1 '~expr)))
