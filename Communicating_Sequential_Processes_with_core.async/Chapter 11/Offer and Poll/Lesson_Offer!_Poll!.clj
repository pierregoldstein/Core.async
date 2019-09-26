(ns understanding-core-async.Lesson-Offer!-Poll!
  (:require [clojure.core.async :refer [offer! poll! chan]]))

(let [c (chan 2)]
  (println (offer! c 42))
  (println (offer! c 41))
  (println (offer! c 43))

  (println (loop [acc []]
             (if-some [v (poll! c)]
                      (recur (conj acc v))
                      acc))))