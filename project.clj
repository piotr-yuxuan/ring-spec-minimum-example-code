(defproject minimum-example-code "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cheshire "5.7.1"]
                 [org.clojure/clojure "1.9.0"]
                 [metosin/compojure-api "2.0.0-alpha26"]
                 [ring/ring-mock "0.3.2"]
                 [ring "1.6.1"]
                 [metosin/spec-tools "0.7.2"]]
  :main ^:skip-aot minimum-example-code.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
