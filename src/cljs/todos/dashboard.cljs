(ns todos.dashboard
  (:require [todos.state]
            [todos.api]
            [todos.items]
            [todos.line-chart]
            [todos.pie-chart]))

(defn component []
  (todos.api/fetch-todos)
  (fn []
    [:div
     [:section#analytics
      [todos.pie-chart/component {:items @todos.state/items }]
      [todos.line-chart/component {:items @todos.state/items }]]
     [:section#todos
      [todos.items/component]]]))
