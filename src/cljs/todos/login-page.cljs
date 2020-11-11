(ns todos.login-page
  (:require
   [todos.api :as api]
   [reagent.core :as r]
   [accountant.core :as accountant]))

(defn login-input [value]
  [:input {:type "text"
           :value @value
           :placeholder "email"
           :on-change #(reset! value (-> % .-target .-value))}])

(defn component []
  (fn []
    (let [val (r/atom "")]
      (fn []
        [:div.login
         [:h1 "Login"]
         [login-input val]
         [:input {:type "button" :value "Login"
                  :on-click #(api/login! @val
                                         (fn []
                                           (reset! val "")
                                           (accountant/navigate! "/todos")))}]]))))