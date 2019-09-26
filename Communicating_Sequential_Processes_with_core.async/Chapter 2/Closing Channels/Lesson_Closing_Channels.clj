(ns understanding-core-async.Lesson-Closing-Channels
  (:require [clojure.core.async :refer [chan <!! >!!
                                        close!]]))

(let [c (chan 2)]
  @(future
    (dotimes [x 2]
      (>!! c x))
    (close! c)
    (println "Closed."))
  (future
    (loop []
      (when-some [v (<!! c)]
        (println "Got: " v)
        (recur)))
    (println "Exiting")))
