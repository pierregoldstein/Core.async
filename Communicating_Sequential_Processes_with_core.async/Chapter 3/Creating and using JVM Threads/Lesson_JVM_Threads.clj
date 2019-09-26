(ns understanding-core-async.Lesson-JVM-Threads
  (:require [clojure.core.async :refer [chan <!! >!!
                                        thread]]))

(<!! (thread
       (let [t1 (thread "Thread 1")
             t2 (thread "Thread 2")]
         [(<!! t1)
          (<!! t2)])))


(let [c (chan)]
  (thread
    (dotimes [x 3]
      (>!! c x)
      (println "Put: " x)))
  (thread
    (dotimes [x 3]
      (println "Take: " (<!! c)))))