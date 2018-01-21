(defproject todo-eventsourcing.clojure "0.1.0-SNAPSHOT"
            :description "A simple todo system using event sourcing techniques"
            :url "https://github.com/patrickds/todo-eventsourcing.clojure"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.9.0"]]
            :profiles {:dev {:dependencies [[midje "1.9.1"]
                                            [failjure "1.2.0"]
                                            [clj-http "3.7.0"]
                                            [ring/ring-core "1.6.3"]
                                            [ring/ring-jetty-adapter "1.6.3"]
                                            [ring/ring-json "0.4.0"]
                                            [compojure "1.6.0"]
                                            [cheshire "5.8.0"]]
                             :plugins [[lein-ring "0.12.1"]
                                       [lein-midje "3.2.1"]
                                       [lein-cljfmt "0.5.7"]
                                       [jonase/eastwood "0.2.5"]
                                       [lein-cloverage "1.0.10"]
                                       [lein-kibit "0.1.5"]]}}
            :ring {:handler web.server/server})
