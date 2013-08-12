(defproject clobot "0.1.0-SNAPSHOT"
  :description "Clojure IRC Bot"
  :url "http://example.com/FIXME"
  :main clobot.core
  :plugins [[lein-ring "0.8.6"]]
  :ring {:handler clobot.core/app}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.java.dev.rome/rome "1.0.0"]
                 [clj-http "0.7.4"],
                 [cheshire "5.2.0"],
                 [com.novemberain/monger "1.6.0"]
                 [org.clojure/core.match "0.2.0-rc2"]
                 [irclj "0.4.1"]
                 [compojure "1.1.5"]
                 [org.thnetos/cd-client "0.3.6-SNAPSHOT"]
                 [clojail "1.0.6"]
                 [tentacles "0.2.5"]])