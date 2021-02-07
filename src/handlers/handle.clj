(ns handlers.handle
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-param]
            [persistence.db :as db]
            [kafka.producer :as kp])
  (:import java.util.UUID))

(def conn (db/open-connection))

(defn normalize-data [data]
  (flatten data))

(defn list-transactions [request]
  (let [data-db (db/get-all-transactions conn)
        trx-list (normalize-data data-db)]
    (http/json-response trx-list)))

(defn add-transaction-id [trx]
  (assoc trx "id" (UUID/randomUUID)))

(defn create-transaction [request]
  (let [body (:json-params request)
        trx (add-transaction-id body)]
    (println "Dumping the body request")
    (println trx)
    (kp/produce trx)
    (-> trx
        http/json-response
        (assoc :status 201))))

