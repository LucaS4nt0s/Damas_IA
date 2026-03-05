package main;

public class Jogada {
    private char origem;
    private char destino;
    
    public Jogada(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino){
        this.origem = Codificadora.codificar(linhaOrigem, linhaDestino);
        this.destino = Codificadora.codificar(linhaDestino, colunaDestino);
    }

    public char getOrigem(){
        return origem;
    }

    public char getDestino(){
        return destino;
    }

    public void setOrigem(int linhaOrigem, int colunaOrigem){
        this.origem = Codificadora.codificar(linhaOrigem, colunaOrigem);
    }

    public void setOrigem(char origem){
        int pos[] = {0,0};

        pos = Codificadora.decodificar(origem);

        this.origem = Codificadora.codificar(pos[0], pos[1]);
    }

    public void setDestino(int linhaDestino, int colunaDestino){
        this.destino = Codificadora.codificar(linhaDestino, colunaDestino);
    }

    public void setDestino(char destino){
        int pos[] = {0,0};

        pos = Codificadora.decodificar(destino);

        this.destino = Codificadora.codificar(pos[0], pos[1]);
    }
}
