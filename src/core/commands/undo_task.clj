(ns core.commands.undo-task)

(defn undo-task-command [clock-now id]
  {:type :task-undone
   :created-at (clock-now)
   :task/id id
   :task/status :active})
