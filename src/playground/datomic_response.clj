(ns playground.datomic-response
  (:require [jsonista.core :as json]))

(def db-response
  [[{:db/id 17592186045419, :transaction/id "a8b06b90-b443-466c-96cc-98fdc70d7ef3", :transaction/merchant "Hirota Supermercados", :transaction/cnpj "18.885.355/0001-88", :transaction/price 389.0M}]
   [{:db/id 17592186045421, :transaction/id "9fd76db2-eaff-431d-98d8-c9c012f52963", :transaction/merchant "Drogasil", :transaction/cnpj "45.897.342/0001-23", :transaction/price 44.5M}]
   [{:db/id 17592186045423, :transaction/id "6e4058c7-006e-4ba2-90a0-7199586b181d", :transaction/merchant "Franz Café", :transaction/cnpj "48.797.111/0001-04", :transaction/price 35.2M}]
   [{:db/id 17592186045425, :transaction/id "7d7f2b16-16b8-4b2d-8e10-2f6e67b565f4", :transaction/merchant "Kopenhagen", :transaction/cnpj "99.888.333/0001-07", :transaction/price 35.83M}]])


(defn datomic->json [data]
  (->> data
       flatten
       json/write-value-as-string))


