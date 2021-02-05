(ns transactions.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [jsonista.core :as json]
            [kafka.producer :as kp]
            [persistence.db :as db])
  (:import java.util.UUID))

(def conn (db/open-connection))

(defn normalize-data [data]
  (->> data
       flatten
       json/write-value-as-string))

(defn greet [req]
  {:status 200
   :body "Hello PayGo!"
   :headers {"Content-Type" "text/plain"}})

(defn list-transactions [req]
  (let [data-db (db/get-all-transactions conn)
        trx-list (normalize-data data-db)]
    {:status 200
     :body trx-list
     :headers {"Content-Type" "application/json"}}))

(defn add-transaction-id [trx]
  (assoc trx "id" (UUID/randomUUID)))

(defn create-transaction [req]
  (handle-dump req)
  (let [body (:body req)
        trx (add-transaction-id body)]
    (println "Dumping the body request")
    (println trx)
    (kp/produce trx)
    {:status  201
     :body    trx
     :headers {"Content-Type" "application/json"}}))

(defroutes routes
  (GET "/" [] greet)
  (GET "/transactions" [] list-transactions)
  (POST "/transactions" [] create-transaction)
  (not-found "Endpoint not found!"))


(def app
  (->> routes
       wrap-json-response
       wrap-json-body))

(defn set-server-port [port]
  {:port (Integer. port)})

(defn -main [port]
  (jetty/run-jetty app (set-server-port port)))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) (set-server-port port)))

;; Running Server
;; (-dev-main 8000)