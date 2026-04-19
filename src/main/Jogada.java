package main;

// classe que representa um movimento (origem -> destino)
public class Jogada {
    // origem e destino ficam salvos como letras do mapa (a..r)
    private char origem;
    private char destino;
    
    // construtor usando coordenadas da matriz
    public Jogada(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino){
        this.origem = Codificadora.codificar(linhaOrigem, colunaOrigem);
        this.destino = Codificadora.codificar(linhaDestino, colunaDestino);
    }

    // retorna a casa de origem
    public char getOrigem(){
        return this.origem;
    }

    // retorna a casa de destino
    public char getDestino(){
        return this.destino;
    }

    // define origem com coordenadas
    public void setOrigem(int linhaOrigem, int colunaOrigem){
        this.origem = Codificadora.codificar(linhaOrigem, colunaOrigem);
    }

    // define origem com letra
    public void setOrigem(char origem){
        int pos[];

        pos = Codificadora.decodificar(origem);

        this.origem = Codificadora.codificar(pos[0], pos[1]);
    }

    // define destino com coordenadas
    public void setDestino(int linhaDestino, int colunaDestino){
        this.destino = Codificadora.codificar(linhaDestino, colunaDestino);
    }

    // define destino com letra
    public void setDestino(char destino){
        int pos[];

        pos = Codificadora.decodificar(destino);

        this.destino = Codificadora.codificar(pos[0], pos[1]);
    }

    // mostra a jogada em formato de texto (ajuda no debug)
    @Override
    public String toString(){
        return "Jogada{" +
                "origem=" + origem +
                ", destino=" + destino +
                "}";
    }
}
