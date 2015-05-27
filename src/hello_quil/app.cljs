(ns hello-quil.app
  (:require [quil.core :as q :include-macros true]))

(def width 500)
(def height 500)
(def paddle-width 20)
(def paddle-height 100)
(def radius 15)


(defn v []
  ((if (> (rand) 0.5) + -) (inc (rand-int 4))))

(defn setup []
  ; Set frame rate to 60 frames per second.
  (q/frame-rate 60)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; the player paddles and the ball
  (let [mid-x (/ width 2)
        mid-y (/ height 2)
        initial-y (- mid-x (/ paddle-height 2))]
    {:player1 {:y initial-y}
     :player2 {:y initial-y}
     :keys    #{}
     :ball    {:x  (- mid-x radius)
               :y  (- mid-y radius)
               :dx (v)
               :dy (v)}}))

(defn draw-paddle [x y]
  (q/rect x y paddle-width paddle-height))

(defn draw-paddles [player1 player2]
  (draw-paddle 10 (:y player1))
  (draw-paddle (- (q/width) 30) (:y player2)))

(defn draw-ball [{:keys [x y]}]
  (q/ellipse x y (* 2 radius) (* 2 radius)))

(defn down [y]
  (if (< y (- height paddle-height)) (+ y 10) y))

(defn up [y]
  (if (> y 0) (- y 10) y))

(defn update-player-positions [state]
  (reduce
    (fn [state key]
      (case key
        :a (update-in state [:player2 :y] up)
        :z (update-in state [:player2 :y] down)
        :up (update-in state [:player1 :y] up)
        :down (update-in state [:player1 :y] down)
        state))
    state (:keys state)))

(defn update-velocity [v d max]
  (if (or
        (and (pos? v) (> d (- max radius)))
        (and (neg? v) (< d radius)))
    (* -1 v) v))

(defn update-ball-v [state]
  (update-in
    state
    [:ball]
    (fn [{:keys [x y dx dy] :as ball}]
      (assoc ball
        :dx (update-velocity dx x width)
        :dy (update-velocity dy y height)))))

(defn update-ball-xy [state]
  (update-in
    state
    [:ball]
    (fn [{:keys [x y dx dy] :as ball}]
      (assoc ball
        :x (+ x dx)
        :y (+ y dy)))))

(defn update-state [state]
  (-> state
      update-player-positions
      update-ball-v
      update-ball-xy))

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
