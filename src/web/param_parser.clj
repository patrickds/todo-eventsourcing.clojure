(ns web.param-parser)

(defn get-block-param [options]
  (if (= (:block-thread? options) :false)
    false
    true))

(defn parse-params
  ([] {:port 3000 :block-thread? true})
  ([params]
   (let [options (apply hash-map params)
         port (or (:port options) 3000)
         block-thread? (get-block-param options)]
     {:port port
      :block-thread? block-thread?})))
