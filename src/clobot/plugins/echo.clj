(ns clobot.plugins.echo)

(def echo-rx #"echo (.*)")

(defn echo-fn [respond nick channel [[full msg]]]
  (respond msg))

(def echo-functions [[echo-rx echo-fn]])