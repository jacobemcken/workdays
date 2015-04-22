(defproject workdays "1.0.0"
  :description "Lib and service to calculate workdays in the danish energy market (electricity and gas)"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [clj-time "0.8.0"]
                 [jkkramer/verily "0.6.0" :exclusions [org.clojure/clojurescript]]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]]
  :plugins [[lein-ring "0.9.3"]]
  :ring {:handler workdays.service/site})
