(ns clobot.plugins.tell)

(def tell-rx #"tell\s(.*?)\s(.*)")

(defn tell-fn [respond nick channel [[full user msg]]]
  (respond (str user ": " msg)))

(def tell-functions [[tell-rx tell-fn]])