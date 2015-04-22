(ns workdays.service
  (:require [workdays.core :refer [nth-workday]]
            [clojure.string :as string]
            [jkkramer.verily :as v]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

;; source: http://www.pelagodesign.com/blog/2009/05/20/iso-8601-date-validation-that-doesnt-suck/
(def iso-8601-pattern
  #"^([\+-]?\d{4}(?!\d{2}\b))((-?)((0[1-9]|1[0-2])(\3([12]\d|0[1-9]|3[01]))?|W([0-4]\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\d|[12]\d{2}|3([0-5]\d|6[1-6])))([T\s]((([01]\d|2[0-3])((:?)[0-5]\d)?|24\:?00)([\.,]\d+(?!:))?)?(\17[0-5]\d([\.,]\d+)?)?([zZ]|([\+-])([01]\d|2[0-3]):?([0-5]\d)?)?)?)?$")

(def validate
  (v/combine
   (v/required [:ref-date :workdays])
   (v/matches iso-8601-pattern :ref-date "must be using a iso 8601 valid format")
   (v/matches #"\-?[0-9]+" :workdays "must be an integer")))

(defn error-msg->str
  [{:keys [keys msg]}]
  (str (string/join ", " (map name keys)) " " msg))

(defn nth-workday-for-web
  "Wraps the nth-workday from core to be web request/response friendly."
  [{:keys [ref-date workdays] :as params}]
  (if-let [errors (not-empty (validate params))]
    {:status 400 :body (str (string/join ", " (map error-msg->str errors)))}
    (str (nth-workday (clj-time.format/parse-local-date ref-date) (Integer. workdays)))))

(defroutes handler
  (GET "/date-subsequent-to-workdays/"
       {params :params}
       (nth-workday-for-web params))
  (route/not-found "Unknown request"))

(def site
  (wrap-defaults handler api-defaults))
