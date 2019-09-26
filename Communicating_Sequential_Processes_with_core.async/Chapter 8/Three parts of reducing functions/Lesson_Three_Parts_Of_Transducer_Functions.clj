(ns understanding-core-async.Lesson-Three-Parts-Of-Transducer-Functions)

(defn -map [f]
  (fn [rf]
    (fn
      ([] (rf))
      ([acc] (rf acc))
      ([acc v]
       (rf acc (f v))))))

(defn -filter [f]
  (fn [rf]
    (fn
      ([] (rf))
      ([acc] (rf acc))
      ([acc v]
       (if (f v)
         (rf acc v)
         acc)))))

(def inc-xf (comp (-map inc)
                  (-filter even?)))

(defn -conj!
  ([] (transient []))
  ([acc] (persistent! acc))
  ([acc v] (conj! acc v)))

(let [rf (inc-xf -conj!)]
  (rf (reduce rf (rf) [1 2 3 4])))


