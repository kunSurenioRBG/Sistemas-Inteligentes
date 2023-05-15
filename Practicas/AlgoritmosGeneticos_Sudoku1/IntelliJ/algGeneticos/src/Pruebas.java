package Practicas.AlgoritmosGeneticos_Sudoku1.IntelliJ.algGeneticos.src;
import com.qqwing.Difficulty;

public class Pruebas {
    public static void main(String[] args) {
        Sudoku sud = new Sudoku(Difficulty.EASY);
        System.out.println("-----------------TABLERO SUDOKU----------------");
        sud.printConsole();
        sud.printFichero("output.txt");
        System.out.println();
        System.out.println("---------------POBLACION INICIAL---------------");
        sud.printPoblacion();
        System.out.println();
        System.out.println("-----------PRUEBA CROSSOVER Y MUTATE-----------");
        Cromosoma c1 = sud.getPoblacionInicial().get(0);
        Cromosoma c2 = sud.getPoblacionInicial().get(1);
        System.out.println("Cromosoma 1:\t\t\t\t\t" + c1);
        System.out.println("Cromosoma 2:\t\t\t\t\t" + c2);
        System.out.println("Cromosoma crossover de c1 y c2:\t" + Cromosoma.crossover(c1, c2));
        System.out.println("Cromosoma 1:\t\t\t\t\t" + c1);
        System.out.println("Cromosoma mutante de c1:\t\t" + Cromosoma.mutate(c1));
    }
}
