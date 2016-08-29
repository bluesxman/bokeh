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
  (q/background 255))

(def tlens
  [["a" 100 80 100]
   ["d" 90 55 82]
   ["c" 85 75 55]
   ["b" 28 100 85]])

(defn draw []
  (q/stroke 150)
  (q/fill 150)
  (q/background 255)

  (let [vmargin 15
        hmargin 5
        vspace 10
        hspace 30
        row-height 10
        col-width 100
        row-count (count tlens)
        col-count (count (tlens 0))
        total-height (+ vmargin (* row-count row-height) (* vspace (dec row-count)))
        total-width (+ hmargin (* col-count col-width) (* hspace (dec col-count)))
        yoffset (vec (range vmargin total-height (+ vspace row-height)))
        xoffset (vec (range hmargin total-width (+ hspace col-width)))]
    (doseq [col (range (dec col-count))]
      (q/fill 0)
      (q/text (get-in table [:columns col]) (+ hspace col-width (xoffset col)) (- vmargin 5))
      (q/fill 150))
    (doseq [row (range row-count)
            col (range col-count)]
      (when (= row 2)
        (q/stroke 255 150 0)
        (q/fill 255 150 0))
      (if (zero? col)
        (do
          (q/fill 0)
          (q/text (get-in tlens [row 0]) 100 (+ 10 (yoffset row)))
          (q/fill 150))
        (q/rect (xoffset col) (yoffset row) (get-in tlens [row col]) row-height))
      (when (= row 2)
        (q/stroke 150)
        (q/fill 150)))))

(defn export-svg
  [table]
  ;http://www.ricardmarxer.com/geomerative/
  ;https://forum.processing.org/one/topic/export-svg.html
  )

(q/defsketch example                  ;; Define a new sketch named example
             :title "Oh so many grey circles"    ;; Set the title of the sketch
             :settings #(q/smooth 2)             ;; Turn on anti-aliasing
             :setup setup                        ;; Specify the setup fn
             :draw draw                          ;; Specify the draw fn
             :size [640 480])                    ;; You struggle to beat the golden ratio