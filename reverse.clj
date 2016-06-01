(defn enesimo [i seq]
  (if (= i 1)
    (first seq)
    (enesimo (- i 1) (rest seq))))


(defn aux-count [seq count]
  (if (empty? seq)
    count
    (aux-count (rest seq) (+ count 1))))

(defn count [seq]
  (aux-count seq 0))

(defn numeros-enteros []
  (iterate inc 1))


(str (interpose "," ["manzana" "banano" "uva"]))

(def estudiantes [ {:name "Hernan"
                    :promedio 3.0}
                  {:name "Alberto"
                   :promedio 4.5}
                  {:name "Sebastian"
                   :promedio 2.7}
                  {:name "Daniel"
                   :promedio 3.5}
                  {:name "Alonzo"
                   :promedio 1.5}
                  ])

(defn paso-materia [estudiante]
  (> (:promedio estudiante) 3.0))

(filter paso-materia estudiantes)

(take-while (complement #{\a \e \i \o \u}) "strong will continue")

(#{"a" "e" "i" "o" "u"} "p")

(#{"a" "e" "i" "o" "u"} "a")

(complement #{"a" "e" "i" "o" "u"})

((complement #{"a" "e" "i" "o" "u"}) "a")


(every? odd? (take 10 (numeros-enteros)))

(every? odd? (numeros-enteros))


(defn divisible-por-ambos [n a b]
  (if (and (= 0 (mod n a))
           (= 0 (mod n b)))
    n
    false))

(defn divisible-por-7-y-3 [n]
  (divisible-por-ambos n 7 3))

(some divisible-por-7-y-3 (numeros-enteros))

(some divisible-por-7-y-3 (take 10 (numeros-enteros)))


(not-every? #(> (:promedio %) 3.0) estudiantes)
(not-any? #(> (:promedio %) 5.0) estudiantes)


(def reportes-climatologicos [
                              {:ciudad "Medellín"
                               :temp-centigrados 27}
                              {:ciudad "Bogotá"
                               :temp-centigrados 21}
                              {:ciudad "Cartagena"
                               :temp-centigrados 30}
                              {:ciudad "Cali"
                               :temp-centigrados 25}
                              {:ciudad "Barranquilla"
                               :temp-centigrados 31}
                              ])

(defn a-farenheit [celsius]
  (float (+ (/ (* celsius 9) 5) 32))
)

(defn agregar-farenheit [reporte]
  (assoc reporte :temp-farenheit (a-farenheit (:temp-centigrados reporte))))

(map agregar-farenheit reportes-climatologicos)



(defn mayor [reporte-a reporte-b]
  (if (> (:temp-centigrados reporte-a) (:temp-centigrados reporte-b))
    reporte-a
    reporte-b))

(reduce mayor reportes-climatologicos)

(sort-by :temp-centigrados reportes-climatologicos)

(for [word ["dabale" "arroz" "a" "la" "zorra"]]
  (format "<p>%s</p>" word))


(defn formato-parrafo [texto]
  (format "<p>%s</p>" texto))

(map formato-parrafo ["dabale" "arroz" "a" "la" "zorra"])

(for [columna "ABCDEFGH"
      fila (range 1 9)]
  (format "%c%d" columna fila))


(defn par? [n]
  (= 0 (mod n 2)))

(def impar? 
  (complement par?))

(for [n (numeros-enteros)
      :when (impar? n)]
  (* n n))

(take 10 (for [n (numeros-enteros)
        :when (impar? n)]
    (* n n)))

(def reportes-ventas [{:ciudad "Medellin"
                       :total 2500000
                       }
                      {:ciudad "Bogota"
                       :total 3000000
                       }
                      {:ciudad "Cali"
                       :total 1000000
                       }
                      {:ciudad "Cartagena"
                       :total 2000000
                       }
                      ])
(defn suma [totales]
  (reduce + totales))

(defn promedio [totales]
  (/ (suma totales) (count totales)))

(defn estadisticas [totales]
  (map #(% totales) [suma count promedio]))

(estadisticas (map :total reportes-ventas))


## Regexes
(re-seq #"\w+" "dabale arroz a la zorra el abad")

(sort (re-seq #"\w+" "dabale arroz a la zorra el abad"))

(drop 2 (re-seq #"\w+" "dabale arroz a la zorra el abad"))

(map #(.toUpperCase %) (re-seq #"\w+" "dabale arroz a la zorra el abad"))

## Files
(import '(java.io File))

(.listFiles (File. "."))

(seq (.listFiles (File. ".")))

(map #(.getName %) (seq (.listFiles (File. "."))))

## Clojure's built-in function 
(file-seq (File. "."))

(count (file-seq (File. ".")))

##Seq-ing un stream
(use '[clojure.java.io :only (reader)])

(reader "./ejemplo.txt")

(line-seq (reader "./ejemplo.txt"))

(with-open [rdr (reader "./ejemplo.txt")]
  (count (line-seq rdr)))

(with-open [rdr (reader "./ejemplo.txt")]
  (count (filter #(re-find #"\S" %) (line-seq rdr))))


##XML

(use '[clojure.xml :only (parse)])

(parse (File. "./composiciones.xml"))

(xml-seq (parse (File. "./composiciones.xml")))

(first (xml-seq (parse (File. "./composiciones.xml"))))

(:content (first (xml-seq (parse (File. "./composiciones.xml")))))

(:tag (first (xml-seq (parse (File. "./composiciones.xml")))))

(for [nodo (xml-seq (parse (File. "./composiciones.xml")))
      :when (= :composicion (:tag nodo))]
  (:compositor (:attrs nodo)))
