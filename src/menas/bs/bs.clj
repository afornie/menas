(ns menas.bs.bs
  (:require
    [menas.db.core :refer :all]
    [clj-time.core :as t]
    ))

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

(def stored-tokens (atom {}))

(defn set-token
  [token]
  (reset! stored-tokens (assoc @stored-tokens token (t/now))))

(defn still-valid
  [date]
  (t/after? (t/plus date (t/weeks 1)) (t/now)))

(defn validate-token
  [token]
  (println "Current sessions are " @stored-tokens)
  (if (still-valid (@stored-tokens token))
    :ok
    (throw (Exception. "Unauthorized. Not logged in yet")))
  )

(defn validate-user
  [alias pwd]
  (println
    "Current sessions are " @stored-tokens)
  (let [user-found (find-user alias pwd)]
    (println "User found for given alias and password " user-found)
    (if (empty? user-found)
      (do
        (println "Login failed")
        false)
      (do
        (println "Login correct")
        true))))
