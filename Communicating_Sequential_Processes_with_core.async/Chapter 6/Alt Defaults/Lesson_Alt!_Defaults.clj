(ns understanding-core-async.Lesson-Alt!-Defaults
  (:require [clojure.core.async :refer [thread alt!! alts!! <!! >!! <! >! go chan]]))


(let [c1 (chan 1)
      c2 (chan 1)]
  (thread
    (println "Got: " (alt!! [c1] :c1
                            [c2] :c2
                            :default :the-default))))