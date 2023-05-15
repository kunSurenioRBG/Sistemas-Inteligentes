package Practicas.AlgoritmosGeneticos_Sudoku1.IntelliJ.algGeneticos.src;

import com.qqwing.Difficulty;
import com.qqwing.QQWing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Sudoku {
    static final int SIDE = 9;
    static final int MAXFITNESS = 162; //maximo de valores unicos (sin contar filas). 9x9 (columnas) + 9x9 (cuadrados)
    private int[][] tablero;
    private ArrayList<Cromosoma> poblacionInicial;
    boolean resuelto;
    public Sudoku(Difficulty d){
        resuelto = false;
        tablero = new int[SIDE][SIDE];
        int[] secuencia = createPuzzleByDifficulty(d);
        int count = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                tablero[i][j] = secuencia[count];
                count++;
            }
        }
        inicializarPoblacion();
    }

    public Sudoku(int n){
        resuelto = false;
        tablero = new int[SIDE][SIDE];
        int[] secuencia = computePuzzleWithNHolesPerRow(n);
        int count = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                tablero[i][j] = secuencia[count];
                count++;
            }
        }
        inicializarPoblacion();
    }

    public Sudoku(int[][] tablero){
        resuelto = false;
        this.tablero = tablero;
        inicializarPoblacion();
    }
    //create sudoku of specific difficulty level
    private static int[] createPuzzleByDifficulty(Difficulty d) {
        QQWing qq = new QQWing();
        qq.setRecordHistory(true);
        qq.setLogHistory(false);
        boolean go_on = true;
        while (go_on) {
            qq.generatePuzzle();
            qq.solve();
            Difficulty actual_d = qq.getDifficulty();
            //System.out.println("Difficulty: "+actual_d.getName());
            go_on = !actual_d.equals(d);
        }
        int []puzzle = qq.getPuzzle();
        return puzzle;
    }

    //cheat by creating absurdly simple sudoku, with a given number of holes per row
    public static int[] computePuzzleWithNHolesPerRow(int numHolesPerRow) {
        Random rnd = new Random();
        QQWing qq = new QQWing();

        qq.setRecordHistory(true);
        qq.setLogHistory(false);
        qq.generatePuzzle();
        qq.solve();
        int []solution = qq.getSolution();
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i=0; i<9; i++) {
            set.clear();
            while(set.size()<numHolesPerRow) {
                int n = rnd.nextInt(9);
                if (set.contains(n)) continue;
                set.add(n);
            }
            for (Integer hole_idx : set) {
                solution[i*9+hole_idx] = 0;
            }
        }
        return solution;
    }

    private void inicializarPoblacion(){
        poblacionInicial = new ArrayList<>();

        Integer[] numeros = {1,2,3,4,5,6,7,8,9};
        ArrayList<Integer> posibleNum = new ArrayList<>(Arrays.asList(numeros));
        Map<Integer, ArrayList<Integer>> valoresPosibles = new HashMap<>(); //guardar los posibles valores por fila

        Integer[] masNumeros = {0,1,2,3,4,5,6,7,8};
        ArrayList<Integer> posiblesHuecos = new ArrayList<>(Arrays.asList(masNumeros));
        Map<Integer, ArrayList<Integer>> huecosPosibles = new HashMap<>(); //guardar los huecos de cada fila

        for (int i = 0; i < SIDE; i++) {
            ArrayList<Integer> copiaNumeros = new ArrayList<>(posibleNum);
            ArrayList<Integer> copiaHuecos = new ArrayList<>(posiblesHuecos);
            for (int j = 0; j < SIDE; j++) {
                if(tablero[i][j]!=0){
                    //Borrar numero
                    Integer numABorrar = tablero[i][j];
                    copiaNumeros.remove(numABorrar);
                    //Borrar hueco
                    numABorrar = j;
                    copiaHuecos.remove(numABorrar);
                }
            }
            valoresPosibles.put(i,copiaNumeros);
            huecosPosibles.put(i,copiaHuecos);
        }
        int poblacionSize = 25;
        for (int i = 0; i < poblacionSize; i++) {
            Cromosoma c = new Cromosoma();
            for (Map.Entry<Integer, ArrayList<Integer>> entry : valoresPosibles.entrySet()) {
                int fila = entry.getKey();
                ArrayList<Integer> listaNumeros = new ArrayList<>(entry.getValue());
                ArrayList<Integer> listaHuecos = new ArrayList<>(huecosPosibles.get(fila));
                Collections.shuffle(listaNumeros);
                while(!listaNumeros.isEmpty()){
                    c.addValue(fila, listaHuecos.get(0), listaNumeros.get(0));
                    listaNumeros.remove(0);
                    listaHuecos.remove(0);
                }
            }
            poblacionInicial.add(c);
        }
    }

    public void solve(){
        ArrayList<Cromosoma> population = new ArrayList<>(poblacionInicial);
        ArrayList<Cromosoma> population2 = new ArrayList<>();
        Cromosoma bestIndividual = bestFitness(population);
        int timer = 0;
        System.out.println(fitnessMedio(population) + " " + fitness(bestIndividual));

        while(fitness(bestIndividual) != MAXFITNESS && timer<100000) {
            for (int i = 0; i < population.size(); i++) {
                Cromosoma primero, segundo;
                Random r = new Random();
                int size;
                Set<Integer> set;
                int probabilidadMutar; //numero entre el 0 y el 100
                if(fitness(bestIndividual) >= 156){
                    size = 8;
                    probabilidadMutar = 80;
                } else {
                    size = 4;
                    probabilidadMutar = 30;
                }

                set = new HashSet<>();
                while(set.size() < size){
                    set.add(r.nextInt(population.size()));
                }

                ArrayList<Cromosoma> competicion = new ArrayList<>();
                    for (Integer n : set) {
                        competicion.add(population.get(n));
                    }
                    primero = bestFitness(competicion);
                    competicion.remove(primero);
                    segundo = bestFitness(competicion);

                Cromosoma hijo = Cromosoma.crossover(primero, segundo);

                if(r.nextInt(100) < probabilidadMutar){
                    hijo = Cromosoma.mutate(hijo);
                }
                population2.add(hijo);
            }
            population = new ArrayList<>(population2);
            population2 = new ArrayList<>();
            bestIndividual = bestFitness(population);
            timer++;
            System.out.println(fitnessMedio(population) + " " + fitness(bestIndividual));
        }
        if(fitness(bestIndividual) == MAXFITNESS){
            resuelto = true;
        }
        reconstructSudoku(bestIndividual);
    }

    private void reconstructSudoku(Cromosoma c){
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if(tablero[i][j] == 0){
                    tablero[i][j] = c.getValue(i,j);
                }
            }
        }
    }
    //Selecciona el mejor cromosoma de una poblacion
    private Cromosoma bestFitness(ArrayList<Cromosoma> population){
        if(population.size() == 0){
            return null;
        }
        Cromosoma bestOne = population.get(0);
        int bestFitness = fitness(bestOne);
        int i = 1;
        while(bestFitness != MAXFITNESS && i < population.size()){
            if(fitness(population.get(i)) > bestFitness){
                bestOne = population.get(i);
                bestFitness = fitness(population.get(i));
            }
            i++;
        }
        return bestOne;
    }

    private float fitnessMedio(ArrayList<Cromosoma> population){
        float suma = 0;
        for (int i = 0; i < population.size(); i++) {
            suma += fitness(population.get(i));
        }
        return suma/population.size();
    }

    public ArrayList<Cromosoma> getPoblacionInicial() {
        return poblacionInicial;
    }

    public int fitness(Cromosoma c){
        int valoresUnicos = 0; //Contador de valores unicos

        //Contamos valores unicos en las columnas
        for (int i = 0; i < SIDE; i++) {
            ArrayList<Integer> numeros = new ArrayList<>();
            for (int j = 0; j < SIDE; j++) {
                int n = tablero[j][i];
                if(n == 0){ //Si n vale 0, miramos al cromosoma
                    n = c.getValue(j,i);
                }
                if(!numeros.contains(n)){ //si no hemos guardado ese valor previamente, es unico
                    numeros.add(n);
                    valoresUnicos++;
                }
            }
        }
        //Contamos para los cuadrados
        //Iterar sobre los cuadrados
        for (int i = 0; i < SIDE; i+=3) {
            for (int j = 0; j < SIDE; j+=3) {

                //Iteramos dentro de cada cuadrado 3x3
                ArrayList<Integer> numeros = new ArrayList<>();
                for (int k = i; k < i + 3; k++) {
                    for (int l = j; l < j + 3; l++) {
                        int n = tablero[k][l];
                        if(n == 0){ //Si n vale 0, miramos al cromosoma
                            n = c.getValue(k,l);
                        }
                        if(!numeros.contains(n)){ //si no hemos guardado ese valor previamente, es unico
                            numeros.add(n);
                            valoresUnicos++;
                        }
                    }
                }
            }
        }
        //Devolvemos los valores unicos que hemos contado
        return valoresUnicos;
    }
    public void printPoblacion(){
        for (Cromosoma c: poblacionInicial) {
            System.out.println("Cromosoma: " + c + " - Fitness: " + fitness(c) + "/" + MAXFITNESS);
        }
    }
    public void printConsole(){
        if(resuelto){
            System.out.println("Se ha encontrado la solucion optima: ");
        } else {
            System.out.println("No se ha encontrado la solucion optima");
        }
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void printFichero(String nombreFichero){
        try {
            PrintWriter pw = new PrintWriter(nombreFichero);
            if(resuelto){
                pw.println("Se ha encontrado la solucion optima: ");
            } else {
                pw.println("No se ha encontrado la solucion optima");
            }
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    pw.print(tablero[i][j]+" ");
                }
                pw.println();
            }
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
