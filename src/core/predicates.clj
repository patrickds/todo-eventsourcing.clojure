(ns core.predicates)

(defn task-exist? [state id]
  (some #(= (:id %) id) state))
