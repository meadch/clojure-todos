(ns todos.dashboard
  (:require [todos.state]
            [todos.api]
            [todos.items]
            [todos.line-chart]
            [todos.pie-chart]
            [accountant.core :as accountant]))

(defn component []
  (todos.api/fetch-todos)
  (fn []
    [:div#dashboard
     [:header#menu [:button#logout {:on-click (fn []
                                                (todos.state/reset-items!)
                                                (accountant/navigate! "/"))} "Log out"]]
     [:section#analytics
      [todos.pie-chart/component {:items @todos.state/items }]
      [todos.line-chart/component {:items @todos.state/items }]]
     [:section#todos
      [todos.items/component]]]))
