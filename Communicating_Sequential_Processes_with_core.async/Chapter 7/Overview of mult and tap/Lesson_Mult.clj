(ns understanding-core-async.Lesson-Mult
  (:require [clojure.core.async :refer [mult tap <!! >!! thread chan]]))

(let [c (chan 10)
      m (mult c)
      t1 (chan 10)
      t2 (chan 10)]
  (tap m t1)
  (tap m t2)

  (>!! c 42)
  (>!! c 43)

  (thread
    (println "Got from T1: " (<!! t1))
    (println "Got from T1: " (<!! t1)))

  (thread
    (println "Got from T2: " (<!! t2))
    (println "Got from T2: " (<!! t2))))