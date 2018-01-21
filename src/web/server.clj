(ns web.server
  (:require [compojure.core :refer :all]
            [compojure.core :refer [GET defroutes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [core.clock :refer :all]
            [failjure.core :as f]
            [event-store.in-memory :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.undo-task :as undo-task]
            [usecase.edit-task :as edit-task]
            [usecase.delete-task :as delete-task]
            [usecase.list-all-tasks :as list-tasks]
            [web.param-parser :refer [parse-params]]))

(def store (->InMemoryStore (atom '())))

(defn create-task! [description]
  (create-task/execute! store clock-now description))

(defn do-task! [task-id]
  (do-task/execute! store clock-now task-id))

(defn undo-task! [task-id]
  (undo-task/execute! store clock-now task-id))

(defn edit-task! [task-id description]
  (edit-task/execute! store clock-now task-id description))

(defn delete-task! [task-id]
  (delete-task/execute! store clock-now task-id))

(defn list-tasks []
  (list-tasks/execute store))

(defn not-found-response [result]
  {:status 404
   :headers {}
   :body (f/message result)})

(defn ok-response []
  {:status 200
   :headers {}
   :body {}})

(defroutes handler
           (GET "/list-tasks" [] (list-tasks))

           (POST "/create-task" request
                 (let [description (get-in request [:body :description])
                       task-id (create-task! description)]
                   (str task-id)))

           (POST "/do-task/:task-id" [task-id]
                 (let [uuid (java.util.UUID/fromString task-id)
                       result (do-task! uuid)]
                   (if (f/failed? result)
                     (not-found-response result)
                     (str result))))

           (POST "/undo-task/:task-id" [task-id]
                 (let [uuid (java.util.UUID/fromString task-id)
                       result (undo-task! uuid)]
                   (if (f/failed? result)
                     (not-found-response result)
                     (str result))))

           (POST "/edit-task/:task-id" request
                 (let [task-id (get-in request [:params :task-id])
                       uuid (java.util.UUID/fromString task-id)
                       description (get-in request [:body :description])
                       result (edit-task! uuid description)]
                   (if (f/failed? result)
                     (not-found-response result)
                     (str result))))

           (POST "/delete-task/:task-id" [task-id]
                 (let [uuid (java.util.UUID/fromString task-id)
                       result (delete-task! uuid)]
                   (if (f/failed? result)
                     (not-found-response result)
                     (ok-response)))))

(def server
  (-> handler
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))

(defn -main [& args]
  (let [options (parse-params args)]
    (jetty/run-jetty server {:port (:port options)
                             :join? (:block-thread? options)})))
