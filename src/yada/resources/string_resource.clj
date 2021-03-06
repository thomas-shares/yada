;; Copyright © 2015, JUXT LTD.

(ns yada.resources.string-resource
  (:require
   [clj-time.core :refer (now)]
   [clj-time.coerce :refer (to-date)]
   [yada.charset :as charset]
   [yada.protocols :as p]
   [yada.methods :refer [Get Options]]
   [yada.util :refer (md5-hash)]))

(defrecord StringResource [s last-modified]

  p/Properties
  (properties [_]
    {:representations
     [{ ;; Without attempting to actually parse it (which isn't completely
       ;; impossible) we're not able to guess the media-type of this
       ;; string, so we return text/plain.
       :media-type "text/plain"
       :charset charset/platform-charsets}]})
  (properties [_ ctx]
    {:last-modified last-modified
     :version s})

  Get
  (GET [_ _] s))

(extend-protocol p/ResourceCoercion
  String
  (as-resource [s]
    (->StringResource s (to-date (now)))))
