(ns todos.items
  (:require [todos.state]
            [todos.api :as api]
            [reagent.core :as r]
            [clojure.string]))

(defn new-todo [{:keys [title on-save on-stop]}]
  (let [val (r/atom title)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn []
      [:input.new {:type "text" :value @val
               :placeholder "What needs to be done?"
               :auto-focus true
               :on-blur save
               :on-change #(reset! val (-> % .-target .-value))
               :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}])))

(defn todo-item [{:keys [concluded-at text on-toggle on-destroy]}]
    [:li.item
      [:input.toggle {:type "checkbox" 
                      :checked (boolean concluded-at)
                      :on-change on-toggle}]
      [:label text]
      [:button.destroy {:on-click on-destroy} "X"]])

(defn component []
 (let [creating-todo (r/atom false)] 
   (fn []
    [:div#items.card
     [:header
      [:h4 "To-Do Items:"]
      (if @creating-todo
        [new-todo {:on-stop #(reset! creating-todo false)
                   :on-save api/create-todo!}] 
        [:button {:on-click #(reset! creating-todo true)} "Add item"])]
     [:ul#todo-list
      (for [todo (sort-by :created-at #(compare %2 %1) @todos.state/items)] 
        [todo-item {
          :key (:id todo)
          :text (:text todo)
          :concluded-at (:concluded-at todo)
          :on-toggle #(api/toggle-concluded (:id todo))
          :on-destroy #(api/destroy-item (:id todo))}])]])))