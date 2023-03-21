public class Node implements Comparable<Node>{
    private int col;
    private int row;
    private int cost;
    private int manhattan;
    private int f;
    private Node parent;

    public Node(int col, int row, int cost, int manhattan, Node parent) {
        this.col = col;
        this.row = row;
        this.cost = cost;
        this.manhattan = manhattan;
        this.f = manhattan + cost;
        this.parent = parent;
    }

    public int col(){
        return col;
    }

    public int row(){
        return row;
    }

    public int cost(){
        return cost;
    }

    public int manhattan(){
        return manhattan;
    }
    public Node parent(){return parent;}
    public int f(){
        return f;
    }

    public void newCost(int c){ //nuevo coste de la funcion heuristica
        cost = c;
        f = manhattan + cost;
    }

    public void newManhattan(int m){
        manhattan = m;
        f = manhattan + cost;
    }

    public void newF(int c, int m){
        cost = c;
        manhattan = m;
        f = manhattan + cost;
    }

    @Override
    public int hashCode(){
        return row*13+col*29;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Node)){
            return false;
        }
        Node node = (Node) obj;
        return node.row == this.row && node.col == this.col;
    }


    @Override
    public int compareTo(Node o) {
        int comp = 0;
        comp = Integer.compare(f, o.f);
        if(comp == 0){
            comp = Integer.compare(row, o.row);
            if(comp == 0){
                comp = Integer.compare(col, o.col);
            }
        }
        return comp;
    }
}
