(ns todos.items
  (:require [todos.state]))

(defn component []
  (fn []
    [:div.items
     [:h1 "Items"]
     [:ul#todo-list
      (for [todo @todos.state/items]
        ^{:key (:id todo)} [:p (:text todo)])]]))