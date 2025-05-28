import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  static final String[] NOMS = {
          "José Javier Morales Caravaca"
  };

  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   */
  static class Tema1 {
    /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
     */
    static final char CONJ = '∧';
    static final char DISJ = '∨';
    static final char IMPL = '→';
    static final char NAND = '.';

    static int exercici1(char[] ops, int[] vars) {
      int varsUnicas = 0;
      ArrayList<Integer> varsEncontradas = new ArrayList<>();
      for (int var :vars) {
        if(!varsEncontradas.contains(var)) {
          varsEncontradas.add(var);
        }
      }
      varsUnicas = varsEncontradas.size();

      boolean [][] tablaVerdad = new boolean [pow2(varsUnicas)][varsUnicas];
      for (int columna = 0; columna < varsUnicas; columna++) {
        int repeticiones = 0;
        boolean dato = false;
        for (int fila = 0; fila < pow2(varsUnicas); fila++) {
          tablaVerdad[fila][columna] = dato;
          repeticiones++;
          if(repeticiones == pow2(columna)) {
            dato = !dato;
            repeticiones = 0;
          }
        }
      }
      //Por cada fila de la tabla de verdad, calcula su valor de la expresión correspondiente
      // y almacena el resultado en una array de valores
      boolean[] valores = new boolean[pow2(varsUnicas)];
      for (int f = 0 ; f < tablaVerdad.length ; f++) {
        boolean[] fila = tablaVerdad[f];
        boolean valorAnterior = calcularExpresion(fila[vars[0]], ops[0], fila[vars[1]]);
        for (int i = 2; i < vars.length; i++){
          valorAnterior = calcularExpresion(valorAnterior, ops[i-1], fila[vars[i]]);
        }
        valores[f] = valorAnterior;
      }

      //Si
      boolean primerValor = valores[0];
      for (boolean valor : valores) {
        if(valor != primerValor) {
          return -1;
        }
      }
      return primerValor ? 1 : 0;
    }

    //Calcular la expresión lógica var1 op var2 (el orden es únicamente relevante para la implicación)
    static boolean calcularExpresion(boolean var1, char op, boolean var2){

      switch(op) {
        case CONJ:
          return var1 && var2;
        case DISJ:
          return var1 || var2;
        case NAND:
          return !(var1 && var2);
        case IMPL:
          return !var1 || var2; // Utilizando la expresión equivalente a la implicación
        default:
          return false;
          //Nunca se debería llegar al caso default, pero Java exige que se añada un return por defecto
      }
    }

    //Calcula 2^n
    static int pow2(int n){
      int p = 1;
      for(int i = 1; i <= n; i++){
        p*=2;
      }
      return p;
    }

    /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
      boolean resultado1 = true;
      for(int x : universe) {
        if(!p.test(x)) {
          resultado1 = false;
        }
      }

      boolean resultado2 = false;
      for(int x : universe) {
        if(q.test(x)) {
          if (resultado2){
            resultado2 = false;
            break;
          }
          resultado2 = true;
        }
      }
      return resultado1 == resultado2;
    }

    static void tests() {
      // Exercici 1
      // Taules de veritat

      // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
      test(1, 1, 1, () -> exercici1(new char[] { IMPL, DISJ, DISJ }, new int[] { 0, 2, 1, 0 }) == 1);

      // Contradicció: (p0 . p0) ∧ p0
      test(1, 1, 2, () -> exercici1(new char[] { NAND, CONJ }, new int[] { 0, 0, 0 }) == 0);

      //Modus Ponens: ((p0 → p1) ^ p0) → p1
      test(1, 1, 3, () -> exercici1(new char[] { IMPL, CONJ, IMPL }, new int[] { 0, 1, 0, 1 }) == 1);

      //Indeterminación: p0 → p1
      test(1, 1, 4, () -> exercici1(new char[] { IMPL }, new int[] { 0, 1}) == -1);

      // Exercici 2
      // Equivalència

      test(1, 2, 1, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x == 0, (x) -> x == 0);
      });

      test(1, 2, 2, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x >= 1, (x) -> x % 2 == 0);
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
   */
  static class Tema2 {
    /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
     */
    static int exercici1(int[] a) {
      int total = 0;
      for (int k = 1; k <= a.length; k++) {
        total += stirling(a.length, k);
      }
      return total;
    }

    static int stirling(int n, int k){
      if (n == k){
        return 1;
      }else if (n==0 || k==0 || n<k) {
        return 0;
      } else{
        return stirling(n-1, k-1) + k * stirling(n-1, k);
      }
    }

    /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
     */
    static int exercici2(int[] a, int[][] rel) {
        //Clausura reflexiva
      int reflex = 0;
      for(int x : a){
        if (!contieneEnRelacion(rel, new int[]{x, x})){
          reflex++;
        }
      }

      //Clausura antisimétrica
      for(int x : a){
        for(int y : a){
          if(contieneEnRelacion(rel, new int[]{x, y}) && contieneEnRelacion(rel, new int[]{y, x}) && x != y){
            return -1;
          }
        }
      }

      //Clausura transitiva
      int transitiva = 0;
      for(int x : a){
        for(int y : a){
          for (int z : a){
            if(contieneEnRelacion(rel, new int[]{x, y}) && contieneEnRelacion(rel, new int[]{y, z}) && !contieneEnRelacion(rel, new int[]{x, z})){
              transitiva++;
            }
          }
        }
      }

      return reflex + rel.length + transitiva;
    }

    static boolean contieneEnRelacion(int[][] rel, int [] x){
      for (int[] elemento : rel) {
        if(Arrays.equals(x, elemento)){
          return true;
        }
      }
      return false;
    }

    static boolean contieneEnArray(int x, int[] a){
      for(int y : a){
        if(x == y){
          return true;
        }
      }
      return false;
    }

    /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
     */
    static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {
      return op ? findSuprem(a, rel, x): findInfim(a, rel, x);
    }

    static Integer findInfim(int[] a, int[][] rel, int[] x){
      ArrayList<Integer> cotaInferior = new ArrayList<>();
      for(int n: a){
        int relacionados = 0;
        for(int y: x){
          if(contieneEnRelacion(rel, new int[]{n, y}) && !contieneEnArray(n, x)){
            relacionados++;
          }
        }
        if(relacionados == x.length && !contieneEnArray(n, x)){
          cotaInferior.add(n);
        }
      }
      if(cotaInferior.size() == 1){
        return cotaInferior.getFirst();
      }

      if(cotaInferior.isEmpty()){
        return null;
      }
      for(int n: cotaInferior){
        int relacionados = 0;
        for(int y: cotaInferior){
          if (contieneEnRelacion(rel, new int[]{y, n}) && n != y){
            relacionados++;
          }
        }
        if(relacionados ==  cotaInferior.size() -1){
          return n;
        }
      }
      return null;
    }

    static Integer findSuprem(int[] a, int[][] rel, int[] x){
      ArrayList<Integer> cotaSuperior = new ArrayList<>();
      for(int n: a){
        int relacionados = 0;
        for(int y: x){
          if(contieneEnRelacion(rel, new int[]{y, n}) && !contieneEnArray(n, x)){
            relacionados++;
          }
        }
        if(relacionados == x.length && !contieneEnArray(n, x)){
          cotaSuperior.add(n);
        }
      }
      if(cotaSuperior.isEmpty()){
        return null;
      }

      if(cotaSuperior.size() == 1){
        return cotaSuperior.getFirst();
      }

      for(int n: cotaSuperior){
        int relacionados = 0;
        for(int y: cotaSuperior){
          if (contieneEnRelacion(rel, new int[]{n, y}) && n != y){
            relacionados++;
          }
        }
        if(relacionados ==  cotaSuperior.size() -1){
          return n;
        }
      }

      return null;
    }

    /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
     */
    static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {

//      if (esBiyectiva(a, b, f)) {
//
//        int[][] res = new int[a.length][2];
//        for (int i = 0; i < a.length; i++) {
//          int x = a[i];
//          res[i] = new int[]{f.apply(x), x};
//        }
//        return res;
//      }

      if (esInyectiva(a, b, f)) {

        int[][] res = new int[a.length][2];
        for (int i = 0; i < a.length; i++) {
          int x = a[i];
          res[i] = new int[]{f.apply(x), x};
          System.out.println(res[i][0] + " " + res[i][1]);
        }
        return res;
      }

      if (esExhaustiva(a, b, f)) {
        int[][] res = new int[b.length][2];
        for (int i = 0; i < b.length; i++) {
          int x = a[i];
          res[i] = new int[]{f.apply(x), x};
        }
        return res;
      }

      return null;
    }

    static boolean esInyectiva(int[] a, int[] b, Function<Integer, Integer> f){
      ArrayList<Integer> imagenes = new ArrayList<>();
      for(int x : a){
        if (imagenes.contains(f.apply(x))){
          return false;
        }
        imagenes.add(x);
      }
      return true;
    }

    static boolean esExhaustiva(int[] a, int[] b, Function<Integer, Integer> f){
      ArrayList<Integer> imagenes = new ArrayList<>();
      for(int x : a){
        if(!imagenes.contains(f.apply(x))){
          imagenes.add(f.apply(x));
        }
      }
      return imagenes.size() == b.length;
    }

    static boolean esBiyectiva(int[] a, int[] b, Function<Integer, Integer> f){
      return esInyectiva(a, b, f) && esExhaustiva(a, b, f);

    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // Nombre de particions

      test(2, 1, 1, () -> exercici1(new int[] { 1 }) == 1);
      test(2, 1, 2, () -> exercici1(new int[] { 1, 2, 3 }) == 5);

      // Exercici 2
      // Clausura d'ordre parcial

      final int[] INT02 = { 0, 1, 2 };

      test(2, 2, 1, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 2} }) == 6);
      test(2, 2, 2, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 0}, {1, 2} }) == -1);

      // Exercici 3
      // Ínfims i suprems

      final int[] INT15 = { 1, 2, 3, 4, 5 };
      final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
      final Integer ONE = 1;

      final int[] INT_TEST3 = {2, 3, 4, 9, 12, 18};
      final int[][] DIV_TEST3 = generateRel(INT_TEST3, (n, m) -> m % n == 0);

      final int[] INT_TEST4 = {2, 3, 4, 9, 12, 18, 36};
      final int[][] DIV_TEST4 = generateRel(INT_TEST4, (n, m) -> m % n == 0);
      final Integer TWELVE = 12;

      test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[] { 2, 3 }, false)));
      test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[] { 2, 3 }, true) == null);
      test(2, 3, 3, () ->  exercici3(INT_TEST3, DIV_TEST3, new int[] { 4, 9 }, false) == null);
      test(2, 3, 4, () -> TWELVE.equals(exercici3(INT_TEST4, DIV_TEST4, new int[] { 3, 4 }, true)));

      // Exercici 4
      // Inverses

      final int[] INT05 = { 0, 1, 2, 3, 4, 5 };

      test(2, 4, 1, () -> {
        var inv = exercici4(INT05, INT02, (x) -> x/2);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT02.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1]/2 != i)
            return false;
        }

        return true;
      });

      test(2, 4, 2, () -> {
        var inv = exercici4(INT02, INT05, (x) -> x);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT05.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1] != i)
            return false;
        }

        return true;
      });
    }

    /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
     */
    static int[][] lexSorted(int[][] arr) {
      if (arr == null)
        return null;

      var arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr2, Arrays::compare);
      return arr2;
    }

    /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
     */
    static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
      var rel = new ArrayList<int[]>();

      for (int a : as) {
        for (int b : bs) {
          if (pred.test(a, b)) {
            rel.add(new int[] { a, b });
          }
        }
      }

      return rel.toArray(new int[][] {});
    }

    // Especialització de generateRel per as = bs
    static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
      return generateRel(as, as, pred);
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
   */
  static class Tema3 {
    /*
     * Determinau si el graf `g` (no dirigit) té cicles.
     */
    static boolean exercici1(int[][] g) {
      return tieneCiclos(g);
    }

    static boolean tieneCiclos(int [][] g){
      int aristas = getAristas(g);
      int vertices = g.length;
      return aristas >= vertices;
    }

    static int getAristas(int [][] g){
      int sumatorioGrafos = 0;
      for(int[] veins : g){
        sumatorioGrafos += veins.length;
      }
      return sumatorioGrafos/2;
    }

    /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
     */
    static boolean exercici2(int[][] g1, int[][] g2) {
      int[][] m1 = getMatrizAdyacencia(g1);
      int[][] m2 = getMatrizAdyacencia(g2);
      int n = m1.length;
      if (m2.length != n || m1[0].length != m2[0].length) return false;

      ArrayList<Integer> nodos = new ArrayList<>();
      for (int i = 0; i < n; i++) nodos.add(i);

      return probarPermutaciones(nodos, 0, m1, m2);
    }

    static int [][] getMatrizAdyacencia(int [][] g){
      int [][] m = new int[g.length][g.length];
      for (int i = 0; i < g.length; i++) {
        for (int j : g[i]) {
          m[i][j] = 1;
        }
      }
      return m;
    }

    private static boolean probarPermutaciones(ArrayList<Integer> perm, int index, int[][] g1, int[][] g2) {
      if (index == perm.size()) {
        if (compararConPermutacion(g1, g2, perm)) return true;
        return false;
      }

      for (int i = index; i < perm.size(); i++) {
        intercambiar(perm, index, i);
        if (probarPermutaciones(perm, index + 1, g1, g2)) return true;
        intercambiar(perm, index, i);
      }

      return false;
    }

    private static void intercambiar(ArrayList<Integer> lista, int i, int j) {
      int temp = lista.get(i);
      lista.set(i, lista.get(j));
      lista.set(j, temp);
    }

    private static boolean compararConPermutacion(int[][] g1, int[][] g2, ArrayList<Integer> perm) {
      int n = g1.length;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (g1[i][j] != g2[perm.get(i)][perm.get(j)]) return false;
        }
      }
      return true;
    }

    /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
     */
    static int[] exercici3(int[][] g, int r) {
      if (tieneCiclos(g)){
        return null;
      }
      int [][] m = getMatrizAdyacencia(g);
      ArrayList<Integer> lista = new ArrayList<>();
      boolean[] visitado = new boolean[m.length];
      recorrerArbol(m, r, visitado, lista);

      // Convertir ArrayList a int[]
      int[] resultado = new int[lista.size()];
      for (int i = 0; i < lista.size(); i++) {
        resultado[i] = lista.get(i);
      }
      return resultado;
    }

    private static void recorrerArbol(int[][] grafo, int nodo, boolean[] visitado, ArrayList<Integer> lista) {
      visitado[nodo] = true;

      for (int i = 0; i < grafo.length; i++) {
        if (grafo[nodo][i] == 1 && !visitado[i]) {
          recorrerArbol(grafo, i, visitado, lista);
        }
      }

      lista.add(nodo); // Postorden: hijos primero, luego el nodo
    }

    /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
     */
    static int exercici4(char[][] mapa) {
      throw new UnsupportedOperationException("pendent");
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {

      final int[][] D2 = { {}, {} };
      final int[][] C3 = { {1, 2}, {0, 2}, {0, 1} };

      final int[][] T1 = { {1, 2}, {0}, {0} };
      final int[][] T2 = { {1}, {0, 2}, {1} };

      // Exercici 1
      // G té cicles?

      test(3, 1, 1, () -> !exercici1(D2));
      test(3, 1, 2, () -> exercici1(C3));

      // Exercici 2
      // Isomorfisme de grafs

      test(3, 2, 1, () -> exercici2(T1, T2));
      test(3, 2, 2, () -> !exercici2(T1, C3));

      // Exercici 3
      // Postordre

      test(3, 3, 1, () -> exercici3(C3, 1) == null);
      test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[] { 1, 2, 0 }));

      // Exercici 4
      // Laberint

      test(3, 4, 1, () -> {
        return -1 == exercici4(new char[][] {
            " #O".toCharArray(),
            "D# ".toCharArray(),
            " # ".toCharArray(),
        });
      });

      test(3, 4, 2, () -> {
        return 8 == exercici4(new char[][] {
            "###D".toCharArray(),
            "O # ".toCharArray(),
            " ## ".toCharArray(),
            "    ".toCharArray(),
        });
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
   */
  static class Tema4 {
    /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
     */
    static int[] exercici1(String msg, int n, int e) {
      throw new UnsupportedOperationException("pendent");
    }

    /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     */
    static String exercici2(int[] m, int n, int e) {
      throw new UnsupportedOperationException("pendent");
    }

    static void tests() {
      // Exercici 1
      // Codificar i encriptar
      test(4, 1, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = exercici1("Patata", n, e);
        return Arrays.equals(encr, new int[] { 4907, 4785, 4785 });
      });

      // Exercici 2
      // Desencriptar i decodificar
      test(4, 2, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = new int[] { 4907, 4785, 4785 };
        var decr = exercici2(encr, n, e);
        return "Patata".equals(decr);
      });
    }
  }

  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    System.out.println("---- Tema 1 ----");
    Tema1.tests();
    System.out.println("---- Tema 2 ----");
    Tema2.tests();
    System.out.println("---- Tema 3 ----");
    Tema3.tests();
    System.out.println("---- Tema 4 ----");
    Tema4.tests();
  }

  // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
  static void test(int tema, int exercici, int test, BooleanSupplier p) {
    try {
      if (p.getAsBoolean())
        System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
      else
        System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
        System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
      } else {
        System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
        e.printStackTrace();
      }
    }
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
