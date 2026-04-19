package main;

// classe simples para representar uma peça no tabuleiro
public class Peca {
    // tipo da peça ('1','2','3','4')
    private char tipo;
    // linha atual da peça
    private int linha;
    // coluna atual da peça
    private int coluna;

    // cria uma peça com tipo e posição
    public Peca(char tipo, int linha, int coluna){
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }


    // retorna o tipo da peça
    public char getTipo(){
        return this.tipo;
    }

    // atualiza o tipo (ex: promoção para dama)
    public void setTipo(char novoTipo){
        this.tipo = novoTipo;
    }

    // retorna a linha atual
    public int getLinha(){
        return this.linha;
    }

    // atualiza linha se estiver dentro do tabuleiro
    public void setLinha(int novaLinha){
        if(novaLinha >= 0 && novaLinha <= 5){
            this.linha = novaLinha;
        }
    }

    // retorna a coluna atual
    public int getColuna(){
        return this.coluna;
    }

    // atualiza coluna se estiver dentro do tabuleiro
    public void setColuna(int novaColuna){
        if(novaColuna >= 0 && novaColuna <= 5){
            this.coluna = novaColuna;
        }
    }

    // mostra a peça como texto (ajuda no debug)
    @Override
    public String toString() {
        return "Peca{" +
                "tipo=" + tipo +
                ", linha=" + linha +
                ", coluna=" + coluna +
                '}';
    }
}   
