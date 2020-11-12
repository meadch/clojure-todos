(ns todos.line-chart
  (:require
   [cljsjs.react-vis :as rvis]
   [todos.state]))

(def test-data '({:text "foo"
                  :id "a7984057-f2a3-4b1e-b4cb-dffff132abef"
                  :owner :charlie
                  :created-at 1605145782373
                  :concluded-at nil}
                 {:text "bar"
                  :id "bac646c7-5529-4822-8781-9a8d613dde3e"
                  :owner :charlie
                  :created-at 1605145785655
                  :concluded-at nil}
                 {:text "bizz"
                  :id "1ad382eb-4ee2-4471-9356-5a365b487cd5"
                  :owner :charlie
                  :created-at 1605145804660
                  :concluded-at 1605145830882}
                 {:text "buzz"

                  :id "11038c5b-e424-4cf2-91c6-830d8e6af1fc"
                  :owner :charlie
                  :created-at 1605145808137
                  :concluded-at 1605145833158}))

(.toLocaleDateString (js/Date. 1605145833158))
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
    (map-indexed (fn [i item] {:x (:time item) :y (:unfinished item)}) (:data unfinished-series))))

(def axis-style {:line {:stroke "#333"}
                 :ticks {:stroke "#999"}
                 :text {:stroke "none"
                        :fill "#333"}})

(defn component []
  (fn []
    [:div#line-chart.chart
     [:h2 "Line Chart"]
     [:> rvis/XYPlot
      {:width 250 :height 225}
      [:> rvis/XAxis {:style axis-style :tick-format (fn [x] (.toLocaleDateString (js/Date. x)))}]
      [:> rvis/YAxis {:style axis-style :y-domain [0,5]}]
      [:> rvis/LineSeries
       {:data (parse-items @todos.state/items)
        :color "#e47320"
        :strokeWidth 5
        :style {:fill "none"}}]]]))
