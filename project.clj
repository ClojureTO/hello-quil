(defproject hello-quil "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil "2.3.0"]
                 [org.clojure/clojurescript "1.7.170"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-2"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
              :builds [{:source-paths ["src/"]
                        :figwheel true
                        :compiler {:main "hello-quil.core"
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/quil.js"
                                   :output-dir "resources/public/js/out" }}]}
  )
