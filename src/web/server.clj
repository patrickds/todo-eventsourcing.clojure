(ns web.server
  (:require [compojure.core :refer :all]
            [compojure.core :refer [GET defroutes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [core.clock :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.list-all-tasks :as list-tasks]
            [web.param-parser :refer [parse-params]]))

(def store (->InMemoryStore (atom '())))

(defn create-task [description]
  (create-task/execute! store clock-now description))

(defn list-tasks []
  (list-tasks/execute store))

(defroutes handler
           (GET "/list-tasks" [] (list-tasks))
           (POST "/create-task" request
                 (let [description (get-in request [:body :description])
                       task-id (create-task description)]
                   (str task-id))))

(def server
  (-> handler
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))

(defn -main [& args]
  (println args)
  (let [options (parse-params args)]
    (jetty/run-jetty server {:port (:port options)
                             :join? (:block-thread? options)})))
