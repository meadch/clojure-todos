(ns todos.login
  (:require
   [reagent.core :as r]
   [accountant.core :as accountant]
   [clojure.string]))

(defn login-input [value]
  [:input {:type "text"
           :value @value
           :placeholder "email address"
           :on-change #(reset! value (-> % .-target .-value clojure.string/trim))}])

(defn component []
  (fn []
    (let [val (r/atom "")]
      (fn []
        [:section#login.card
         [login-input val]
         [:button { :on-click (fn []
                              (let [email @val]
                                (if-not (clojure.string/blank? email)
                                  (do
                                    (reset! val "")
                                    (accountant/navigate! (str "/dashboard/" email))))))} "Login" ]]))))