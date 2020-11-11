(ns todos.handler
  (:require
   [reitit.ring :as reitit-ring]
   [todos.middleware :refer [middleware]]
   [hiccup.page :refer [include-js include-css html5]]
   [config.core :refer [env]]))

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    [:div#app]
    (include-js "/js/app.js")]))

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(defn create-todo [_req] {:status 200 :body "Todo created!"})
(defn login [_req] {:status 200 :body "Logged in!"})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/todos" {:get {:handler index-handler}}]
     ["/api"
      ["/todos" {:post {:handler create-todo}}]
      ["/login" {:post {:handler login}}]]
     ])
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"}))
   {:middleware middleware}))