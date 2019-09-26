(ns understanding-core-async.Lesson-Introduction-to-Backpressure
  (:require [clojure.core.async :refer [go <! >! <!! chan]]))


(def c (chan 24))

(go
  (loop [i 0]
    (println "Putting: " i)
    (>! c i)
    (recur (inc i))))

(<!! c)