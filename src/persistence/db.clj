(ns persistence.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/transactions")

(defn open-connection []
  (println "Opening DB connection")
  (d/create-database db-uri)
  (d/connect db-uri))

(defn get-all-transactions [conn]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :transaction/id]] (d/db conn)))

