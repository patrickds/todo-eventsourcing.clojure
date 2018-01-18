(ns usecase.clear-done-tasks
  (:require [failjure.core :as f]
            [core.commands.clear-task :refer :all]
            [usecase.clear-task :as clear-task]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn- task-completed? [task]
  (= (:status task) :completed))

(defn execute! [store clock-now]
  (let [state (list-all-tasks/execute store)
        done-tasks (filter task-completed? state)
        clear-task (partial clear-task/execute! store clock-now)]
    (f/ok->> state
             (filter task-completed?)
             (map :id)
             (map clear-task)
             (doall))))
