(ns todos.dashboard
  (:require [reagent.session :as session]
            [todos.state]
            [todos.api]))

(reagent.session/get :email)

(defn component []
  (todos.api/fetch-todos (reagent.session/get :email))
  (fn []
    [:span.main
     [:h1 "Dashboard"]
     [:h2 (session/get :email)]
     [:ul (map (fn [item-id]
                 [:li {:name (str "item-" item-id) :key (str "item-" item-id)} item-id])
               @todos.state/items)]]))
