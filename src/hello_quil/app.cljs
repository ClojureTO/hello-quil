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
  ; Set frame rate to 60 frames per second.
  (q/frame-rate 60)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; the player paddles and the ball
  (let[mid-x     (/ width 2)
       mid-y     (/ height 2)
       initial-y (- mid-x (/ paddle-height 2))]
    {:player1 {:y initial-y}
     :player2 {:y initial-y}
     :keys #{}
     :ball {:x (- mid-x (/ ball-diameter 2))
            :y mid-y
            :dx (v)
            :dy (v)}}))

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

(defn down [y]
  (if (< y (- height paddle-height)) (+ y 10) y))

(defn up [y]
  (if (> y 0) (- y 10) y))

(defn update-player-positions [state]
  (reduce
   (fn [state key]
     (case key
       :a    (update-in state [:player2 :y] up)
       :z    (update-in state [:player2 :y] down)
       :up   (update-in state [:player1 :y] up)
       :down (update-in state [:player1 :y] down)
       state))
   state (:keys state)))

(defn update-state [state]
  (-> state
      update-player-positions
      (update-in
       [:ball]
       (fn [{:keys [x y dx dy]}]
         (if (and (< x width) (> x 0))
           {:x (+ x dx)
            :y (+ y dy)
            :dx dx
            :dy dy})))))

(defn key-press [state {:keys [key]}]
  (if (some #{key} [:a :z :up :down])
    (update-in state [:keys] conj key)
    state))

(defn key-release [state]
  (assoc state :keys #{}))

(defn draw-state [{:keys [ball player1 player2]}]
  ; Clear the sketch by filling it with black color.
  (q/background 0)
  ; Set foreground color.
  (q/fill 255 255 255)
  ; draw the player paddles and the ball
  (draw-paddles player1 player2)
  (draw-ball ball))
