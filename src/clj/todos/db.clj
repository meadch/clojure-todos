(ns todos.db)

(defonce items (atom (sorted-map)))

(defn add-item [email item]
  (let [email (keyword email)]
    (swap! items (fn [items]
                   (assoc-in items [email] (conj (email items) item))))))

(defn get-items-by-email [email]
  (let [email (keyword email)]
    (email @items)))