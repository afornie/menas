(ns menas.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]))

(s/defschema Thingie {:id Long
                      :hot Boolean
                      :tag (s/enum :kikka :kukka)})

(s/defschema Mena {:id Long
                      :title String
                      :body String})

(defapi service-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  ;JSON docs available at the /swagger.json route
  (swagger-docs
    {:info {:title "Sample api"}})
  (context* "/api" []
            :tags ["thingie"]


            (GET* "/mine" []
                  :return       Thingie
                  :summary      "Just return a Thingie"
                  (ok {:id 123456 :hot true :tag :kikka}))

            (GET* "/menas" []
                  :return       [Mena]
                  :summary      "List of Menas"
                  (ok [{:id 1 :title "Buscando apoyos para esta web" :body "Necesitamos tu ayuda"},
                       {:id 2 :title "Ayuda a que este proyecto trascienda" :body "Tu apoyo es ejemplo para otros"}]))


            (POST* "/mine" []
                   :return      Thingie
                   :body     [thingie Thingie]
                   :summary     "Post and return thingies"
                   (do
                     (println "body was " thingie)
                     (println "hot was " (:hot thingie))
                     (ok {:id 123456 :hot true :tag :kikka})))
            )


  (context* "/entries" []
            :tags ["context*"]
            :summary "summary inherited from context"
            (context* "/all" []
                      (GET* "/:filter" []
                            :path-params [filter :- s/Str]
                            (ok {:name "Ale"
                                 :age 2
                                 :filter filter}))))

  (context* "/context" []
            :tags ["context*"]
            :summary "summary inherited from context"
            (context* "/:kikka" []
                      :path-params [kikka :- s/Str]
                        :query-params [kukka :- s/Str]
                      (GET* "/:kakka" []
                            :path-params [kakka :- s/Str]
                            (ok {:kikka kikka
                                 :kukka kukka
                                 :kakka kakka}))))
  )
