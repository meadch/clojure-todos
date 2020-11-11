(ns todos.api
  (:require [ajax.core :refer [GET POST]]
            [todos.state]))

(defn handler [response]
  (.log js/console response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn create-todo! [msg]
  (POST "/api/todos" {:params {:msg msg} :handler (fn [r] (println r))}))

(defn fetch-todos [email]
  (GET "/api/todos" {:params {:email email} :handler (fn [response]
                                                       (reset! todos.state/items response))}))