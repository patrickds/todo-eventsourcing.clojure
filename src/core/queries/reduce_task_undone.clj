(ns core.queries.reduce-task-undone)

(defn reduce-task-undone [acc current]
  (let [task-id (:task/id current)
        task-status (:task/status current)]
    (if (contains? acc task-id)
      (assoc-in acc [task-id :status] task-status)
      acc)))
