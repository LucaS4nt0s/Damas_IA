package main;

public class Peca {
    private char tipo;
    private int linha;
    private int coluna;

    public Peca(char tipo, int linha, int coluna){
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }


    public char getTipo(){
        return this.tipo;
    }

    public void setTipo(char novoTipo){
        this.tipo = novoTipo;
    }

    public int getLinha(){
        return this.linha;
    }

    public void setLinha(int novaLinha){
        if(novaLinha >= 0 && novaLinha <= 5){
            this.linha = novaLinha;
        }
    }

    public int getColuna(){
        return this.coluna;
    }

    public void setColuna(int novaColuna){
        if(novaColuna >= 0 && novaColuna <= 5){
            this.coluna = novaColuna;
        }
    }

    @Override
    public String toString() {
        return "Peca{" +
                "tipo=" + tipo +
                ", linha=" + linha +
                ", coluna=" + coluna +
                '}';
    }
}   
