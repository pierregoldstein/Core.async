(ns understanding-core-async.Lesson-Overview-of-Pipeline-Async
  (:require [org.httpkit.client :as http]
            [clojure.core.async :refer [chan <! >! go put! <!! >!! pipeline-async
                                        close!]]
            [cheshire.core :as cheshire]))

(defn http-get [url c]
  (println url)
  (http/get url (fn [v]
                  (put! c v)
                  (close! c)))
  c)

(let [from (chan 10 (map (fn [name]
                           (str "http://imdbapi.poromenos.org/js/?name=%25"
                                name
                                "%25"))))

      to (chan 10 (map (fn [response]
                         (-> response
                             :body
                             (cheshire/parse-string true)))))]
  (pipeline-async 10 to http-get from)
  (>!! from "Home")
  (println (<!! to)))


