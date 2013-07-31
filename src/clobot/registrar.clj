(ns clobot.registrar
  (:require [clobot.plugins.beer30 :as beer30]
            [clobot.plugins.echo :as echo]
            [clobot.plugins.tell :as tell]
            [clobot.plugins.hackernews :as hn]))

(def plugins (atom {}))

(defn register-plugin [name functions]
  (swap! plugins #(conj % [name functions])))

(defn register-plugins []
  (register-plugin "hackernews" hn/hn-functions)
  (register-plugin "beer30" beer30/beer30-functions)
  (register-plugin "echo" echo/echo-functions)
  (register-plugin "tell" tell/tell-functions))