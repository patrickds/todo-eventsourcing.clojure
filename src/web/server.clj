(ns web.server
  (:require [compojure.core :refer :all]
            [compojure.core :refer [GET defroutes]]
            [ring.adapter.jetty :as jetty]
            [web.param-parser :refer [parse-params]]))

(defroutes handler
           (GET "/" [] "Hi pretty"))

(defn -main [& args]
  (println args)
  (let [options (parse-params args)]
    (jetty/run-jetty handler {:port (:port options)
                              :join? (:block-thread? options)})))
