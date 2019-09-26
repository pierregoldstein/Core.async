(ns understanding-core-async.Lesson-Dataflow
  (:require [clojure.data.xml :as xml]
            [clojure.core.async :refer [chan <! >! go put! <!! >!!]]
            [org.httpkit.client :as http]
            [clojure.core.async :as async]
            [clojure.java.io :as jio])
  (:import [javax.imageio ImageIO]
           [java.io InputStream ByteArrayOutputStream]
           (java.awt.image BufferedImage)))

(def url "https://api.flickr.com/services/feeds/photos_public.gne")
(def data (xml/parse (java.io.StringReader.
                       (slurp url))))

(def entries (keep (fn [node]
                     (when (= (:tag node) :entry)
                       (println (map :tag (:content node)))
                       {:url   (:href (:attrs (first (filter #(= "enclosure" (-> % :attrs :rel)) (:content node)))))
                        :title (:content (first (filter #(= :title (:tag %)) (:content node))))}))
                   (:content data)))

(vec entries)

(let [log-c (chan 1024)]
  (go (loop []
        (when-some [v (<! log-c)]
          (println v)
          (recur))))
  (defn log [& itms]
    (>!! log-c (apply str (interpose " " itms)))))


(defn downloader [in out threads]
  (let [f (fn [{:keys [url] :as data} out-chan]
            (log "Downloading " url)
            (http/get url (fn [response]
                            (put! out-chan (assoc data
                                             :body (:body response)))
                            (async/close! out-chan))))]
    (async/pipeline-async threads out f in)))


(defn image-loader [in out threads]
  (let [f (fn [{:keys [^InputStream body url] :as data}]
            (log "Reading..." url)
            (-> data
                (assoc :image (ImageIO/read body))
                (dissoc :body)))]
    (async/pipeline threads out (map f) in)))

(defn resizer [in out threads sizes]
  (let [f (fn [{:keys [^BufferedImage image url] :as data}]
            (mapv (fn [[width height :as size]]
                    (let [baos (ByteArrayOutputStream.)
                          resized-image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
                          g (.createGraphics resized-image)]
                      (log "Resizing" url width height)
                      (.drawImage g image 0 0 width height nil)
                      (.dispose g)
                      (ImageIO/write resized-image "PNG" baos)
                      (assoc data :image (.toByteArray baos)
                                  :size size)))
                  sizes))]
    (async/pipeline threads out (mapcat f) in)))

(defn write-to-file [name data]
  (with-open [w (jio/output-stream name)]
    (.write w data)))

(defn writer [in out threads]
  (let [f (fn [{:keys [image size url] :as data}]
            (let [[width height] size
                  filename (munge (str url "_" width "_" height ".png"))]
              (write-to-file filename image)
              (-> data
                  (dissoc :image)
                  (assoc :filename filename))))]
    (async/pipeline-blocking threads out (map f) in)))

(time
  (let [urls (chan 20)
        image-bytes (chan 20 (remove (comp string? :body)))
        images (chan 20)
        resized (chan 20)
        written (chan 20)]

    (async/onto-chan urls entries)
    (downloader urls image-bytes 20)
    (image-loader image-bytes images 2)
    (resizer images resized 8 [[128 128]
                               [256 256]])
    (writer resized written 1)
    (println (<!! (async/into [] written)))
    (println "Done")))