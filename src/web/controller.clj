(ns web.controller
  (:require [failjure.core :as f]
            [ring.util.response :refer :all]
            [core.clock :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.undo-task :as undo-task]
            [usecase.edit-task :as edit-task]
            [usecase.delete-task :as delete-task]
            [usecase.delete-done-tasks :as delete-done-tasks]
            [usecase.list-all-tasks :as list-tasks]))

(defn- get-task-id [request]
  (-> request
      :params
      :task-id
      java.util.UUID/fromString))

(defn- failjure-respond [result]
  (if (f/failed? result)
    (not-found (f/message result))
    (str result)))

(defn list-tasks [store request]
  (list-tasks/execute store))

(defn create-task! [store request]
  (let [description (get-in request [:body :description])
        task-id (create-task/execute! store clock-now description)]
    (str task-id)))

(defn do-task! [store request]
  (let [task-id (get-task-id request)
        result (do-task/execute! store clock-now task-id)]
    (failjure-respond result)))

(defn undo-task! [store request]
  (let [task-id (get-task-id request)
        result (undo-task/execute! store clock-now task-id)]
    (failjure-respond result)))

(defn edit-task! [store request]
  (let [task-id (get-task-id request)
        description (get-in request [:body :description])
        result (edit-task/execute! store clock-now task-id description)]
    (failjure-respond result)))

(defn delete-task! [store request]
  (let [task-id (get-task-id request)
        result (delete-task/execute! store clock-now task-id)]
    (failjure-respond result)))

(defn delete-done-tasks! [store request]
  (delete-done-tasks/execute! store clock-now)
  (response {}))
