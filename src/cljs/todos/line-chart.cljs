(ns todos.line-chart
  (:require
   [cljsjs.react-vis :as rvis]
   [todos.state]))

(defn parse-items [items]
  (let [sorted-by-created-at (sort-by :created-at #(- %1 %2) items)
        events (mapcat (fn [todo]
                         (let [created-log {:time (:created-at todo) :concluded false}
                               concluded-log {:time (:concluded-at todo) :concluded true}]
                           (filter #(not (nil? (:time %))) [created-log concluded-log]))) sorted-by-created-at)
        sorted-by-time (sort-by :time #(- %1 %2) events)
        unfinished-series (reduce (fn [res event]
                                    (let [prev-unfinished (:unfinished res)
                                          next-unfinished (if (:concluded event) (dec prev-unfinished) (inc prev-unfinished))]
                                      {:unfinished next-unfinished
                                       :data (conj (:data res) {:time (:time event) :unfinished next-unfinished})})) {:unfinished 0 :data []} sorted-by-time)]
    (map-indexed (fn [i item] {:x i :y (:unfinished item)}) (:data unfinished-series))))

(def axis-style {:line {:stroke "#333"}})

(defn component []
  (fn []
    [:div#line-chart.chart
     [:h4 "Unfinished tasks over time"]
     [:> rvis/XYPlot
      {:width 250 :height 225}
      [:> rvis/XAxis {:style axis-style :tick-format (fn [_] "")}]
      [:> rvis/YAxis {:style axis-style :tick-format (fn [_] "")}]
      [:> rvis/LineSeries
       {:data (parse-items @todos.state/items)
        :color "#e47320"
        :style {:fill "none"}}]]]))