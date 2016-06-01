(def base-datos-vampiros
  {0 {:le-gusta-la-sangre? false :tiene-pulso? true :nombre "Bruno Díaz"}
   1 {:le-gusta-la-sangre? false :tiene-pulso? true :nombre "Charlie Harper"}
   2 {:le-gusta-la-sangre? false :tiene-pulso? false :nombre "Bender"}
   3 {:le-gusta-la-sangre? true :tiene-pulso? true :nombre "Conde Vlad"}
   4 {:le-gusta-la-sangre? true :tiene-pulso? true :nombre "Alucard"}})


(defn detalle-vampiro [cedula]
  (Thread/sleep 1000)
  (get base-datos-vampiros cedula))

(defn es-vampiro? [registro]
  (and (:le-gusta-la-sangre? registro)
       (not (:tiene-pulso? registro))
       registro))

(defn identificar-vampiro [cedulas]
  (first (filter es-vampiro? (map detalle-vampiro cedulas))))


(time (detalle-vampiro 0))

(time (def detalles-mapeados (map detalle-vampiro (range 0 1000000))))

(time (first detalles-mapeados))

(time (identificar-vampiro (range 0 1000000)))


      
