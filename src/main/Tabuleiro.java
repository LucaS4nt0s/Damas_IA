package main;

/**
 * @author Douglas
 */
public class Tabuleiro implements Cloneable {

    private int[][] matriz;
    private final int TAMANHO = 6;

    public Tabuleiro() {
        this.matriz = new int[TAMANHO][TAMANHO];
        inicializar();
    }

    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 2) {
                        matriz[i][j] = 2; // Pretas
                    } else if (i > 3) {
                        matriz[i][j] = 1; // Brancas
                    } else {
                        matriz[i][j] = 0; // Vazio
                    }
                } else {
                    matriz[i][j] = -1; // Casa Inválida
                }
            }
        }
    }

    @Override
    public Tabuleiro clone() {
        try {
            Tabuleiro clone = (Tabuleiro) super.clone();
            clone.matriz = new int[TAMANHO][];
            for (int i = 0; i < TAMANHO; i++) {
                clone.matriz[i] = this.matriz[i].clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    
    /*
        Implementação dos métodos - getMovimentosPossiveis(), fazerMovimento(), etc
    */

    public boolean moverPecaLogica(int r1, int c1, int r2, int c2) {
        
        if (matriz[r1][c1] == -1 || matriz[r1][c1] == 0) { // Impede que o jogador seja capaz de selecionar casas claras ou vazias
            return false; 
        }

        switch (this.matriz[r1][c1]){
            case 1: // Peça Branca
                if (c1 == c2){
                    return false; // Impede movimentos verticais
                }
                if (r2 < r1){ // Brancas só podem mover para cima
                    if (this.matriz[r2][c2] == 0) { // A casa de destino deve estar vazia
                        // Transfere o valor (seja 1, 2, 3 ou 4) para a nova posição
                        if(r2 == r1 - 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                            this.matriz[r2][c2] = this.matriz[r1][c1];
                            this.matriz[r1][c1] = 0;
                            return true;
                        } else if (r2 == r1 - 2 && (c2 == c1 - 2 || c2 == c1 + 2)) { // Verifica se há uma peça adversária para capturar
                            int tempR = r1 - 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (this.matriz[tempR][tempC] == 2 || this.matriz[tempR][tempC] == 4) { // Verifica se a peça adversária está na casa intermediária
                                this.matriz[r2][c2] = this.matriz[r1][c1];
                                this.matriz[r1][c1] = 0;
                                this.matriz[tempR][tempC] = 0;
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false; 
                        }
                    }
                    return false; 
                } 
                break;
            case 2: // Peça Preta
                if (c1 == c2){
                    return false; // Impede movimentos verticais
                }
                if (r2 > r1){ // Pretas só podem mover para baixo
                    if (this.matriz[r2][c2] == 0) { // A casa de destino deve estar vazia
                        // Transfere o valor (seja 1, 2, 3 ou 4) para a nova posição
                        if(r2 == r1 + 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                            this.matriz[r2][c2] = this.matriz[r1][c1];
                            this.matriz[r1][c1] = 0;
                            return true;
                        } else if (r2 == r1 + 2 && (c2 == c1 - 2 || c2 == c1 + 2)) { // Verifica se há uma peça adversária para capturar
                            int tempR = r1 + 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (this.matriz[tempR][tempC] == 1 || this.matriz[tempR][tempC] == 3) { // Verifica se a peça adversária está na casa intermediária
                                this.matriz[r2][c2] = this.matriz[r1][c1];
                                this.matriz[r1][c1] = 0;
                                this.matriz[tempR][tempC] = 0;
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false; 
                        }
                    }
                    return false; 
                }
                break;
            case 3: // Dama Branca
            case 4: // Dama Preta
                // Damas podem mover em qualquer direção, então não há restrição aqui
                break;
        }
        

        // Promoção simples para Dama (opcional)
        if (this.matriz[r2][c2] == 2 && r2 == 5) {
            this.matriz[r2][c2] = 4;
        }
        if (this.matriz[r2][c2] == 1 && r2 == 0) {
            this.matriz[r2][c2] = 3;
        }

        return false;
    }

    public boolean verificaMovimento () {
        return true;
    }
    

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }
}
