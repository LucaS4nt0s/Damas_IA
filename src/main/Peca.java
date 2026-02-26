package main;

public class Peca {
    private final int id;
    private char tipo;
    private int linha;
    private int coluna;

    public Peca(int id, char tipo, int linha, int coluna){
        this.id = id;
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getID(){
        return this.id;
    }

    public char getTipo(){
        return this.tipo;
    }

    public void setTipo(char novoTipo){
        if(this.tipo == '1' && novoTipo == '3'){
            this.tipo = novoTipo;
        } else if(this.tipo == '2' && novoTipo == '4'){
            this.tipo = novoTipo;
        }
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
                "id=" + id +
                ", tipo=" + tipo +
                ", linha=" + linha +
                ", coluna=" + coluna +
                '}';
    }
}   
