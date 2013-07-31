(ns clobot.plugins.beer30
  (:require [cheshire.core :as json]
            [clj-http.client :as client]))

(def beer30-status-url "https://beer30.sparcedge.com/status")

(def beer30-status-rx #"beer30\sstatus.*")

(defn get-beer30-status []
  (let [res (client/get beer30-status-url)
        resjs (json/parse-string (:body res) true)
        status (:statusType resjs)
        reason (:reason resjs)]
    (cond
      (= status "CAUTION") (str "beer30 is yellow :/ (" reason ")")
      (= status "STOP") (str "beer30 is red :( (" reason ")")
      (= status "GO") (str "beer30 is green :) (" reason ")")
      :else "unknown beer30 status")))

(defn try-get-beer30-status []
  (try
    (get-beer30-status)
    (catch Exception e "beer 30 is down :(")))

(defn beer30-status-fn [respond nick channel [[full]]]
  (respond (try-get-beer30-status)))

(def beer30-functions [[beer30-status-rx beer30-status-fn]])