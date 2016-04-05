(ns kata19.core-test
  (:require [clojure.test :refer :all]
            [kata19.core :refer :all]))

(def p clojure.pprint/pprint)

(def test-words ["cat" "dog" "cot" "cog" "car" "bat" "bar" "bananas"])

(require ['clojure.walk :as 'walk])
(require ['clojure.zip :as 'z])


(p (filter #(= "cat" (last %)) (tree-seq-path :children :children (node "cog" "cat" test-words) :item)))

(deftest valid-step-works
  (testing "when words are one character apart, say ok"
    (is (= true (valid-step "cat" "cot")))
    (is (= true (valid-step "cat" "car")))
    (is (= false (valid-step "caat" "car")))
    (is (= false (valid-step "cat" "rap")))))

(deftest find-next-works
  (testing "we can sift for close words"
    (is (not (some #(= "car" %) (find-next "caat" test-words))))
    (is (some #(= "car" %) (find-next "cat" test-words)))))

;; build a tree of nodes
;; [cat [car []] [cot []]]
