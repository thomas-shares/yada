;; Copyright © 2015, JUXT LTD.

(ns yada.collection-resource-test
  (:require
   [clojure.test :refer :all]
   [clojure.tools.logging :refer :all]
   [clojure.pprint :refer [pprint]]
   [ring.mock.request :as mock]
   [clojure.java.io :as io]
   [ring.util.time :refer (parse-date format-date)]
   [yada.representation :as rep]
   [juxt.iota :refer (given)]
   [yada.test.util :refer (to-string)]
   [yada.yada :refer [resource]]))

;; Collections can be resources too, we should test them

(deftest map-resource-test
  (testing "map"
    (let [handler (resource {:name "Frank"})
          request (mock/request :get "/")
          response @(handler request)
          last-modified (some-> response :headers (get "last-modified") parse-date)]

      (is last-modified)
      (is (instance? java.util.Date last-modified))

      (given response
        :status := 200
        :headers :⊃ {"content-type" "application/edn"
                     "content-length" 16}
        :body :instanceof java.nio.HeapByteBuffer)

      (let [request (merge (mock/request :get "/")
                           {:headers {"if-modified-since" (format-date last-modified)}})
            response @(handler request)]
        (given response
          :status := 304)))))

;; For tests we need to round-up to seconds given that HTTP formats are to the nearest second
