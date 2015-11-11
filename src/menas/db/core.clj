(ns menas.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [monger.joda-time :refer :all]
              [environ.core :refer [env]]

              [menas.domain.core :refer :all]
              )
  )


(defonce db (atom nil))

(defn connect! []
  ;; Tries to get the Mongo URI from the environment variable
  (println "About to connect")
  (reset! db (-> (System/getenv "MONGOLAB_URI") mg/connect-via-uri :db))
  (println "Right after connect"))

(defn disconnect! []
  (when-let [conn @db]
    (mg/disconnect conn)
    (reset! db nil)))

(defn find-menas
  []
  (map #(dissoc % :_id)
       (mc/find-maps @db "mena" {})))

(defn find-user
  [alias pwd]
  (mc/find-maps @db "user" {:alias alias :pwd pwd}))

(defn insert-mena
  [mena]
  (mc/insert @db "mena" mena)
  )
