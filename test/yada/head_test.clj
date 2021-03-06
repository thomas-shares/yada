;; Copyright © 2015, JUXT LTD.

(ns yada.head-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [juxt.iota :refer (given)]
   [ring.mock.request :refer [request]]
   [yada.yada :as yada :refer [yada]]))

(deftest head-test []
  (let [resource "Hello World!"
        handler (yada resource {:media-type "text/plain"
                                         :charset "UTF-8"})
        request (request :head "/")
        response @(handler request)]
    (given response
      :status := 200
      [:headers "content-type"] := "text/plain;charset=utf-8"
      [:headers "content-length"] :? nil? ; see rfc7231.html#section-3.3
      :body :? nil?)))
