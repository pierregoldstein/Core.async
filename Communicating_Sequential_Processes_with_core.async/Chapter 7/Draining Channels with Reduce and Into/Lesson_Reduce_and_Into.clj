(ns understanding-core-async.Lesson-Reduce-and-Into
  (:require [clojure.core.async :refer [chan >!! <!!] :as async]))


(let [c (chan)]
  (async/onto-chan c (range 10))
  (<!! (async/into #{} c)))