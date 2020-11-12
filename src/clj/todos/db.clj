(ns todos.db)

(defonce items (atom {}))

(defn now []
  (.getTime (java.util.Date.)))

(defn make-uuid []
  (-> (java.util.UUID/randomUUID) str))

(defn add-item [email item]
  (let [owner (keyword email)
        id (make-uuid)
        created-at (long (now))
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
  (get-items-by-email "bob@gmail.com")
  (reset! items {})
  @items
  )