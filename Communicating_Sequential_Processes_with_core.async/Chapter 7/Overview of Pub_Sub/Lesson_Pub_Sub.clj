(ns understanding-core-async.Lesson-Pub-Sub
  (:require [clojure.core.async :refer [close! pub sub <!! >!! thread chan]]))

(let [c (chan)
      p (pub c pos?)
      s1 (chan 10)
      s2 (chan 10)]
  (sub p true s1)
  (sub p false s2)

  (>!! c 42)
  (>!! c -42)
  (>!! c -2)
  (>!! c 2)
  (close! c)

  (thread
    (loop []
      (when-some [v (<!! s1)]
        (println "S1: " v)
        (recur)))
    (println "S1: done"))

  (thread
    (loop []
      (when-some [v (<!! s2)]
        (println "S2: " v)
        (recur)))
    (println "S2: done"))

  )
