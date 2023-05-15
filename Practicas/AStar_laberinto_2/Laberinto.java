package Practicas.AStar_laberinto_2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Laberinto {

    static final int ROWS = 60;
    static final int COLS = 80;
    static final double OBS = 0.3; // Probabilidad de obstaculo
    private final char[][] maze;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private boolean solution;
    private int pathLength;

    public Laberinto() {
        maze = new char[ROWS][COLS];
        solution = false;
        pathLength = 0;
        initializeMaze(maze);
    }

    public boolean getSolution() {
        return solution;
    }

    public int getPathLength() {
        return pathLength;
    }

    private void initializeMaze(char[][] lab) {

        // Obstacles
        int numObstacles = (int) (ROWS * COLS * OBS); // numero de obstaculos que tiene que haber en el tablero
        int prob = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (numObstacles == (COLS - j - 1) + (ROWS - i - 1) * COLS) { // si el numero de obstaculos es igual al
                                                                              // de casillas restantes
                    lab[i][j] = '■'; // rellenamos con obstaculos
                    numObstacles--;
                } else if (numObstacles == 0) { // si el numero de obstaculos es 0
                    lab[i][j] = ' '; // rellenamos con huecos
                } else {
                    prob = (int) (Math.random() * (101)); // Random number between 0 and 100
                    if (prob <= OBS * 100) {
                        lab[i][j] = '■';
                        numObstacles--;
                    } else {
                        lab[i][j] = ' ';
                    }
                }
            }
        }

        // Initial state

        int randRow = (int) (Math.floor(Math.random() * ROWS));
        int randCol = (int) (Math.floor(Math.random() * COLS));
        while (lab[randRow][randCol] == '■') { // hay que checkear que no sea un obstaculo
            randRow = (int) (Math.floor(Math.random() * ROWS));
            randCol = (int) (Math.floor(Math.random() * COLS));
        }
        lab[randRow][randCol] = 'I';
        startCol = randCol;
        startRow = randRow;

        // Final state

        do {
            randRow = (int) (Math.floor(Math.random() * ROWS));
            randCol = (int) (Math.floor(Math.random() * COLS));
        } while (lab[randRow][randCol] == 'I' || lab[randRow][randCol] == '■');
        endRow = randRow;
        endCol = randCol;
        lab[randRow][randCol] = 'G';

    }

    public void solve() {
        Node initial = new Node(startCol, startRow, 0, manhattanToEnd(startRow, startRow), null);
        ArrayList<Node> closedset = new ArrayList<>();
        SortedSet<Node> openset = new TreeSet<>();
        openset.add(initial);
        Map<Node, Integer> g = new HashMap<>();
        g.put(initial, 0);

        while (!openset.isEmpty()) {
            Node current = openset.first();
            if (isGoal(current)) {
                constructPath(current);
                solution = true;
                return;
            }
            openset.remove(current);
            closedset.add(current);
            for (Node neighbor : neighborNodes(current)) {
                if (!closedset.contains(neighbor)) {
                    int tentativeG = g.get(current) + 1;
                    // que el nuevo nodo no este en la lista de abiertos o que para ese mismo nodo
                    // hayamos encontrado un camino con menor coste
                    if (!openset.contains(neighbor) || tentativeG < g.get(neighbor)) {
                        g.put(neighbor, tentativeG);
                        openset.add(neighbor);
                    }
                }
            }
        }
    }

    private boolean isGoal(Node n) {
        return n.row() == endRow && n.col() == endCol;
    }

    private List<Node> neighborNodes(Node current) {
        List<Node> neighbors = new ArrayList<>();

        int row = current.row();
        int col = current.col();

        if (col < COLS - 1 && maze[row][col + 1] != '■' && maze[row][col + 1] != 'I') {
            neighbors.add(new Node(col + 1, row, current.cost() + 1, manhattanToEnd(row, col + 1), current));
        }
        if (col > 0 && maze[row][col - 1] != '■' && maze[row][col - 1] != 'I') {
            neighbors.add(new Node(col - 1, row, current.cost() + 1, manhattanToEnd(row, col - 1), current));
        }
        if (row < ROWS - 1 && maze[row + 1][col] != '■' && maze[row + 1][col] != 'I') {
            neighbors.add(new Node(col, row + 1, current.cost() + 1, manhattanToEnd(row + 1, col), current));
        }
        if (row > 0 && maze[row - 1][col] != '■' && maze[row - 1][col] != 'I') {
            neighbors.add(new Node(col, row - 1, current.cost() + 1, manhattanToEnd(row - 1, col), current));
        }

        return neighbors;
    }

    private void constructPath(Node n) {

        while (n.parent() != null) {
            n = n.parent();
            maze[n.row()][n.col()] = '+';
            pathLength++;
        }
        pathLength--;
        maze[startRow][startCol] = 'I';
    }

    private int manhattan(int sRow, int sCol, int fRow, int fCol) {
        return Math.abs(sRow - fRow) + Math.abs(sCol - fCol);
    }

    private int manhattanToEnd(int sRow, int sCol) {
        return manhattan(sRow, sCol, endRow, endCol);
    }

    public void printConsole() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
        if (solution) {
            System.out.println("SOLUTION FOUND");
        } else {
            System.out.println("SOLUTION NOT FOUND");
        }
        System.out.println("Path length: " + pathLength);
    }

    public void printSalida() {
        try {
            PrintWriter pw = new PrintWriter("salida.txt");

            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    pw.print(maze[i][j]);
                }
                pw.println();
            }
            if (solution) {
                pw.println("SOLUTION FOUND");
            } else {
                pw.println("SOLUTION NOT FOUND");
            }
            pw.println("Path length: " + pathLength);
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
