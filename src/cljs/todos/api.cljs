(ns todos.api
  (:require [ajax.core :refer [GET POST]]
            [todos.state]
            [reagent.session :as session]))

(defn handler [response]
  (.log js/console response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))


(defn handle-create-success [new-todo]
  (swap! todos.state/items conj @todos.state/items new-todo))

(defn create-todo! [text]
  (POST "/api/todos" {:params {:item {:text text} :email (session/get :email)} :handler handle-create-success}))

(defn fetch-todos []
  (GET "/api/todos" {:params {:email (session/get :email)} :handler (fn [response]
                                                       (reset! todos.state/items response))}))