(ns usecase.delete-done-tasks
  (:require [failjure.core :as f]
            [usecase.delete-task :as delete-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn- task-completed? [task]
  (= (:status task) :completed))

(defn execute! [store clock-now]
  (let [state (list-all-tasks/execute store)
        done-tasks (filter task-completed? state)
        delete-task (partial delete-task/execute! store clock-now)]
    (->> state
         (filter task-completed?)
         (map :id)
         (map delete-task)
         (doall))))
