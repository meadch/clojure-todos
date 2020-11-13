(ns todos.items
  (:require [todos.state]
            [todos.api :as api]
            [reagent.core :as r]))

(defn new-todo [{:keys [title on-save on-stop]}]
  (let [val (r/atom title)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input {:type "text" :value @val
               :id id :class class :placeholder placeholder
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
      [:button.destroy.glyphicon {:on-click on-destroy} "X"]])

(defn component []
 (let [creating-todo (r/atom false)] 
   (fn []
    [:div#items
     [:header
      [:h4 "Todo Items"]
      (if @creating-todo
        [new-todo {:on-stop #(reset! creating-todo false)
                   :on-save api/create-todo!}] 
        [:input {:type "submit" 
                 :value "Add item"
                 :on-click #(reset! creating-todo true)}])]
     [:ul#todo-list
      (for [todo (sort-by :created-at #(compare %2 %1) @todos.state/items)] 
        [todo-item {
          :key (:id todo)
          :text (:text todo)
          :concluded-at (:concluded-at todo)
          :on-toggle #(api/toggle-concluded (:id todo))
          :on-destroy #(api/destroy-item (:id todo))}])]])))