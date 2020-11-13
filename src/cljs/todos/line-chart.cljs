(ns todos.line-chart
  (:require
   [cljsjs.react-vis :as rvis]
   [todos.state]))

(def split-into-events #(let [logs (vector {:time (:created-at %) :concluded false})]
                              (if (:concluded-at %)
                                (conj logs {:time (:concluded-at %) :concluded true})
                                logs)))

(def with-unfinished-reducer (fn [res event]
               (let [prev-unfinished (:unfinished res)
                     next-unfinished (if (:concluded event) (dec prev-unfinished) (inc prev-unfinished))]
                 {:unfinished next-unfinished
                  :data (conj (:data res) {:time (:time event) :unfinished next-unfinished})})))

(def map-to-coordinates (fn [item] 
                          {:x (:time item) :y (:unfinished item)}))
                         
(defn parse-items [items]
  (->> items
      (mapcat split-into-events)
      (sort-by :time)
      (reduce with-unfinished-reducer {:unfinished 0 :data []})
      (:data)
      (map map-to-coordinates)))

(def axis-style {
                 :line {:stroke "#333"}
                 :strokeLinecap "square"
                 :ticks {:stroke "#999"}
                 :text {:stroke "none"
                        :fill "#333"
                        :fontSize 8}})


(defn component []
  (fn []
    [:div#line-chart.chart.card
     [:h4 "Unfinished tasks over time"]
     (let [parsed-items (parse-items @todos.state/items)
           items (if (empty? parsed-items) [{:x 0 :y 0}] (conj parsed-items (merge (first parsed-items) { :y 0 })))] 
         [:> rvis/XYPlot
          {:width 400 :height 250 :margin {:left 50 :right 50}}
          [:> rvis/YAxis
           {:tickValues (map :y items)
            :tickFormat int
            :tickSizeInner 0
            :tickSizeOuter 3
            :style axis-style}]
          [:> rvis/XAxis
           {
            :tickValues (map :x items)
            :tickFormat (fn [x] (if (zero? x) "" (.toLocaleDateString (js/Date. x))))
            :tickLabelAngle -20
            :tickSizeInner 0
            :tickSizeOuter 3
            :style axis-style}]
          [:> rvis/LineMarkSeries
           {:data items
            :color "#e47320"
            :style {:fill "none"}}]])]))