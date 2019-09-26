(ns understanding-core-async.Lesson-Problem-with-Reducer-Function)


(defn -map [f col]
  (reduce
    (fn [acc v]
      (conj acc (f v)))
    []
    col))

(defn -filter [f col]
  (reduce
    (fn [acc v]
      (if (f v)
        (conj acc v)
        acc))
    []
    col))

(defn -map [f]
  (fn [rf]
    (fn [acc v]
      (rf acc (f v)))))

(defn -filter [f]
  (fn [rf]
    (fn [acc v]
      (if (f v)
        (rf acc v)
        acc))))

(def inc-xf (comp (-map inc)
                  (-filter even?)))

(reduce (inc-xf conj) [] [1 2 3 4])


(-filter even? [1 2 3 4])