(ns todos.api
  (:require [ajax.core :refer [GET POST]]))

(defn create-todo! [msg]
  (POST "/api/todos" {:params {:msg msg} :handler (fn [r] (println r))}))

(defn login! [email success]
  (POST "/api/login" {:params {:email email} :handler success}))