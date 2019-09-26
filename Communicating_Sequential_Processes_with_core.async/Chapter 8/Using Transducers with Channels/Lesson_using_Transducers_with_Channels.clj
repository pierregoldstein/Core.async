 (ns Lesson-using-Transducers-with-Channels
  (:require [clojure.core.async :refer [chan <!! >!!] :as async]))


 (let [c (chan 3 (mapcat identity))]
  (async/onto-chan c [[1 2 3]
                      [4 5 6]
                      [7 8 9]])
  (<!! (async/into [] c)))