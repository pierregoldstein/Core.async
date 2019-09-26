(ns understanding-core-async.Lesson-How-Alt-Handlers-Interact-With-Channels
  (:require [clojure.core.async :refer [chan alts!!]]))

(alts!!)