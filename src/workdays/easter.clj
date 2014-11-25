(ns workdays.easter
  (:require [clojure.math.numeric-tower :as math]
            [clj-time.core :as t]))

(defn i4-modp
  [i j]
  (let [v (mod i j)]
    (if (neg? v)
      (+ v (math/abs j))
      v)))

;; inspiration sources:
;; * http://www.bitshift.me/calculate-easter-in-clojure/
;; * https://gist.github.com/werand/2387286
(defn sunday-where-easter-falls [year]
  "Calculates the date of easter sunday for which easter falls in the given year."
  (let [golden-year (+ 1 (mod year 19))
        century (+ 1 (math/floor (/ year 100)))
        skipped-leap-years (math/floor (- (/ (* 3 century) 4) 12))
        correction (math/floor (- (/ (+ (* 8 century) 5) 25) 5))
        d (math/floor (- (/ (* 5 year) 4) skipped-leap-years 10))
        epac (let [h (i4-modp (- (+ (* 11 golden-year) 20 correction)
                                 skipped-leap-years) 30)]
               (if (or (and (= h 25) (> golden-year 11)) (= h 24))
                 (inc h) h))
        m (let [t (- 44 epac)]
            (if (< t 21) (+ 30 t) t))
        n (- (+ m 7) (mod (+ d m) 7))
        day (if (> n 31) (- n 31) n)
        month (if (> n 31) 4 3)]
    (t/local-date year (int month) (int day))))
