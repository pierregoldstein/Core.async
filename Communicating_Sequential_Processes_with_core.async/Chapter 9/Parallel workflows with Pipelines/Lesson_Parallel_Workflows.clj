(ns understanding-core-async.Lesson-Parallel-Workflows
  (:require [clojure.core.async :refer [>!! <!! chan pipeline
                                        pipeline-async
                                        pipeline-blocking
                                        onto-chan]]))


(let [input-jms (chan 1024)
      avatar-images (chan 1024)
      resized-avatars (chan 1024)
      output-jms (chan 1024)]

  (pipeline-async 4 input-jms get-avatar-async avatar-images)
  (pipeline-blocking 8 avatar-images resize-image resized-avatars)
  (pipeline-async 128 resized-avatars write-to-db output-jms))