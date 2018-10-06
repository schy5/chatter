(ns chatter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
            [hiccup.page :as page]))

(defroutes app-routes
  (GET "/" []
       (page/html5
        [:head
         [:title "chatter"]]
        [:body
         [:h1 "Our Chat App"]]))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
