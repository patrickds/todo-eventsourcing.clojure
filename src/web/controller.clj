(ns web.controller
  (:require [core.clock :refer :all]
            [usecase.create-task :as create-task]
            [usecase.do-task :as do-task]
            [usecase.undo-task :as undo-task]
            [usecase.edit-task :as edit-task]
            [usecase.delete-task :as delete-task]
            [usecase.delete-done-tasks :as delete-done-tasks]
            [usecase.list-all-tasks :as list-tasks]))

(defn create-task! [store description]
  (create-task/execute! store clock-now description))

(defn do-task! [store task-id]
  (do-task/execute! store clock-now task-id))

(defn undo-task! [store task-id]
  (undo-task/execute! store clock-now task-id))

(defn edit-task! [store task-id description]
  (edit-task/execute! store clock-now task-id description))

(defn delete-task! [store task-id]
  (delete-task/execute! store clock-now task-id))

(defn delete-done-tasks! [store]
  (delete-done-tasks/execute! store clock-now))

(defn list-tasks [store]
  (list-tasks/execute store))
