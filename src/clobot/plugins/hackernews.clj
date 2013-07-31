(ns clobot.plugins.hackernews
  (:require [clobot.lib.rss :as rss]))

(def hn-feed-url "https://news.ycombinator.com/rss")

(def hn-feed-rx #"hn.*")

(defn get-hn-feed []
  (let [hn-feed (rss/parse-feed hn-feed-url)]
    (map :title (take 10 (:entries hn-feed)))))

(defn try-get-hn-feed []
  (try
    (get-hn-feed)
    (catch Exception e ["error getting hacker news :("])))

(defn hn-feed-fn [respond nick channel [[full]]]
  (let [feed (try-get-hn-feed)]
    (doseq [item feed]
      (respond item))))
  

(def hn-functions [[hn-feed-rx hn-feed-fn]])