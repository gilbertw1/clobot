(ns clobot.registrar
  (:require [clobot.plugins.beer30 :as beer30]
            [clobot.plugins.clojuredoc :as cdoc]
            [clobot.plugins.echo :as echo]
            [clobot.plugins.tell :as tell]
            [clobot.plugins.hackernews :as hn]
            [clobot.plugins.github :as gh]
            [clobot.plugins.coderwall :as cw]
            [clobot.plugins.clojure :as clj]))

(def plugins (atom {}))

(defn register-plugin [name functions]
  (swap! plugins #(conj % [name functions])))

(defn register-plugins []
  (register-plugin "echo" echo/echo-functions)
  (register-plugin "tell" tell/tell-functions)
  (register-plugin "clojure" clj/clj-functions)
  (register-plugin "hackernews" hn/hn-functions)
  (register-plugin "clojuredoc" cdoc/cdoc-functions)
  (register-plugin "github" gh/gh-functions)
  (register-plugin "coderwall" cw/coderwall-functions)
  (register-plugin "beer30" beer30/beer30-functions))