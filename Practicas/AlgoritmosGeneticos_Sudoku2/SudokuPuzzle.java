package Practicas.AlgoritmosGeneticos_Sudoku2;

import java.util.Random;
//Para generar el sudoku con valores aleatorios

public class SudokuPuzzle {
    public int[][] puzzle = new int[9][9]; // Genera sudoku
    public int[][] solved_puzzle = new int[9][9]; // Sudoku resuelto

    private int[][] _tmp_grid = new int[9][9]; // matriz auxiliar usada para resolver el sudoku
    private Random rand = new Random();
    private short solution_count; // 1 solucion

    /**
     * El constructor genera una nuevo sudoku y su solucion
     */
    public SudokuPuzzle() {
        generateSolvedPuzzle(0);
        generaPuzzle();
    }

    /**
     * Busqueda de la solucion mediante una busqueda en profundidad
     */
    private boolean generateSolvedPuzzle(int cur_cell) {
        if (cur_cell > 80)
            return true;

        int col = cur_cell % 9;
        int row = cur_cell / 9;

        // crea una secuencia de enteros{1,...,9} en orden aleatorio
        int[] numbers = new int[9];
        for (int i = 0; i < 9; i++)
            numbers[i] = 1 + i;
        desordenarValores(numbers);

        for (int i = 0; i < 9; i++) {
            int n = numbers[i]; // para el siguiente numero en el array
            // si el numero, dado las reglas del sudoku, podria ir en esa posicion...
            if (!existeEnColumna(solved_puzzle, n, col)
                    && !existeEnFila(solved_puzzle, n, row)
                    && !existeEnSubmatriz(solved_puzzle, n, row, col)) {
                // intenta rellenar la siguiente celda con el numero de la celda donde me encuentro ahora
                solved_puzzle[row][col] = n;
                if (generateSolvedPuzzle(cur_cell + 1))
                    return true;
                solved_puzzle[row][col] = 0; // si el valor no me vale, vuelvo a poner a cero y probamos con otro numero
            }
        }
        return false; // no encuentra solucion
    }

    /**
     * Resuelve el sudoku mediante una busqueda en profundidad, y guarda el numero de soluciones en solution_count.
     * Si hay 2 soluciones, pararemos la busqueda.
     */
    private boolean solvePuzzle(int cur_cell) {
        if (cur_cell > 80) {
            solution_count++;
            if (solution_count > 1) // se han encontrado 2 soluciones
                return true;
            return false;
        }

        int col = cur_cell % 9;
        int row = cur_cell / 9;

        if (_tmp_grid[row][col] == 0) // Si la celda esta vacia (tiene 0)
        {
            for (int n = 1; n <= 9; n++) // para cada numero...
            {
                // si el numero, dado las reglas del sudoku, podria ir en esa posicion...
                if (!existeEnColumna(_tmp_grid, n, col)
                        && !existeEnFila(_tmp_grid, n, row)
                        && !existeEnSubmatriz(_tmp_grid, n, row, col)) {
                    // intenta rellenar la siguiente celda con el numero de la celda donde me encuentro ahora
                    _tmp_grid[row][col] = n;
                    if (solvePuzzle(cur_cell + 1)) // 2 soluciones encontradas
                        return true; // dejamos de buscar
                    _tmp_grid[row][col] = 0; // probamos con otro numero
                }
            }
        } else if (solvePuzzle(cur_cell + 1)) // 2 soluciones encontradas
            return true; // dejamos de buscar

        return false;
    }

    private void desordenarValores(int array[]) {
        // intercambia el valor actual por otro random del array
        for (int i = 0; i < array.length; i++) {
            int j = i + rand.nextInt(array.length - i);
            int t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
    }

    /**
     * Devuelve si el numero ya esta en esa misma columna
     *
     * @param col    columna a comprobar.
     * @param number numero a comprobar.
     * @return verdadero si el numero existe en la columna.
     */
    private boolean existeEnColumna(int[][] puzzle, int number, int col) {
        for (int row = 0; row < 9; row++)
            if (puzzle[row][col] == number)
                return true;
        return false;
    }

    /**
     * Devuelve si el numero ya esta en esa misma fila
     *
     * @param row    columna a comprobar.
     * @param number numero a comprobar.
     * @return verdadero si el numero existe en la fila.
     */
    private boolean existeEnFila(int[][] puzzle, int number, int row) {
        for (int col = 0; col < 9; col++)
            if (puzzle[row][col] == number)
                return true;
        return false;
    }

    /**
     * Si en una sub-matriz 3x3 contiene el valor "number"
     *
     * @param row    fila
     * @param col    columna
     * @param number numero a comprobar
     * @return verdadero si contiene el numero
     */
    private boolean existeEnSubmatriz(int[][] puzzle, int number, int row, int col) {
        int sub_grid_start_row = (row / 3) * 3;
        int sub_grid_start_col = (col / 3) * 3;
        for (int _row = sub_grid_start_row; _row < sub_grid_start_row + 3; _row++)
            for (int _col = sub_grid_start_col; _col < sub_grid_start_col + 3; _col++)
                if (puzzle[_row][_col] == number)
                    return true;
        return false;
    }

    /**
     * Genera un Sudoku a partir del SUdoku resuelto
     */
    private void generaPuzzle() {
        // copia solved_puzzle a puzzle
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                puzzle[row][col] = solved_puzzle[row][col];

        // genera una secuencia de enteros {0,...,80} de forma aleatoria
        int[] cell_sequence = new int[81];
        for (int i = 0; i < 81; i++)
            cell_sequence[i] = i;
        desordenarValores(cell_sequence);

        // pone cada celda a cero
        int count_set_to_zero = 0;
        for (int i = 0; i < 81 && count_set_to_zero < 64; i++) {
            int cur_cell = cell_sequence[i];
            int col = cur_cell % 9;
            int row = cur_cell / 9;
            int sav = puzzle[row][col];
            puzzle[row][col] = 0;
            solution_count = 0;

            // copia auxiliar del puzzle a "_tmp_grid" 
            for (int r = 0; r < 9; r++)
                for (int c = 0; c < 9; c++)
                    _tmp_grid[r][c] = puzzle[r][c];

            if (solvePuzzle(0)) // Si el puzzle admite mas de una solucion
                puzzle[row][col] = sav; // Se revierten los cambios y volvemos al puzzle original
            else
                count_set_to_zero++;
        }
    }

    public void mostrarSolucion() {
        for (int row = 0; row < 9; row++) {
            System.out.print("  ");
            for (int col = 0; col < 9; col++)
                System.out.print(" " + solved_puzzle[row][col]);
            System.out.println();
        }
    }

    public void mostrar() {
        for (int row = 0; row < 9; row++) {
            System.out.print("  ");
            for (int col = 0; col < 9; col++)
                System.out.print(" " + puzzle[row][col]);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SudokuPuzzle sudoku_puzzle = new SudokuPuzzle();
        System.out.println("Puzzle:");
        sudoku_puzzle.mostrar();
        System.out.println();
        System.out.println("Solution:");
        sudoku_puzzle.mostrarSolucion();
    }
}