(ns bokeh.main
  (:require [quil.core :as q]
            [bokeh.core :as bokeh]))

(q/defsketch example                                        ;; Define a new sketch named example
             :title "Oh so many grey circles"               ;; Set the title of the sketch
             :settings #(q/smooth 8)                        ;; Turn on anti-aliasing
             :setup bokeh/setup                                   ;; Specify the setup fn
             :draw bokeh/draw2                                   ;; Specify the draw fn
             :size [960 480])                               ;; You struggle to beat the golden ratio