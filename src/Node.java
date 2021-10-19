import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private int data;
    private char symbol;
    private Node left;
    private Node right;

    public Node(){}

    /**
     *
     * @param data - frequency of character
     * @param symbol - character
     */
    public Node(int data,char symbol) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.symbol = symbol;
    }

    /**
     *
     * @param data - the addition of the data from node1 and node 2
     * @param n1 - node 1
     * @param n2 - node 2
     * create 2 child nodes to the parent node
     */
    public Node(int data,Node n1 ,Node n2){
        this.data =data;
        this.left = n1;
        this.right = n2;
    }

    /**
     *
     * @return traverses to the right child node
     */
    public Node getRight() { return right; }

    /**
     *
     * @return traverses to the left child node
     */
    public Node getLeft() { return left; }

    /**
     *
     * @return gets character information of the node
     */
    public char getSymbol() { return symbol; }

    /**
     *
     * @return gets frequency info the the node
     */
    public int getData() { return data; }




}
