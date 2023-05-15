package Practicas.AlgoritmosGeneticos_Sudoku1.IntelliJ.algGeneticos.src;
import com.qqwing.Difficulty;

public class Main {
    public static void main(String[] args) {
        //Enunciado
        int[][] tablero = {
                {1, 9, 0, 0, 2, 0, 5, 0, 8},
                {0, 6, 7, 0, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 8, 3, 0, 9, 0},
                {3, 0, 0, 7, 0, 0, 2, 0, 9},
                {0, 0, 0, 1, 0, 0, 6, 0, 5},
                {0, 0, 0, 5, 9, 8, 0, 0, 4},
                {4, 0, 5, 8, 0, 0, 9, 0, 6},
                {2, 0, 6, 0, 4, 0, 0, 5, 1},
                {9, 0, 1, 0, 0, 6, 0, 7, 0}
        };
        //Juanma
        int[][] tablero2 = {
                {5, 8, 6, 0, 7, 0, 0, 0, 0},
                {0, 0, 4, 9, 0, 1, 6, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 0},
                {0, 0, 7, 0, 6, 0, 0, 0, 0},
                {9, 0, 2, 0, 1, 0, 3, 0, 5},
                {0, 0, 5, 0, 9, 0, 0, 0, 0},
                {0, 9, 0, 0, 4, 0, 0, 0, 8},
                {0, 0, 3, 5, 0, 0, 0, 6, 0},
                {0, 0, 0, 0, 2, 0, 4, 7, 0}
        };
        //Uno dificultad simple
        int[][] tablero3 = {
                {9, 0, 0, 2, 0, 0, 0, 0, 5},
                {0, 1, 0, 0, 0, 7, 0, 0, 0},
                {2, 3, 8, 0, 0, 0, 0, 0, 1},
                {5, 4, 0, 0, 7, 0, 0, 0, 0},
                {7, 0, 3, 9, 8, 0, 0, 2, 0},
                {0, 0, 0, 3, 1, 0, 7, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 8, 0},
                {0, 0, 0, 0, 9, 3, 0, 4, 0}
        };
        Sudoku sud = new Sudoku(tablero);
        //Sudoku sud = new Sudoku(6);
        //Sudoku sud = new Sudoku(Difficulty.SIMPLE);
        sud.printConsole();
        sud.printFichero("output.txt");
        sud.solve();
        sud.printFichero("output.txt");
    }
}
