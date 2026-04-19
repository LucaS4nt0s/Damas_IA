package main;

import java.util.ArrayList;

/**
 *
 * @author Douglas
 */
public class Node {
    
    // casa de origem da jogada que gerou este nó
    private char origin;
    // casa de destino da jogada que gerou este nó
    private char dest;
    // turno armazenado neste estado (true=brancas, false=pretas)
    private boolean turn; // true = brancas; false = pretas;
    // matriz do tabuleiro neste nó da árvore
    private char[][] matrix;
    // valor minmax calculado para este nó
    private int minMax;
    // filhos (possíveis próximos estados)
    private ArrayList<Node> children;
    
    // cria nó com lista de filhos vazia e minmax ainda não calculado
    public Node () {
        this.children = new ArrayList<>();
        this.minMax = Integer.MIN_VALUE;
    }

    // retorna valor minmax atual
    public int getMinMax() {
        return minMax;
    }

    // define valor minmax do nó
    public void setMinMax(int minMax) {
        this.minMax = minMax;
    }
    
    // retorna lista de filhos
    public ArrayList<Node> getChild (){
        return this.children;
    }
    
    // adiciona um filho na árvore
    public void addChild (Node child){
        this.children.add(child);
    }
    
    // retorna origem
    public char getOrigin() {
        return origin;
    }

    // define origem
    public void setOrigin(char origin) {
        this.origin = origin;
    }

    // retorna destino
    public char getDest() {
        return dest;
    }

    // define destino
    public void setDest(char dest) {
        this.dest = dest;
    }

    // retorna de quem é o turno neste estado
    public boolean isTurn() {
        return turn;
    }

    // define de quem é o turno neste estado
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    // retorna a matriz do nó
    public char[][] getMatrix() {
        return matrix;
    }

    // define a matriz do nó
    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }
    
    // método auxiliar antigo para setar matriz (mantido por compatibilidade)
    public void setMovimento(char[][] matriz){
        this.matrix = matriz;
    }
    
}
