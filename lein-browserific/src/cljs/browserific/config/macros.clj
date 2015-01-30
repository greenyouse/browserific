(ns browserific.config.macros
  (:require [clojure.string :as s]))

(defn- swap [g name]
  (symbol (s/replace g #"g" name)))

(defn- js-val
  "Gives the JS for getting a value from a DOM node"
  [checkbox item g]
  (if (contains? checkbox item)
    `(.-checked ~(swap g "el"))
    `(.-value ~(swap g "el"))))

(defn- co-reduce
  "Folding for two seqs"
  [f var c1 c2]
  {:pre [(= (count c1) (count c2))]}
  (if (empty? c1)
    var
    (recur f (f var (first c1) (first c2))
      (rest c1) (rest c2))))

(comment (co-reduce #(conj % (+ %2 %3))
           [] [1 2 3 4] [5 6 7 8]))

(defn- render-components
  "Helper that renders the Reagent markup for each item based on their type."
  [item g]
  (case (:type item)
    :name [:div (:label item) [:input {:type "text" :id g}]]
    :checkbox [:div (:label item) [:input {:type "checkbox" :id g}]]
    :select `[:div ~(:label item)
              [:select {:id ~g}
               ~@(reduce #(conj % [:option %2])
                  [] (:options item))]]))

(defn- save-cursor
  "Saves an item to a cursor based on the component's struct."
  [struct items gens]
  (if (= :map struct)
    `[(if (nil? ~'@data)
        (t/add-item #(reset! % %2) ~'data {}))
      (t/add-item #(swap! % assoc %2 %3)
        ~'data ~'v1 ~(co-reduce (fn [m i g]
                                  (assoc m
                                    (keyword (:label i))
                                    (swap g "v")))
                       {} (rest items) (rest gens)))]
    `[(t/add-item #(swap! % conj %2)
        ~'data ~(co-reduce (fn [m i g]
                             (assoc m
                               (keyword (:label i))
                               (swap g "v")))
                  {} items gens))]))

(comment (save-cursor :map [{:type :name :label "some-name"}
                             {:type :name :label "moar"}]
            ["g1" "g2"]))

(defn- rm-cursor
  "Deletes an item from a cursor based on the component's struct."
  [struct]
  (if (= :map struct)
    `(fn [~'k]
       (t/rm-item #(swap! % dissoc %2) ~'data ~'k))
    `(fn [~'item]
       (t/rm-item #(swap! % (fn [~'d] (remove (fn [~'x] (= ~'item ~'x)) ~'d)))
         ~'data))))

(comment (rm-cursor :map))

(defn- render-saved
  "Displays the saved items from a cursor based on the component's struct."
  [struct items]
  (if (= :map struct)
    `[(let [~'k (first ~'item)
            ~'m (second ~'item)]
        ^{:key (gensym)}
        [:div
         [:span
          (str ~(:label (first items)) ~'k
            ~@(reduce #(conj %
                         (str ", " (:label %2)) ": "
                         `(~(keyword (:label %2)) ~'m))
                [] (rest items)))]
         [:button {:on-click #(~'rm ~'k)} "x"]])]
    `[^{:key (gensym)}
      [:div
       [:span
        (str ~(:label (first items))
          ;; FIXME: why is there a wayward colon in the output?
          (~(keyword (:label (first items))) ~'item)
          ~@(reduce #(conj %
                       (str ", " (:label %2)) ": "
                       `(~(keyword (:label %2)) ~'item))
              [] (rest items)))]
       [:button {:on-click #(~'rm ~'item)} "x"]]]))

(comment (render-saved :vec [{:type :name :label "some-name"}
                             {:type :name :label "moar"}]))

;; can dispatch on struct to create multiple types of components, each
;; dispatch should have a special function (like save-cursor)
(defmacro multi-input-template
  "A template for hash-map data that requires multiple inputs."
  [struct items]
  ;; instatiate the gensym variables we'll bind to
  (let [checkboxes# (set (filter #(= :checkbox (:type %)) items))
        js-v# (fn [i# g#] (js-val checkboxes# i# g#))
        gens# (for [i# (range 1 (inc (count items)))]
                (symbol (str "g" i#)))]
    `(fn [{:keys [~'data]}]
       (let [~@(reduce #(conj % %2 `(gensym))
                 [] gens#)
             ~'save (fn []
                      (let [~@(reduce #(conj %
                                         (swap %2 "el")
                                         `(.getElementById js/document ~%2))
                                [] gens#)
                            ~'v1 (keyword ~(js-v# (first items) "g1"))
                            ~@(co-reduce #(conj %
                                            (swap %3 "v")
                                            (js-v# %2 %3))
                                [] (rest items) (rest gens#))]
                        ~@(save-cursor struct items gens#)
                        ~@(co-reduce #(conj %
                                        `(set! ~(js-v# %2 %3) ""))
                            [] items gens#)))
             ~'rm ~(rm-cursor struct)]
         (fn []
           [:div (if-not (empty? ~'@data)
                   (for [~'item ~'@data]
                     ~@(render-saved struct items)))
            [:br]
            [:div ~@(co-reduce #(conj % (render-components %2 %3))
                      [] items gens#)
             [:button {:on-click #(~'save)} "Add"]]])))))

(comment
  ;; returns a fn that renders according to the inputs
  (macroexpand '(multi-input-template  :map
                  [{:type :name :label "something"}
                   {:type :checkbox :label "moar"}]))

  (macroexpand '(multi-input-template :vec
                  [{:type :name :label "some-name"}
                   {:type :name :label "moar"}
                   {:type :select :options ["window" "inline"] :label "disposition"}]))

  ;; pass in a cursor to create a component
  (let [m (multi-input-template :map [{:type :name :label "something"}
                                      {:type :checkbox :label "moar"}])]
    (m {:data (atom [])})))
