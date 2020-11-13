(ns todos.db
  (:require [clj-time.core :as t]
            [clj-time.coerce :as c]))

(defonce items (atom {}))

(defn now [] (c/to-long (t/now)))

(defn make-uuid []
  (-> (java.util.UUID/randomUUID) str))

(defn add-item [email item]
  (let [owner (keyword email)
        id (make-uuid)
        created-at (now)
        new-item (merge item {:id id :owner owner :created-at created-at :concluded-at nil})]
    (swap! items (fn [items]
                   (assoc-in items [id] new-item)))
    new-item))

(defn update-item [id item]
  (swap! items (fn [items]
                 (let [current-item (get items id)]
                   (assoc-in items [id] (merge current-item item)))))
  (get @items id))

(defn delete-item [id]
  (swap! items dissoc id))

(defn get-items-by-email [email]
  (filter #(= (keyword email) (:owner %)) (vals @items)))

(comment
  (add-item "meadch@gmail.com" {:text "Do a thing"})
  (reset! items {})
  @items

  ; seed items created over time
  (doseq [i (range 1 14)]
    (let [item (add-item "seed@test.com" {:text (str "item: " i)})
          created-at (c/from-long (:created-at item))]
      (update-item
       (:id item)
       (merge item {:created-at (c/to-long (t/minus created-at (t/days i)))}))))
  )