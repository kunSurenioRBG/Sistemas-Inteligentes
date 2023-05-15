package Practicas.AlgoritmosGeneticos_Sudoku1.IntelliJ.algGeneticos.src;

import java.util.ArrayList;
import java.util.Random;

public class Cromosoma {
    static final int SIZE = 9;
    private ArrayList<Tupla>[] cromosoma;

    record Tupla(int pos, int value) {
    }

    //Constructor default
    public Cromosoma() {
        cromosoma = new ArrayList[SIZE];
        for (int i = 0; i < 9; i++) {
            cromosoma[i] = new ArrayList<Tupla>();
        }
    }

    //Constructor al que se le pasa un cromosoma para copiar su contenido
    public Cromosoma(Cromosoma c) {
        cromosoma = c.cromosoma;
    }

    public void addValue(int row, int pos, int value) {
        cromosoma[row].add(new Tupla(pos, value));
    }

    public int getValue(int row, int pos) {
        for (Tupla t : cromosoma[row]) {
            if (t.pos == pos) {
                return t.value;
            }
        }
        return 0;
    }

    public static Cromosoma crossover(Cromosoma c1, Cromosoma c2) {
        Cromosoma hijo = new Cromosoma(); //hijo fruto del cruce de c1 y c2
        Random r = new Random();

        int puntoCruce = r.nextInt(SIZE - 1) + 1; //seleccionamos de forma aleatoria el punto de cruce (entre 1 y 8)

        //Copiamos la primera parte de c1
        for (int i = 0; i < puntoCruce; i++) {
            for (Tupla t : c1.cromosoma[i]) {
                hijo.addValue(i, t.pos, t.value);
            }
        }

        //Copiamos la segunda parte de c2
        for (int i = puntoCruce; i < SIZE; i++) {
            for (Tupla t : c2.cromosoma[i]) {
                hijo.addValue(i, t.pos, t.value);
            }
        }

        return hijo;
    }

    public static Cromosoma mutate(Cromosoma c) {
        Cromosoma mutant = new Cromosoma(c); //cromosoma fruto de la mutacion de c
        Random r = new Random();


        int whereToMutate = r.nextInt(SIZE); //en que parte del cromosoma se va a mutar (entre 0 y 8)
        int portionSize = c.cromosoma[whereToMutate].size(); //longitud de la porcion del cromosoma

        if (portionSize > 1) {
            int posicion1, posicion2;
            do {
                posicion1 = r.nextInt(portionSize);
                posicion2 = r.nextInt(portionSize);
            } while (posicion1 == posicion2);

            Tupla t1 = mutant.cromosoma[whereToMutate].get(posicion1); //variable auxiliar para el intercambio
            Tupla t2 = mutant.cromosoma[whereToMutate].get(posicion2); //variable auxiliar para el intercambio
            mutant.cromosoma[whereToMutate].set(posicion1, new Tupla(t1.pos, t2.value)); //intercambiamos los valores de posicion1 con posicion2 (queremos mantener la posicion)
            mutant.cromosoma[whereToMutate].set(posicion2, new Tupla(t2.pos, t1.value)); //intercambiamos los valores de posicion2 con posicion1
        }

        return mutant;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < SIZE; i++) {
            for (Tupla t : cromosoma[i]) {
                s += t.pos + "-" + t.value + " ";
            }
            if (i != SIZE - 1) {
                s += "| ";
            }
        }
        return s;
    }
}
