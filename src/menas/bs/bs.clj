(ns menas.bs.bs
  (:require
    [menas.db.core :refer :all]
    [clj-time.core :as t]
    )
  (:import [org.joda.time DateTimeZone]
           [org.joda.time DateTime])
  )

(DateTimeZone/setDefault DateTimeZone/UTC)

(def stored-tokens (atom {}))

(defn set-token
  [token session]
  (reset! stored-tokens (assoc @stored-tokens token session)))

(defn still-valid
  [date]
  (t/after? (t/plus date (t/weeks 1)) (t/now)))

(defn validate-token
  [token]
  (println "Current sessions are " @stored-tokens)
  (let [session (@stored-tokens token)]
    (if (nil? session)
      (throw (Exception. "Unauthorized. Not logged in yet")))
    (if (still-valid (session :timestamp))
      session
      (throw (Exception. "Unauthorized. Session no longer valid"))))
  )

(defn generate-token
  []
  (let [token (crypto.random/hex 32)]
    (println "Generated token is " token)
    token)
  )

(defn init-session
  [user]
  (let [session {:user user :timestamp (t/now)}
        token (generate-token)]
    (set-token token session)
    token))

(defn validate-user
  [alias pwd]
  (println
    "Current sessions are " @stored-tokens)
  (let [user-found (find-user alias pwd)]
    (println "User found for given alias and password " user-found)
    (if (not= 1 (count user-found))
      (do
        (println "Login failed")
        false)
      (do
        (println "Login correct")
        (init-session (first user-found))))))

(defn get-menas
  "Gets latest menas from db"
  []
  (find-menas))

(defn fill-mena
  [mena token]
  (let [alias (((validate-token token) :user) :alias)]
    (println "Found alias is " alias)
    (assoc
      (assoc mena :userId alias)
      :createdOn (t/now)))
  )

(defn create-mena
  "Creates a new mena"
  [mena token]
  (println "About to create mena " mena)
  (insert-mena (fill-mena mena token))
  (println "Mena persisted"))
