(ns understanding-core-async.Lesson-Understanding-Alt-and-Alts
  (:require [clojure.core.async :refer [thread alt!! alts!! <!! >!! <! >! go chan]]))

(let [c1 (chan 1)
      c2 (chan 1)]
  (>!! c1 44)
  (thread
    (println (alt!! [c1] ([v] [:got v])
                    [[c2 42]] ([v] [:sent v])))


    (comment (let [[v c] (alts!! [c1 [c2 42]])]
               (println "Value: " v)
               (println "Chan 1?" (= c1 c))
               (println "Chan 2?" (= c2 c))))))