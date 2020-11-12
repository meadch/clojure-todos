(ns todos.api
  (:require [ajax.core :refer [GET POST PUT DELETE]]
            [todos.state]
            [reagent.session :as session]))

; handlers
(defn handler [response]
  (.log js/console response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn handle-create-success [new-todo]
  (swap! todos.state/items conj new-todo))

(defn handle-update-success [updated-todo]
  (swap! todos.state/items #(map (fn [item] (if (= (:id item) (:id updated-todo)) updated-todo item)) %)))

; api interface

(defn create-todo! [text]
  (POST "/api/todos" {:params {:item {:text text} :email (session/get :email)} :handler handle-create-success}))

(defn fetch-todos []
  (GET "/api/todos" {:params {:email (session/get :email)} :handler (fn [response]
                                                       (reset! todos.state/items response))}))

(defn toggle-concluded [id]
  (let [item (first (filter #(= (:id %) id) @todos.state/items))
        concluded-at (if (:concluded-at item) nil (.getTime (js/Date.)))]
    (PUT "/api/todos" {:params {:id id :item { :concluded-at concluded-at }} :handler handle-update-success})))

(defn destroy-item [id]
    (DELETE "/api/todos" {:params {:id id } 
                          :handler (fn [_] 
                                     (swap! todos.state/items #(filter (fn [item] (not= (:id item) id)) %)))}))