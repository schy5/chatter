(ns chatter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.page :as page]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [ring.middleware.params :refer [wrap-params]]))
(def chat-messages (atom '()))
      ;(def chat-messages
          ;   (atom   [{:name "blue" :message "hello, world"}
          ;            {:name "red" :message "red is my favorite color"}
              ;        {:name "green" :message "green makes it go faster"}]))
;atom added -An atom is like a box that protects information from being changed in an unsafe way. You simply pass the information into the atom
; "(defroutes app-routes
;     (GET "/" []
;        (page/html5
;         [:head
;          [:title "chatter"]]
;         [:body
;          [:h1 "Our Chat App"]]))
;   (route/not-found "Not Found"))"
(defn update-messages!
"This will update a message list atom"
[messages name new-messages]
(swap! messages conj {:name name :message new-messages}))

(defn generate-message-view
"This generates the HTML for displaying messages"
[messages]
(page/html5
 [:head
  [:title "chatter"]]
 [:body
  [:h1 "Our Chat App"]
  ;[:p (str messages)]])) ;since taking message as html so type is must for message in a ()
  [:p
  (form/form-to
  [:post "/"]
  "Name: " (form/text-field "name")
  "Message: " (form/text-field "msg")
  (form/submit-button "Submit"))]
[:p
 [:table
  (map (fn [m] [:tr [:td (:name m)] [:td (:message m)]]) messages)]]])) ;table form, calling a function syntax- map fn coll

(defroutes app-routes
(GET "/" [] (generate-message-view @chat-messages)) ;Because chat-messages is pointing to the atom, we can't simply map over it in generate-message-view. Now, we have to tell Clojure that we want to generate HTML for the contents of the atom.
;(POST "/" [] (generate-message-view @chat-messages));Reading what's stored in an atom is called "dereferencing" and is represented by the @ character.
;(POST "/" {params :params} (generate-message-view(update-messages! chat-messages (get params "name")(get params"msg"))))
(POST "/" {params :params}
    (let [name-param (get params "name")
          msg-param (get params "msg")
          new-messages (update-messages! chat-messages name-param msg-param)]
      (generate-message-view new-messages)
      ))
(route/not-found "Not Found"))

;(def app
(def app (wrap-params app-routes)) ;This enables us to have access to the information sent back in our form.
  (wrap-defaults app-routes site-defaults)
