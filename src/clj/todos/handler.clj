(ns todos.handler
  (:require
   [reitit.ring :as reitit-ring]
   [hiccup.page :refer [include-js include-css html5]]
   [muuntaja.core :as m]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :refer [parameters-middleware]]
   [todos.db]
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

(defn create-todo [req] 
  (let [email (:email (:body-params req))
        new-item (todos.db/add-item email (:item (:body-params req)))]
    {:status 200 
   :body new-item}))

(defn fetch-todos [req] 
  {:status 200 
   :body (todos.db/get-items-by-email (get (:query-params req) "email"))})

(def router-config
  {:data {:muuntaja m/instance
          :middleware [parameters-middleware
                       muuntaja/format-middleware]}})

(def app
  (reitit-ring/ring-handler
   (reitit-ring/router
    [["/" {:get {:handler index-handler}}]
     ["/dashboard/:email" {:get {:handler index-handler}}]
     ["/api"
      ["/todos" {:post {:handler create-todo} :get {:handler fetch-todos}}]]
     ]
    router-config)
   (reitit-ring/routes
    (reitit-ring/create-resource-handler {:path "/" :root "/public"}))))