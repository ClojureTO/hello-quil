(ns hello-quil.app
  (:require [quil.core :as q :include-macros true]))

(def width 500)
(def height 500)
(def paddle-width 20)
(def paddle-height 100)
(def ball-diameter 30)

(defn v []
  ((if (> (rand) 0.5) + -) (inc (rand-int 4))))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:player1 {:y 0}
   :player2 {:y 0}
   :ball {:x (/ width 2)
          :y (/ height 2)
          :dx (v)
          :dy (v)}})

(defn draw-paddle [x y]
  (q/rect x y paddle-width paddle-height))

(defn draw-paddles [player1 player2]
  (draw-paddle 10 (:y player1))
  (draw-paddle (- (q/width) 30) (:y player2)))

(defn draw-ball [{:keys [x y]}]
  (q/ellipse (+ x (/ ball-diameter 2))
             (+ y (/ ball-diameter 2))
             ball-diameter
             ball-diameter))

(defn update-state [state]
  (update-in state [:ball]
    (fn [{:keys [x y dx dy] :as ball}]
      (if (and (< x width) (> x 0))
        {:x (+ x dx)
         :y (+ y dy)
         :dx dx
         :dy dy}
        ball))))

(defn down [y]
  (if (< y (- height paddle-height)) (+ y 10) y))

(defn up [y]
  (if (> y 0) (- y 10) y))

(defn key-press [state {:keys [key]}]
  (case key
    :a    (update-in state [:player2 :y] up)
    :z    (update-in state [:player2 :y] down)
    :up   (update-in state [:player1 :y] up)
    :down (update-in state [:player1 :y] down)
    state))

(defn draw-state [{:keys [ball player1 player2]}]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 0)
  ; Set circle color.
  (q/fill 255 255 255)
  ; Calculate x and y coordinates of the circle.
  (draw-paddles player1 player2)
  (draw-ball ball))
