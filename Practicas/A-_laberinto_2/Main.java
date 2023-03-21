public class Main {
    public static void main(String[] args) {
        Laberinto l1 = new Laberinto();
        // Laberinto l1 = new Laberinto(60,80);
        l1.printConsole();
        l1.solve();
        l1.printSalida();
    }
}
