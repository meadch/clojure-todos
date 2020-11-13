(ns todos.state
  (:require [reagent.core :as r]))

(defonce items (r/atom []))

(defn reset-items! []
  (reset! items []))