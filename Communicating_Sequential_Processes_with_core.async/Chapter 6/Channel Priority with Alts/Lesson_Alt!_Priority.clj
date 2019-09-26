(ns understanding-core-async.Lesson-Alt!-Priority
  (:require [clojure.core.async :refer [thread alt!! alts!! <!! >!! <! >! go chan]]))


(let [c1 (chan 1)
      c2 (chan 1)]
  (>!! c2 43)
  (thread
    (let [[v c] (alts!! [c1 c2]
                        :priority true)]
      (println "Value: " v)
      (println "Chan 1?" (= c1 c))
      (println "Chan 2?" (= c2 c)))))