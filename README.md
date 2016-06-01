slidenumbers: true

# Sequence by Clojure
### Unificando datos bajo una poderosa abstracción.



###Coljure 2016

---

# Motivación
* Todos los programas que creamos manipulan datos (listas, vectores, mapas, conjuntos y árboles).
* XML es un árbol
* Los resultados de la BD se pueden ver como una lista o vector.
* Las jerarquías de directorios son árboles.
* Los archivos se pueden ver como un gran string o como un arreglo de líneas.

---

# Motivación
* Todas estas estructuras de datos en Clojure se pueden acceder bajo la misma abstracción
* Un seq. (pronunciado **seek**) es una ‘lista’ lógica.
* Clojure no relaciona la secuencia con detalles de implementación (en lisp **con**)
* Las colecciones que se pueden usar como seqs. Se conocen como seq-able (**seek-a-bull**)
* El poder de la libreria seq.  

---

# car cdn cons
En lisp existen tres operaciones básicas para manipular listas: **car**, **cdn** y **cons**.
Clojure cambió los nombres de estas operaciones para hacerlas más sencillas de recordar.

* car: first
* cdn: rest
* cons: cons

---

#first (first coll)
```clojure 
(first "this awesome string")

(first [1 2 3])

(first [])

(first {:name "Bruce" :last_name "Lee"})

(first #{:a :b :c :d})
```

---

#first (first coll)
```clojure 
(first "this awesome string")
=> \t

(first [1 2 3])
=> 1

(first [])
=> nil

(first {:name "Bruce" :last_name "Lee"}) 
=> [:name "Bruce"]

(first #{:a :b :c :d})
=> :c
```

---

#rest (rest coll)
```clojure 
(rest "this awesome string")

(rest [1 2 3])

(rest [])

(rest {:name "Bruce" :last_name "Lee"})

(rest #{:a :b :c :d})
```

---

#rest (rest coll)
```clojure 
(rest "this awesome string")
=> (\h \i \s \space \a \w \e \s \o \m \e \space \s \t \r \i \n \g)

(rest [1 2 3])
=> [2 3]

(rest [])
=> nil

(rest {:name "Bruce" :last_name "Lee"})
=> ([:last_name "Lee"])

(rest #{:a :b :c :d})
=> (:b :d :a)
```

---

#cons (cons x seq)
```clojure 
(cons "new string" "")

(cons [1 2 3] [])

(cons [9] [1 2 3])

(cons [:address "Fake Street 123"] {:name "Bruce" :last_name "Lee"})

(cons :tres #{:tristes :tigres})
```

---

#cons (cons x seq)
```clojure 
(cons "new string" "")
=> ("new string")

(cons [1 2 3] [])
=> ([1 2 3])

(cons 9 [1 2 3])
=> (9 1 2 3)

(cons [:address "Fake Street 123"] {:name "Bruce" :last_name "Lee"})
=> ([:address "Fake Street 123"] [:name "Bruce"] [:last_name "Lee"])

(cons :tres #{:tristes :tigres})
=> (:tres :tristes :tigres)
```

---

#¿Qué podemos hacer con estas funciones básicas?
### Retornenos el enésimo elemento de una lista (naive)

```clojure 
(defn enesimo [i seq]
  (if (= i 1)
    (first seq)
    (enesimo (- i 1) (rest seq))))
    
(enesimo 4 [1 2 3 4])

(enesimo 1 "abcd")

(enesimo 4 #{:the :set :is :here})

(enesimo 1 {:show "Futurama" :chain "Fox"})
```

---

```clojure
(enesimo 4 [1 2 3 4])
=> 4

(enesimo 1 "abcd")
=> a

(enesimo 4 #{:the :set :is :here})
=> :set

(enesimo 1 {:show "Futurama" :chain "Fox"})
=> [:show "Futurama"]
````

---

#Una nota sobre los sets y maps
##Los sets y maps implementan un orden de recorrido pero este depende de los detalles de implementación. 
##Si queremos tener orden en estas secuencias debemos usar los constructores de las versiones ordenadas

```clojure
(first #{:a :b :c :d}) => :c

(first (sorted-set :a :b :c :d)) => :a

(sorted-map :c 1 :b 2 :a 3) => {:a 3, :b 2, :c 1}
```

---

#Addendum
##Además de las tres operaciones básicas sobre seq, podemos utilizar otras dos utilidades

###(conj coll element & elements)
```clojure
(conj [1 2 3] :a :b :c) => [1 2 3 :a :b :c]
(conj '(1 2 3) :a :b :c) => (:c :b :a 1 2 3)
```
###(into to-coll from-coll)
```clojure
(into [1 2 3] [:a :b :c]) => [1 2 3 :a :b :c]
(into '(1 2 3) '(:a :b :c)) => (:c :b :a 1 2 3)
```

---

#Contando los elementos de una secuencia.
###Escribamos una pieza de código que cuente los elementos de un seq.

---

#Contando los elementos de una lista
```clojure
(defn aux-count [seq count]
  (if (empty? seq)
    count
    (aux-count (rest seq) (+ count 1))))

(defn count [seq]
  (aux-count seq 0))
```

---

#Creando secuencias (range start? end step?)
```clojure
(range 10)

(range 10 20)

(range 1 25 2)
```

---

#Creando secuencias (range start? end step?)
```clojure
(range 10) 
=> (0 1 2 3 4 5 6 7 8 9)

(range 10 20) 
=> (10 11 12 13 14 15 16 17 18 19)

(range 1 25 2) 
=> (1 3 5 7 9 11 13 15 17 19 21 23)
```

---

#Creando secuencias 
##(repeat n x) Repite n veces lo que se mande en x

```clojure
(repeat 5 1) 
=> (1 1 1 1 1)

(repeat 10 "x") 
=> ("x" "x" "x" "x" "x" "x" "x" "x" "x" "x")

```

---

#Creando secuencias infinitas
##(iterate f x) Toma un valor x, le aplica una función f para generar el siguiente valor para siempre
##(take n sequence) Retorna una  **lazy sequence** de los primeros n elementos de una sequence (posiblemente infinita)

```clojure
(take 10 (iterate inc 1)) 
=> (1 2 3 4 5 6 7 8 9 10)
```

```clojure
(defn numeros-enteros []
  (iterate inc 1))
```

---

#Creando secuencias infinitas
##(repeat x) Cuando se llama con un solo argumento, retorna una **lazy sequence** infinita

```clojure
(take 10 (repeat 1)) 
=> (1 1 1 1 1 1 1 1 1 1)
```

##(cycle seq) Toma una secuencia seq. y hace un ciclo sobre ella de manera infinita
```clojure
(take 10 (cycle (range 3))) 
=> (0 1 2 0 1 2 0 1 2 0)
```

---

#Creando secuencias
##(interleave & seqs) Toma dos colecciones y crea una nueva que intercala valores de ambas hasta que una de las dos termina
```clojure
(interleave (numeros-enteros) ["A" "B" "C" "D" "E"]) 
=> (1 "A" 2 "B" 3 "C" 4 "D" 5 "E")
```
##(interpose separator coll) Genera una seq en la que cada elemento de coll va separado por el separator
```clojure
(interpose "," ["manzana" "banano" "uva"]) 
=> ("manzana" "," "banano" "," "uva")
```

---

#Creemos un string separado por comas
```clojure
(str (interpose "," ["manzana" "banano" "uva"])) 
=> "clojure.lang.LazySeq@8aad604e"

(class (interpose "," ["manzana" "banano" "uva"])) 
=> clojure.lang.LazySeq

(str "A" "B" "C") 
=> "ABC"
```

---

#¿Cuál es el problema? 
##Funciones que reciben un número variable de params

---

#Creando un string separado por comas
###(str x & ys) Takes several parameters and returns de concatenation of them
```clojure
(apply str (interpose "," ["manzana" "banano" "uva"])) 
=> "manzana,banano,uva"
```

###(join separator sequence) Aplica el separador a la secuencia y genera un string de salida
```clojure
(use '[clojure.string :only (join)])
(join "," ["manzana" "banano" "uva"]) 
=> "manzana,banano,uva"
```

---

#Filtrando sequences
###Sequence tiene varias funciones que retornan un subconjunto de sequence

---

#Filter (filter pred coll)
```clojure
(def estudiantes [ 
  {:name "Hernan"
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
```

---

#Take-while (take-while pred coll)
```clojure
(take-while 
  (complement #{\a \e \i \o \u}) 
  "strong will continue")
  
(#{"a" "e" "i" "o" "u"} "p")

(#{"a" "e" "i" "o" "u"} "a")

(complement #{"a" "e" "i" "o" "u"})

((complement #{"a" "e" "i" "o" "u"}) "a")

((complement #{"a" "e" "i" "o" "u"}) "p")
```

---

#Drop-while (drop-while pred coll)
```clojure
(drop-while 
  (complement #{\a \e \i \o \u}) 
  "strong will continue")
```
=> "ong will continue"

---

#Split (split-at index col)  (split-with pred col)
```clojure
(split-at 5 (range 10))
=> [(0 1 2 3 4) (5 6 7 8 9)]



(split-with #(<= % 10) (range 0 20 2))
=> [(0 2 4 6 8 10) (12 14 16 18)]
```

---

#Predicados sobre seqs
##En las funciones de filtrado que vimos, normalmente se toma un predicado y se crea una nueva seq. Veamos funciones que aplican para todos los elementos de una secuencia.


---

#Every (every? pred coll)
```clojure
(every? odd? (numeros-enteros))

(every? odd? (take 10 (numeros-enteros)))
```

---

#Some (some pred coll)
```clojure
(defn divisible-por-ambos [n a b]
  (if (and (= 0 (mod n a))
           (= 0 (mod n b)))
    n
    false))

(defn divisible-por-7-y-3 [n]
  (divisible-por-ambos n 7 3))

(some divisible-por-7-y-3 (numeros-enteros))

(some divisible-por-7-y-3 (take 10 (numeros-enteros)))
```

---

#Negaciones (not-every? pred coll) (not-any? pred coll)
```clojure
(def estudiantes [ 
  {:name "Hernan"
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

(not-every? #(> (:promedio %) 3.0) estudiantes)
(not-every? #(> (:promedio %) 1.0) estudiantes)

(not-any? #(> (:promedio %) 5.0) estudiantes)
(not-any? #(> (:promedio %) 4.0) estudiantes)
````

---

#Transformaciones sobre secuencias
###Seq provee varias funciones que nos permiten aplicarle una transformación sobre una secuencia y crear una nueva


---

![](/Users/hernan/Documents/Mapa1/Diapositiva1.jpg)


---

#Map (map f seq)
```clojure
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
  (float (+ (/ (* celsius 9) 5) 32)))

(defn agregar-farenheit [reporte]
  (assoc reporte :temp-farenheit (a-farenheit (:temp-centigrados reporte))))

(map agregar-farenheit reportes-climatologicos)
```

---

#Map (map f seq)
```clojure
(
  {:ciudad "Medellín",
   :temp-centigrados 27, 
   :temp-farenheit 80.6}
  {:ciudad "Bogotá", 
  :temp-centigrados 21, 
  :temp-farenheit 69.8} 
  {:ciudad "Cartagena", 
  :temp-centigrados 30, 
  :temp-farenheit 86.0} 
  {:ciudad "Cali", 
  :temp-centigrados 25, 
  :temp-farenheit 77.0} 
  {:ciudad "Barranquilla", 
  :temp-centigrados 31, 
  :temp-farenheit 87.8}
)
```

---

#Reduce (reduce f seq)
```clojure
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

(defn mayor [reporte-a reporte-b]
  (if (> (:temp-centigrados reporte-a) (:temp-centigrados reporte-b))
    reporte-a
    reporte-b))
    
(reduce mayor reportes-climatologicos)
```

---

#Map (map f seq)

```clojure
(def reportes-ventas [{:ciudad "Medellin"
                       :total 2500000}
                       {:ciudad "Bogota"
                       :total 3000000}
                       {:ciudad "Cali"
                        :total 1000000}
                        {:ciudad "Cartagena"
                        :total 2000000}])

(defn suma [totales]
  (reduce + totales))

(defn promedio [totales]
  (/ (suma totales) (count totales)))

(defn estadisticas [totales]
  (map #(% totales) [suma count promedio]))

(estadisticas (map :total reportes-ventas))
```

---

#Sort (sort comp? coll)
```clojure
(sort [42 37 65 11 99 156]) 
=> (11 37 42 65 99 156)

(sort-by #(.toString %) [42 37 65 11 99 156]) 
=> (11 156 37 42 65 99)
```
```clojure
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
(sort-by :temp-centigrados reportes-climatologicos)
````

---

#List comprehension
###El abuelo de todas las transformaciones sobre colecciones.
###Una list comprehension crea una nueva lista con base en una colección existente y unas reglas sobre la colección de salida.

---

#For (for [binding coll-expr filter-expr? ... ] expr)

```clojure
(for [word ["dabale" "arroz" "a" "la" "zorra"]]
  (format "<p>%s</p>" word))
```
```clojure
=> ("<p>dabale</p>" "<p>arroz</p>" "<p>a</p>" "<p>la</p>" "<p>zorra</p>")
```
```clojure
(defn formato-parrafo [texto]
  (format "<p>%s</p>" texto))

(map formato-parrafo ["dabale" "arroz" "a" "la" "zorra"])
```
```clojure
=> ("<p>dabale</p>" "<p>arroz</p>" "<p>a</p>" "<p>la</p>" "<p>zorra</p>")
```

---

#For doble binding
```clojure
(for [columna "ABCDEFGH"
      fila (range 1 9)]
  (format "%c%d" columna fila))
```

```clojure
(
"A1" "A2" "A3" "A4" "A5" "A6" "A7" "A8" 
"B1" "B2" "B3" "B4" "B5" "B6" "B7" "B8" 
"C1" "C2" "C3" "C4" "C5" "C6" "C7" "C8" 
"D1" "D2" "D3" "D4" "D5" "D6" "D7" "D8" 
"E1" "E2" "E3" "E4" "E5" "E6" "E7" "E8" 
"F1" "F2" "F3" "F4" "F5" "F6" "F7" "F8" 
"G1" "G2" "G3" "G4" "G5" "G6" "G7" "G8" 
"H1" "H2" "H3" "H4" "H5" "H6" "H7" "H8"
)
```

---

#For con filter
```clojure
(defn numeros-enteros []
  (iterate inc 1))

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
```

---

#Lazy seq.


---

#Referencias
* Programming Clojure
* Clojure for the brave and true
* http://clojure.org/reference/sequences