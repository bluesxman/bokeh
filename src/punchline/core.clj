(ns punchline.core
  (:require [quil.core :as q]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(def default-style {})

(defn raw->table
  "Convert raw vectors to a table."
  [raw-rows]
  (let [cols (subvec (raw-rows 0) 1 (count (raw-rows 0)))
        row-names (vec (map #(get % 0) (rest raw-rows)))
        parse (fn [rr]
                (vec (map #(read-string %) (rest rr))))
        data (vec (map parse (rest raw-rows)))]
    {:columns cols
     :rows    row-names
     :data    data}))

(defn csv->table
  "Convert a csv file to a table."
  [csv-file]
  (with-open [in-file (io/reader csv-file)]
    (raw->table (csv/read-csv in-file))))

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

(defn color->rgb
  [color]
  (if (keyword? color)
    (case color
      :red [255 150 150]
      :blue [150 150 255]
      :green [150 255 150]
      :orange [250 150 0]
      :yellow [255 215 0]
      :white [255 255 255]
      :black [0 0 0])
    (case (get color 0)
      :rbg (rest color)
      :gray (repeat 3 (last color)))))

(defn todo
  [k v])

(defn error
  [& xs]
  (apply println (interpose " " xs)))

(defn to-screen
  [v]
  (map #(* 400 %) v))

(defn init-setting
  [k v]
  (case k
    :ratio (todo k v)
    :data-to-space (todo k v)
    :margin (apply q/translate (to-screen v))
    :background (apply q/background (color->rgb v))
    :stroke (apply q/stroke (color->rgb v))
    :fill (apply q/fill (color->rgb v))
    :font (q/text-font (apply q/create-font v))
    :else (error "Unknown setting: " k v)))

(def style
  {:ratio         [16 9]
   :data-to-space [2.0 4.0]
   :margin        [0.025 0.025]
   :background    [:gray 220]
   :stroke        [:gray 150]
   :fill          [:gray 150]
   :font          ["Georgia" 16]
   })

(def table
  {:columns ["Speed" "Range" "Cost"]
   :rows    ["A" "B" "C" "D"]
   :data    [[21 310 67584]
             [6 373 56837]
             [18 280 35000]
             [19 221 54000]]})

(def directives
  [[:color [:row "c"] :orange]
   [:vline [:column "speed"] 16]
   [:bold [:cell "b" "range"]]
   [:background [:header-top] [:rbg 220 250 220]]
   [:background :table [:gray 240]]
   ;[:italic]
   ;[:underline]
   [:show-values [:column "cost"] [:gray 200]]
   [:sort "speed" "cost"]])

(defn font-size
  [settings]
  (get-in settings [:font 1]))

(defn render-tlens
  [style directives table]
  (let [settings (into default-style style)
        canvas-color (color->rgb (settings :background))
        col-width 110
        col-header-height (font-size settings)
        row-header-width 50
        row-height 32
        row-spacing 10]

    ;; clear frame
    (apply q/background canvas-color)

    ;; TODO: move settings init to setup.  Loading font is slow and shouldn't be in draw
    ;; initialize settings
    (doseq [s settings ]
     (apply init-setting s))

    ;; table backgrounds

    ;; column headers
    (q/translate 0 col-header-height)
    (q/push-matrix)
    (q/translate row-header-width 0)
    (doseq [col (table :columns)]
      (q/text col 0 0)
      (q/translate col-width 0))
    (q/pop-matrix)

    ;; row headers
    (q/translate 0 (+ (/ row-spacing 2) (* 2 col-header-height)))
    (q/push-matrix)
    (doseq [r (table :rows)]
      (q/text r 0 0)
      (q/translate 0 row-height))
    (q/pop-matrix)

    ;; push row pop

    ))

(defn draw2
  []
  (render-tlens style directives table))

(defn setup []
  (q/frame-rate 1)                                          ;; Set framerate to 1 FPS
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

  (let [vmargin 20
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
    (q/scale 1.75)
    (doseq [col (range (dec col-count))]
      (q/fill 0)
      (q/text (get-in table [:columns col]) (+ hspace col-width (xoffset col)) (- vmargin 10))
      (q/fill 150))
    (doseq [row (range row-count)
            col (range col-count)]
      (when (= row 2)
        (q/stroke 255 215 0)
        (q/fill 255 215 0))
      (if (zero? col)
        (do
          (q/fill 0)
          (q/text (get-in tlens [row 0]) 100 (+ 10 (yoffset row)))
          (q/fill 150))
        (q/rect (xoffset col) (yoffset row) (get-in tlens [row col]) row-height))
      (when (= row 2)
        (q/stroke 150)
        (q/fill 150)))
    (q/stroke 100)
    (let [x (+ 150 60)]
      (q/line x 20 x 90))))

(defn export-svg
  [table]
  ;http://www.ricardmarxer.com/geomerative/
  ;https://forum.processing.org/one/topic/export-svg.html
  )

