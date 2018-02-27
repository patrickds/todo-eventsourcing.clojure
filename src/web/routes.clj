(ns web.routes
  (:require [compojure.route :as route]
            [compojure.api.sweet :refer :all]
            [failjure.core :as f]
            ; [clojure.spec.alpha :as s]
            ; [spec-tools.spec :as spec]
            [schema.core :as s]
            [web.controller :as controller]))

; (s/def :task/id uuid?)
; (s/def :task/description string?)
; (s/def :task/status keyword?)
; (s/def ::task (s/keys :req [:task/id :task/description :task/status]))
; (s/def ::tasks (s/* ::task))
;
; (s/def ::create-task-input (s/keys :req [:task/description]))

(s/defschema Task
             {:id java.util.UUID
              :description s/Str
              :status s/Keyword})

; (s/valid? :task/description "oi")

(def task {:id (java.util.UUID/randomUUID)
           :description "buy milk"
           :status :active})

; (s/validate [Task] task)
; (s/valid? ::task task)
; (s/valid? ::tasks [task task task])

(defn api-routes [store]
  (api
   {:coercion :schema
    :swagger {:ui "/api-docs"
              :spec "/swagger.json"
              :data {:info {:title "Sample API"
                            :description "Compojure Api example"}
                     :tags [{:name "api", :description "some apis"}]
                     :consumes ["application/json"]
                     :produces ["application/json"]}}}
   ; (context "/api" [] :tags ["api"]

   (GET "/list-tasks" request
        :return [Task]
        :summary "It returns a list of tasks"
        (controller/list-tasks store request))

   (POST "/create-task" request
         :return s/Str
         :body [task {:description s/Str}]
         :summary "It creates a task"
         (clojure.pprint/pprint task)
         "Mah oe")
         ; (controller/create-task! store request))

   (POST "/do-task/:task-id" request (controller/do-task! store request))
   (POST "/undo-task/:task-id" request (controller/undo-task! store request))
   (POST "/edit-task/:task-id" request (controller/edit-task! store request))
   (POST "/delete-task/:task-id" request (controller/delete-task! store request))
   (POST "/delete-done-tasks" request (controller/delete-done-tasks! store request))

   (route/not-found "Not found")))
