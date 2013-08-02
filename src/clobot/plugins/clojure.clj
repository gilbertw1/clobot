(ns clobot.plugins.clojure
  (:require [clojail.core :as jail]
           [clojail.testers :as tst]))

(def sbox (jail/sandbox tst/secure-tester))

(def clj-eval-rx #"^\s*,(.*)")

(defn try-evaluate [expr]
  (try
    (sbox (jail/safe-read expr))
    (catch Exception e (str "evaluation failed (" e ")"))))

(defn clj-eval-fn [respond nick channel [[full expr]]]
  (respond (try-evaluate expr)))

(def clj-functions [[clj-eval-rx clj-eval-fn]])