(ns menas.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY routes]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [menas.rest :refer [rest-api]]
            [menas.html :refer [site]]))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (routes rest-api site) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
