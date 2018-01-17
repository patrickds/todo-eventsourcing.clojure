(ns core.commands.clear-task)

(defn- clear-task-event [clock-now id]
  {:type :task-cleared
   :created-at (clock-now)
   :task/id id})

(defn clear-task-command [clock-now id]
  (clear-task-event clock-now id))
