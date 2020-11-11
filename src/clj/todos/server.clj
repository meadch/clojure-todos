(ns todos.server
    (:require
     [todos.handler :refer [app]]
     [config.core :refer [env]]
     [ring.adapter.jetty :refer [run-jetty]])
    (:gen-class))

(defonce server (atom nil)) ; for REPL development

(defn -main [& args]
  (let [port (or (env :port) 3000)]
    (reset! server (run-jetty #'app {:port port :join? false}))))


(comment
  (-main)
  )
