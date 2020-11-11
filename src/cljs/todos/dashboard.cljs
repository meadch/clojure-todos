(ns todos.dashboard
  (:require [reagent.session :as session]
            [todos.state]
            [todos.api]
            [todos.items]
            [todos.line-chart]
            [todos.pie-chart]))

(reagent.session/get :email)

(defn component []
  (todos.api/fetch-todos (reagent.session/get :email))
  (fn []
    [:div
     [:section#analytics
      [todos.pie-chart/component {:items @todos.state/items }]
      [todos.line-chart/component {:items @todos.state/items }]]
     [:section#todos
      [todos.items/component]]]))
