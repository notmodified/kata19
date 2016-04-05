(ns kata19.core
  (:require [clj-fuzzy.metrics :as m])
  (:require [clojure.zip :as z])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(def words (clojure.string/split-lines (slurp "./wordlist.txt")))

(defn valid-step [from to]
  (= 1 (m/levenshtein from to)))

(defn find-next [word wlist]
  (filter #(valid-step word %) wlist))

(defn remove-word [word wlist]
  (remove (fn [x] (= word x)) wlist))

;; borrowed from https://gist.github.com/stathissideris/1397681b9c63f09c6992
(defn tree-seq-path
  "Like core's tree-seq but returns a lazy sequence of vectors of the
  paths of the nodes in a tree, via a depth-first walk. It optionally
  applies node-fn to each node before adding it to the path. branch?
  must be a fn of one arg that returns true if passed a node that can
  have children (but may not).  children must be a fn of one arg that
  returns a sequence of the children. Will only be called on nodes for
  which branch? returns true. Root is the root node of the tree."
  [branch? children root & [node-fn]]
  (let [node-fn (or node-fn identity)
        walk (fn walk [path node]
               (let [new-path (conj path (node-fn node))]
                 (lazy-seq
                   (cons new-path
                         (when (branch? node)
                           (mapcat (partial walk new-path) (children node)))))))]
    (walk [] root)))

(defn children [parent wlist]
  (find-next parent wlist))

(defn node [item to wlist]
  (let [l (remove-word item wlist)]
    {:item item
;;     :wlist l
;;     :to to
     :children (if (not (= item to)) (map #(node % to l) (children item l)) (list))}))

(def p clojure.pprint/pprint)
(def three-words (filter #(= 3 (count %)) words))
;;(p (filter #(= "cat" (last %)) (tree-seq-path :children :children (node "cog" "cat" three-words) :item)))


