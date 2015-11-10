(ns menas.bs.bs
  (:require [menas.db.core :refer :all]))

(defn get-menas
  "Gets latest menas from db"
  []
  (find-menas))

(defn create-mena
  "Creates a new mena"
  [mena]
  (println "About to create mena " mena)
  (insert-mena mena)
  (println "Mena persisted")
  )
