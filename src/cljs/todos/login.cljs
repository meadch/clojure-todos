(ns todos.login
  (:require
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
                  :on-click (fn []
                              (let [email @val]
                                (reset! val "")
                                (accountant/navigate! (str "/dashboard/" email))))}]]))))