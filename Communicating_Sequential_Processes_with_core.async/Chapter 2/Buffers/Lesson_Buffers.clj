(ns understanding-core-async.Lesson_Buffers
  (:require [clojure.core.async :refer [chan <!! >!!
                                        sliding-buffer
                                        dropping-buffer]]))

(let [c (chan (sliding-buffer 2))]
  @(future
    (dotimes [x 3]
      (>!! c x)
      (println "Sent: " x))
    (println "done"))
  (future
    (dotimes [x 3]
      (println "Got: " (<!! c)))
    (println "done getting")))
