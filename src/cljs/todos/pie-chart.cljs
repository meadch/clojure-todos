(ns todos.pie-chart
  (:require
   [cljsjs.react-vis :as rvis]
   [todos.state :refer [items]]))

(defn component []
   (fn []
    [:div#line-chart.chart.card
     [:h4 "Complete vs. incomplete tasks"]
     (if-not (empty? @items) [:> rvis/RadialChart 
      {:width 250 :height 225 :showLabels true
       :data 
       (let [
             concluded-count (-> (filter :concluded-at @items) count)
             unconcluded-count (- (count @items) concluded-count)]
         [
        {:angle concluded-count :label "Complete"} 
        {:angle unconcluded-count :label "Incomplete"}])}])]))