(ns bokeh.core
  (:require [quil.core :as q]))

(def table
  {:ratio   [16 9]
   :columns ["speed" "range" "cost"]
   :rows    ["a" "b" "c" "d"]
   :data [[21 310 67584]
          [6 373 56837]
          [18 280 35000]
          [19 221 54000]]})

(def style
  {:data-to-space-v 2.0
   :data-to-space-h 4.0
   :margin-v 0.025
   :margin-h 0.025})

;;TODO: consider using idx instead of by name
(defn column
  [table col-name]
  (let [idx (.indexOf (table :columns) col-name)]
    (map #(get % idx) (table :data))))

(defn normalize
  [values]
  (let [mx (apply max values)]
    (map #(/ % mx) values)))

(defn sort-by-col
  [table col-name direction])

(defn setup []
  (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (q/background 200))                 ;; Set the background colour to
                                      ;; a nice shade of grey.

(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [diam (q/random 100)             ;; Set the diameter to a value between 0 and 100
        x    (q/random (q/width))       ;; Set the x coord randomly within the sketch
        y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
    (q/ellipse x y diam diam)))         ;; Draw a circle at x y with the correct diameter

(defn export-svg
  [table]
  ;http://www.ricardmarxer.com/geomerative/
  ;https://forum.processing.org/one/topic/export-svg.html
  )

(defn draw []
  )

;(q/defsketch example                  ;; Define a new sketch named example
;             :title "Oh so many grey circles"    ;; Set the title of the sketch
;             :settings #(q/smooth 2)             ;; Turn on anti-aliasing
;             :setup setup                        ;; Specify the setup fn
;             :draw draw                          ;; Specify the draw fn
;             :size [323 200])                    ;; You struggle to beat the golden ratio