(ns core.commands.create-task)

(defn create-task-command [clock-now uuid description]
  {:type :task-created
   :created-at (clock-now)
   :task/id uuid
   :task/description description
   :task/status :active})
