(ns web.routes
  (:require [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [failjure.core :as f]
            [web.controller :as controller]))

(defn not-found-response [result]
  {:status 404
   :headers {}
   :body (f/message result)})

(defn ok-response []
  {:status 200
   :headers {}
   :body {}})

(defn my-routes [store]
  (routes
   (GET "/list-tasks" [] (controller/list-tasks store))

   (POST "/create-task" request
     (let [description (get-in request [:body :description])
           task-id (controller/create-task! store description)]
       (str task-id)))

   (POST "/do-task/:task-id" [task-id]
     (let [uuid (java.util.UUID/fromString task-id)
           result (controller/do-task! store uuid)]
       (if (f/failed? result)
         (not-found-response result)
         (str result))))

   (POST "/undo-task/:task-id" [task-id]
     (let [uuid (java.util.UUID/fromString task-id)
           result (controller/undo-task! store uuid)]
       (if (f/failed? result)
         (not-found-response result)
         (str result))))

   (POST "/edit-task/:task-id" request
     (let [task-id (get-in request [:params :task-id])
           uuid (java.util.UUID/fromString task-id)
           description (get-in request [:body :description])
           result (controller/edit-task! store uuid description)]
       (if (f/failed? result)
         (not-found-response result)
         (str result))))

   (POST "/delete-task/:task-id" [task-id]
     (let [uuid (java.util.UUID/fromString task-id)
           result (controller/delete-task! store uuid)]
       (if (f/failed? result)
         (not-found-response result)
         (ok-response))))

   (POST "/delete-done-tasks" []
     (controller/delete-done-tasks! store)
     (ok-response))

   (route/not-found "Not found")))
