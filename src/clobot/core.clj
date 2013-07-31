(ns clobot.core
  (:use compojure.core)
  (:require [clobot.registrar :as registrar]
            [clojure.java.io :as io]
            [cheshire.core :as json]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [irclj.core :as ircj]))

(def bots (atom []))

(def resource-path (-> "config.json" io/resource))

(defn read-conf []
  (json/parse-string (slurp resource-path) true))

(defn retrieve-plugin [name]
  (@registrar/plugins name))

(defn respond-fn [irc channel]
  (fn [msg]
    (ircj/send-message irc channel msg)))

(defn dispatch-to-plugins [{:keys [name irc plugins]} nick channel msg]
  (let [cmd-msg (.trim (.replaceAll msg (str name ": ") ""))]
    (doseq [[rx fun] plugins]
      (let [matches (re-seq rx cmd-msg)]
        (if matches
            (future (fun (respond-fn irc channel) nick channel matches)))))))

(defn handle-bot-irc-message [id {:keys [nick channel message] :as irc-msg}]
  (let [bot (first (filter #(= id (:id %)) @bots))]
    (if (.startsWith message (str (:name bot) ": "))
      (dispatch-to-plugins bot nick channel message))))

(defn start-bot [{:keys [id name server port channels plugins] :as bot}]
  (let [irc (ircj/create-irc {:name name :server server :fnmap {:on-message (fn [irc-msg] (handle-bot-irc-message id irc-msg))}})]
    (ircj/connect irc :channels channels)
    (assoc bot 
      :irc irc
      :plugins (apply concat (map retrieve-plugin plugins)))))

(defn load-bot [bot-conf]
  (swap! bots #(conj % (start-bot bot-conf))))

(defn load-bots [conf]
  (pmap load-bot (:bots conf)))

;(defn send-jersh [bot]
;  (ircj/send-message (:irc bot) "#SPARCnet" "joshskimore: jersh"))
;
;(defn send-jershes []
;  (future
;    (while true
;      (Thread/sleep (rand 5000))
;      (if (> (count (@bots 1)))
;        (send-jersh (rand-nth @bots))))))
;
;(defn send-node-bash [bot]
;  (ircj/send-message (:irc bot) "#SPARCnet" (str "jmar777: nodejizz is " (rand-nth ["stupid" "dumb" "evil" "dastardly" "not cool" "super lame" "weak" "horrible" "crappy" "nasty" "poop"]))))
;
;(defn send-node-bashes []
;  (future
;    (while true
;      (Thread/sleep (rand 5000))
;      (if (> (count (@bots 1)))
;        (send-node-bash (rand-nth @bots))))))

(defroutes app-routes
  (GET "/" [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn start []
  (registrar/register-plugins)
  (load-bots (read-conf))
  ;(send-jershes)
  ;(send-node-bashes)
  (handler/site app-routes))

(def app (start))