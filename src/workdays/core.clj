(ns workdays.core
  (:require [clojure.math.numeric-tower :as math]
            [clj-time.core :as t]
            [clj-time.predicates :refer [weekend?]]
            [workdays.easter :as easter])
  (:import [org.joda.time LocalDate]))

(def year-relative-non-workdates
  (list [1 1] [6 5] [12 24] [12 25] [12 26] [12 31]))

(def easter-relative-non-workdays
  (list
   -3 ; Skærtorsdag
   -2 ; Langfredag
   1  ; 2. Påskedag
   26 ; Storebededag
   39 ; Kristihimmelfart
   40 ; Kristihimmelfart +1
   50 ; 2. Pinsedag
   ))

(defn fixed-non-workdays
  "Returns a set of date objects which isn't work days in the given year.
These are mostly related to Christmas, and the calculation is based on
a subset of the rules in the D1 regulation."
  [year]
  (reduce (fn [s [month date]]
            (conj s (t/local-date year month date)))
          #{} year-relative-non-workdates))

(defn easter-relative-non-workdates
  "Returns a set of date objects which isn't work days in the given year.
These are  related to Easter, and the calculation is based on a subset
of the rules in the D1 regulation."
  [year]
  (let [easter-sunday (easter/sunday-where-easter-falls year)]
    (reduce #(conj %1 (t/plus easter-sunday (t/days %2)))
            #{} easter-relative-non-workdays)))

(defn workday?
  "Whether a date is a workday based on regulation D1 from Energinet."
  [^LocalDate date]
  (let [year (t/year date)]
    (not (or (weekend? date)
             (some #(= date %) (fixed-non-workdays year))
             (some #(= date %) (easter-relative-non-workdates year))))))

(defn workday-sequence
  "Returns an infinite sequence of workdays from ref-date. Where direction must one of the functions plus or minus from clj-time.core which controls whether the sequences is moving forward or backwards."
  [^LocalDate ref-date direction]
  (->>
   ref-date
   (iterate #(direction % (t/days 1)))
   (rest) ; skip ref-date because first day in sequence must be a whole day (24 hours)
   (filter workday?)))

(defn nth-workday
  "Iterates through workdays from ref-date and returns the first Joda-Time LocalDate object after the nth workday."
  [^LocalDate ref-date ^Integer nth-workday]
  (when-not (instance? org.joda.time.LocalDate ref-date)
    (throw (Exception. "ref-date must be an instance of org.joda.time.LocalDate")))
  (when-not (zero? nth-workday)
    (let [steps (- (math/abs nth-workday) 1)] ; calibrate for nth offset 1 = 0, 2 = 1, 10 = 9 etc, also -1 = 0, -2 = 1, -10 = 9
      (if (pos? nth-workday)
        (t/plus (nth (workday-sequence ref-date t/plus) steps) (t/days 1))
        (nth (workday-sequence ref-date t/minus) steps)))))
