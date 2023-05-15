package Practicas.AStar_laberinto_2;

public class Main {
    public static void main(String[] args){
        int N = 1000;
        long initial, end;
        long time = 0;
        double pathLength = 0;
        double numSuccess = 0;
        for (int i = 0; i < N; i++) {
            Laberinto l = new Laberinto();
            initial = System.nanoTime();
            l.solve();
            end = System.nanoTime();


            time += (end - initial);
            pathLength += l.getPathLength();
            if(l.getSolution()){
                numSuccess++;
            }
        }

        System.out.println("Porcentaje de solucion: " + numSuccess/N * 100 + "%");
        System.out.println("Longitud media: " + pathLength/numSuccess);
        System.out.println("Tiempo medio: " + (time + .0)/N/1000000 + " ms");
        

        Laberinto l1 = new Laberinto();
        l1.printConsole();
        l1.solve();
        l1.printSalida();
        
    }
}
