(ns understanding-core-async.Lesson-Overview-of-Pipeline-and-Pipeline-blocking
  (:require [clojure.core.async :refer [chan <!! >!!] :as async]))


(let [c (chan)
      out (chan)]
  (async/onto-chan c (range 10))

  (async/pipeline-blocking 5 out (map (fn [x]
                               (Thread/sleep 500)
                               (inc x))) c)

  (<!! (async/into [] out)))