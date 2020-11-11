(ns todos.db)

(defonce items (atom {}))

(defn now []
  (java.time.LocalDateTime/now))

(defn make-uuid []
  (-> (java.util.UUID/randomUUID) str))

(defn add-item [email item]
  (let [owner (keyword email)
        id (make-uuid)
        created-at (str (now))]
    (swap! items (fn [items]
                   (assoc-in items [id] (merge item {:owner owner :created-at created-at :concluded-at nil}))))))

(defn get-items-by-email [email]
  (filter #(= (keyword email) (:owner %)) (vals @items)))

(comment
  (add-item "meadch@gmail.com" {:text "Do a thing"})
  (get-items-by-email "bob@gmail.com")
  )