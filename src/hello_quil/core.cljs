(ns ^:figwheel-no-load hello-quil.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [hello-quil.app :as app]))

(enable-console-print!)

(q/defsketch hello-quil
  :host "hello-quil"
  :size [app/width app/height]
  ; setup function called only once, during sketch initialization.
  :setup #'app/setup
  ; update-state is called on each iteration before draw-state.
  :update #'app/update-state
  :draw #'app/draw-state
  :key-pressed #'app/key-press
  :key-released #'app/key-release
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.

  :features [:global-key-events]
  :middleware [m/fun-mode])

