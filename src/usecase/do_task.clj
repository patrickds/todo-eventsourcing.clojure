(ns usecase.do-task
  (:require [failjure.core :as f]
            [core.event-store :refer :all]
            [core.clock :refer :all]
            [core.commands.do-task :refer :all]
            [usecase.list-all-tasks :as list-all-tasks]))

(defn- do-task! [store state id]
  (f/ok->> (do-task-command clock-now state id)
           (save-event! store)))

(defn execute! [store id]
  (let [state (list-all-tasks/execute store)
        result (do-task! store state id)]
    (if (f/failed? result)
      result
      id)))
