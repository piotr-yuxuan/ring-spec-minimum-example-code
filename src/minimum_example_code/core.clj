(ns minimum-example-code.core
  (:require [cheshire.core :as cheshire]
            [clojure.spec.alpha :as s]
            [compojure.api.sweet :refer [context GET POST resource api]]
            [ring.mock.request :as mock]
            [ring.util.http-response :refer [ok]]
            [spec-tools.spec :as spec])
  (:gen-class))

(defn -main [& args] nil)

(s/def ::a spec/int?)

(def app
  (api {:coercion :spec}
       (context "/application/path" []
                (POST "/endpoint" []
                      :body [numbers (s/keys :req-un [::a])]
                      :return spec/map?
                      (ok {:other-numbers numbers})))))

(-> (assoc (mock/request :post "/application/path/endpoint" {:a 7})
      :content-type "application/json")
    (app)
    :body
    (slurp)
    (cheshire/parse-string true))

(comment
  => {:spec "(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:minimum-example-code.core/a]), :type :map, :keys #{:a}, :keys/req #{:a}})",
      :problems [{:path [], :pred "map?", :val nil, :via [], :in []}],
      :type "compojure.api.exception/request-validation",
      :coercion "spec",
      :value nil,
      :in ["request" "body-params"]})

(def app2
  (api {:coercion :spec}
       (context "/application/path/endpoint" []
                (resource
                  {:coercion :spec
                   :post {:parameters {;; :body isn't called, only :body-params
                                       :body-params (fn always-true [body]
                                                      ;; here body is nil
                                                      true)}
                          :responses {200 {:schema spec/map?}}
                          :handler (fn [{{:keys [a]} :body}]
                                     ;; Here, a is nil
                                     (ok {:other-numbers ((fnil inc 0) a)}))}}))))

(-> (assoc (mock/request :post "/application/path/endpoint" {:a 7})
      :content-type "application/json")
    (app2)
    :body
    slurp
    (cheshire/parse-string true))

(comment
  => {:other-numbers 1})

(def app3
  (api {:coercion :spec}
       (context "/application/path/endpoint" []
                (resource
                  {:coercion :spec
                   :post {:parameters {:body-params spec/any?}
                          :responses {200 {:schema spec/map?}}
                          :handler (fn [{{:keys [a]} :body}]
                                     (ok {:other-numbers ((fnil inc 0) a)}))}}))))

(-> (assoc (mock/request :post "/application/path/endpoint" {:a 7})
      :content-type "application/json")
    (app3)
    :body
    slurp
    (cheshire/parse-string true))

(comment
  => {:other-numbers 1})
