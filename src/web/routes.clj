(ns web.routes
  (:require [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]
            [failjure.core :as f]
            [web.controller :as controller]))

(defn my-routes [store]
  (routes
   (GET "/list-tasks" request (controller/list-tasks store request))
   (POST "/create-task" request (controller/create-task! store request))
   (POST "/do-task/:task-id" request (controller/do-task! store request))
   (POST "/undo-task/:task-id" request (controller/undo-task! store request))
   (POST "/edit-task/:task-id" request (controller/edit-task! store request))
   (POST "/delete-task/:task-id" request (controller/delete-task! store request))
   (POST "/delete-done-tasks" request (controller/delete-done-tasks! store request))
   (route/not-found "Not found")))
