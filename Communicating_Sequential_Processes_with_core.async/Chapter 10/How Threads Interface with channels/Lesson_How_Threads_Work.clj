(ns understanding-core-async.Lesson-How-Threads-Work
  (:require [clojure.core.async :refer [<!! >!! thread]]))

(thread)

(>!!)