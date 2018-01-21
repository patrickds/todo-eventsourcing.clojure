(ns unit.web.param-parser-test
  (:require [midje.sweet :refer :all]
            [web.param-parser :refer :all]))

(fact "It returns passed params"
      (parse-params '(:port 1234 :block-thread? :false)) => {:port 1234
                                                             :block-thread? false})
(fact "It returns default port"
      (parse-params '(:block-thread? :false)) => {:port 3000
                                                  :block-thread? false})
(fact "It returns default block-thread?"
      (parse-params '(:port 1234)) => {:port 1234
                                       :block-thread? true})
(fact "It returns defaults when no params"
      (parse-params) => {:port 3000
                         :block-thread? true})
