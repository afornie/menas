(ns menas.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [environ.core :refer [env]]

              [menas.domain.core :refer :all]
              ))


(defonce db (atom nil))

(defn connect! []
  ;; Tries to get the Mongo URI from the environment variable
  (println "About to connect")
  (reset! db (-> "mongodb://localhost:27017/menas01" mg/connect-via-uri :db))
  (println "Right after connect"))

(defn disconnect! []
  (when-let [conn @db]
    (mg/disconnect conn)
    (reset! db nil)))

(comment TODO "Remove this function")
(defn get-support
  []
  (mc/find-one-as-map @db "support" {}))

(defn find-menas
  []
  (map #(dissoc % :_id)
       (mc/find-maps @db "mena" {})))

(defn insert-mena
  [mena]
  (mc/insert @db "mena" mena)
  )
