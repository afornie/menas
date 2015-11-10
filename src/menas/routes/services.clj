(ns menas.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [environ.core :refer [env]]
            [monger.joda-time :refer :all]

            [menas.bs.bs :refer :all]
            )
  (:import [org.joda.time DateTimeZone]
           [org.joda.time DateTime])
  )

(s/defschema Category (s/enum :opinion :event :support))

(s/defschema Mena {
                   (s/optional-key :id) s/Any
                   :title String
                   :body String
                   :tags [String]
                   :categories [Category]
                   (s/optional-key :userId) String
                   (s/optional-key :createdOn) org.joda.time.DateTime
                   })

(defn validate-token
  "Validates request token is correct"
  [token]
  (println "Received token" token)
  (println "Expected token" (System/getenv "VALID_TOKEN"))
  (if (= token (System/getenv "VALID_TOKEN"))
    (println "Token validation successful")
    (do
      (println "Token validation failed")
      (throw (Exception. "Authentication failed")))))

(defn fill-mena
  [mena]
  (DateTimeZone/setDefault DateTimeZone/UTC)
  (assoc
    (assoc mena :userId "server-user")

    :createdOn (new DateTime)))

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  ;JSON docs available at the /swagger.json route
  (swagger-docs
    {:info {:title "Sample api"}})
  (context* "/api" []
            (GET* "/menas" []
                  :return       [Mena]
                  :summary      "List of Menas"
                  (let [result (get-menas)]
                    (println "Result is " result)
                    (ok result)))

            (POST* "/menas" []
                  :summary      "Upload a new Mena"
                  :body     [mena Mena]
                  :query-params [token :- s/Str]
                  (do
                    (validate-token token)
                    (let [mena (fill-mena mena)]
                      (create-mena mena))
                    (ok "ok")))
            ))
