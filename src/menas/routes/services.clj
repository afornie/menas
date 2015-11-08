(ns menas.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [environ.core :refer [env]]

            [menas.bs.bs :refer :all]
            ))

(s/defschema Thingie {:id Long
                      :hot Boolean
                      :tag (s/enum :kikka :kukka)})

(s/defschema Category (s/enum :opinion :event :support))

(s/defschema Mena {
                   (s/optional-key :id) s/Any
                   :title String
                   :body String
                   :tags [String]
                   :categories [Category]
                   (s/optional-key :user-id) String
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
  (assoc mena :user-id "server-user"))

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  ;JSON docs available at the /swagger.json route
  (swagger-docs
    {:info {:title "Sample api"}})
  (context* "/api" []
            :tags ["thingie"]

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
