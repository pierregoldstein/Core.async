(ns understanding-core-async.Lesson-Merge
  (:refer-clojure :exclude [merge])
  (:require [clojure.core.async :refer [merge >!! <!! chan close!]]))


(let [c1 (chan)
      c2 (chan)
      cm (merge [c1 c2] 10)]
  (>!! c1 1)
  (>!! c1 2)
  (>!! c2 3)
  (>!! c2 4)
  (close! c1)
  (close! c2)

  (loop []
    (when-some [v (<!! cm)]
      (println v)
      (recur)))

  (println "Done"))
