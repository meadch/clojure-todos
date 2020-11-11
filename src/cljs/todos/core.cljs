(ns todos.core
  (:require
   [todos.login :as login]
   [todos.dashboard :as dashboard]
   [reagent.dom :as rdom]
   [reagent.session :as session]
   [reitit.frontend :as reitit]
   [accountant.core :as accountant]))

;; -------------------------
;; Route

(def router
  (reitit/router
   [["/" :index]
    ["/dashboard/:email" :dashboard]]))

;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'login/component
    :dashboard #'dashboard/component))

;; -------------------------
;; Page mounting component
(defn current-page []
  (fn []
    (let [page (session/get :current-page)] [page])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (rdom/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            email (:email (:path-params match))]
        (session/put! :current-page (page-for current-page))
        (session/put! :email email)))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))