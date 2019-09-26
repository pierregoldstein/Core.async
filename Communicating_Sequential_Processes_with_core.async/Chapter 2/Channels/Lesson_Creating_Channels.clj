(ns understanding-core-async.Lesson-Creating-Channels
  (:require [clojure.core.async :refer [chan <!! >!!]]))

(let [c (chan)]
  (future (>!! c 42))

  (future (>!! c 41))

  (future (println (<!! c)))

  (future (println (<!! c))))