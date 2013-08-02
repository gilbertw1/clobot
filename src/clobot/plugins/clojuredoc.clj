(ns clobot.plugins.clojuredoc
  (:require [cd-client.core :as cdclient]))

(def cdoc-rx #"cdoc\s(.*)")

(defn cdoc-fn [respond nick channel [[full cfun]]]
  (respond (cdclient/search cfun)))

(def cdoc-functions [[cdoc-rx cdoc-fn]])