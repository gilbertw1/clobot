(ns clobot.plugins.coderwall
  (:require [cheshire.core :as json]
            [clj-http.client :as client]))

(def coderwall-info-rx #"^\s*coderwall\sinfo\s(.*)")
(def coderwall-badges-rx #"^\s*coderwall\sbadges\s(.*)")

(defn cw-user-url [user] 
  (str "https://coderwall.com/" user ".json"))

(defn cw-user-profile [user]
  (let [res (client/get (cw-user-url user))
        jres (json/parse-string (:body res) true)]
    jres))

(defn get-coderwall-info [user]
  (let [profile (cw-user-profile user)]
    (prn (:endorsements profile))
    (str user " has " (:endorsements profile) " endorsements and " (count (:badges profile)) " badges")))

(defn try-get-coderwall-info [user]
  (try
    (get-coderwall-info user)
    (catch Exception e (str "can't load coderwall info for " user))))

(defn coderwall-info-fn [respond nick channel [[full user]]]
  (respond (try-get-coderwall-info user)))

(defn respond-with-coderwall-badges [user respond]
  (let [profile (cw-user-profile user)]
    (prn (:badges profile))
    (doseq [{:keys [name]} (:badges profile)]
      (respond (str user ": " name)))))

(defn try-respond-with-coderwall-badges [user respond]
  (try
    (respond-with-coderwall-badges user respond)
    (catch Exception e (respond 
                         (str "can't load coderwall badges for " user)))))

(defn coderwall-badges-fn [respond nick channel [[full user]]]
  (println "IN CODERWALL")
  (try-respond-with-coderwall-badges user respond))

(def coderwall-functions [[coderwall-info-rx coderwall-info-fn]
                          [coderwall-badges-rx coderwall-badges-fn]])