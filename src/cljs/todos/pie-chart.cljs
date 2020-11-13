(ns todos.pie-chart
  (:require
   [cljsjs.react-vis :as rvis]
   [todos.state :refer [items]]))

(defn component []
   (fn []
    [:div#line-chart.chart.card
     [:h4 "Complete vs. incomplete tasks"]
     [:> rvis/RadialChart 
      {:width 250 :height 225 :showLabels true :labelsStyle { :font-size 10 }
       :data 
       (let [
             concluded-count (-> (filter :concluded-at @items) count)
             unconcluded-count (- (count @items) concluded-count)]
         [
        {:angle concluded-count :label (if (zero? concluded-count) "" "Complete")} 
        {:angle (if (zero? unconcluded-count) 360 unconcluded-count) :label (if (zero? unconcluded-count) "" "Incomplete")}])}]]))