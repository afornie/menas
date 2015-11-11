(ns menas.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [monger.joda-time :refer :all]
            [crypto.random :refer :all]

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

(s/defschema Login {
                   :user String
                   :pwd String
                   })


(defn fill-mena
  [mena]
  (DateTimeZone/setDefault DateTimeZone/UTC)
  (assoc
    (assoc mena :userId "server-user")

    :createdOn (new DateTime)))

(defn generate-token
  []
  (let [token (crypto.random/hex 32)]
    (println "Generated token is " token)
    token)
  )

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  ;JSON docs available at the /swagger.json route
  (swagger-docs
    {:info {:title "Sample api"}})
  (context* "/api" []
    (GET* "/menas" []
          :summary      "List of Menas"
          :return       [Mena]
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

    (POST* "/login" []
           :summary      "Login and get a session token"
           :return       String
           :body [body Login]
           (do
             (if (validate-user (body :user) (body :pwd))
               (let [token (generate-token)]
                 (set-token token)
                 (ok token))
               (unauthorized "Wrong login"))
             ))
    ))
