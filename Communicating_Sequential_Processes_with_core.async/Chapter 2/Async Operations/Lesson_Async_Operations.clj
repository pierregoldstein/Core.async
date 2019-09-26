(ns understanding-core-async.Lesson-Async-Operations
  (:require [clojure.core.async :refer [chan put! take!]]))


(let [c (chan)]
  (put! c 42 (fn [v] (println "Sent: " v)))
  (take! c (fn [v] (println "Got: " v))))