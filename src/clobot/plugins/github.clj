(ns clobot.plugins.github
  (:require [tentacles.repos :as repos]
            [tentacles.events :as events]))


(def gh-user-feed-rx #"^\s*gh\suser-feed\s(.*)")

(defn convert-event [{:keys [type actor repo]}]
  (str "type: " type ", user: " (:login actor) ", repo: " (:name repo)))

(defn gh-user-feed-fn [respond nick channel [[full user]]]
  (dorun 
    (map (comp respond convert-event)
         (take 5 (drop 1 (events/performed-events user {:per-page 5}))))))

(def gh-functions [[gh-user-feed-rx gh-user-feed-fn]])