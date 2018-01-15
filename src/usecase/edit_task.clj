(ns usecase.edit-task
  (:require [failjure.core :as f]
            [core.clock :refer :all]
            [core.event-store :refer :all]
            [core.commands.edit-task :refer :all]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn- edit-task! [store state id description]
  (f/ok->> (edit-task-command clock-now state id description)
           (save-event! store)))

(defn execute! [store id description]
  (let [state (list-all-tasks/execute store)
        result (edit-task! store state id description)]
    (if (f/failed? result)
      result
      id)))
