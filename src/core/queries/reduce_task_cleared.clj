(ns core.queries.reduce-task-cleared)

(defn reduce-task-cleared [acc current]
  (let [task-id (:task/id current)
        task-description (:task/description current)]
    (dissoc acc task-id)))
