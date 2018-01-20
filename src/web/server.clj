(ns web.server
  (:require [compojure.core :refer :all]
            [compojure.core :refer [GET defroutes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body]]
            [cheshire.core :as json]
            [core.clock :refer :all]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.list-all-tasks :as list-tasks]
            [web.param-parser :refer [parse-params]]))

(def store (->InMemoryStore (atom '())))

(defn create-taska [description]
  (create-task/execute! store clock-now description))

(defn list-tasksa []
  (json/generate-string (list-tasks/execute store)))

(defroutes handler
           (GET "/list-tasks" [] (list-tasksa))
           (POST "/create-task" {body :body}
                 (let [description (:description body)
                       task-id (create-taska description)]
                   (str task-id))))

(def server (wrap-json-body handler {:keywords? true}))

(defn -main [& args]
  (println args)
  (let [options (parse-params args)]
    (jetty/run-jetty server {:port (:port options)
                             :join? (:block-thread? options)})))
