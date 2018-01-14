(ns core.queries.reduce-task-edited)

(defn reduce-task-edited [acc current]
  (let [task-id (:task/id current)
        task-description (:task/description current)]
    (assoc-in acc [task-id :description] task-description)))
