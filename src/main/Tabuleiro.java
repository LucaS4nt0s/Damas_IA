package main;

import java.util.ArrayList;

/**
 * @author Douglas
 */
public class Tabuleiro implements Cloneable {

    private char[][] matriz;
    private final int TAMANHO = 6;
    private int turno = 0; // 1 para brancas, 2 para pretas
    private boolean comeu = false;

    public Tabuleiro() {
        this.matriz = new char[TAMANHO][TAMANHO];
        inicializar();
    }

    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 2) {
                        matriz[i][j] = '2'; // Pretas
                    } else if (i > 3) {
                        matriz[i][j] = '1'; // Brancas
                    } else {
                        matriz[i][j] = '0'; // Vazio
                    }
                } else {
                    matriz[i][j] = 'X'; // Casa Inválida
                }
            }
        }
        setTurno(1); // Começa com as brancas
    }

    @Override
    public Tabuleiro clone() {
        try {
            Tabuleiro clone = (Tabuleiro) super.clone();
            clone.matriz = new char[TAMANHO][];
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

    private boolean podeComer(int r, int c){
        if(this.matriz[r][c] == 'X' || this.matriz[r][c] == '0'){
            return false;
        }

        int casaOcupada = 0;

        switch (this.matriz[r][c]) {
            case '1':
                for(int i = 1; i <= -1; i = i - 2){
                    for(int j = 1; j <= -1; j = j - 2){
                        if(this.matriz[r+i][c+j] == '2' || this.matriz[r+i][c+j] == '4'){
                            if(this.matriz[r+(2*i)][c+(2*j)] == '0'){
                                return true;
                            }
                        }
                    }
                }
                break;
            case '2':
                for(int i = 1; i <= -1; i = i - 2){
                    for(int j = 1; j <= -1; j = j - 2){
                        if(this.matriz[r+i][c+j] == '1' || this.matriz[r+i][c+j] == '3'){
                            if(this.matriz[r+(2*i)][c+(2*j)] == '0'){
                                return true;
                            }
                        }
                    }
                }
                break;
            case '3':
                for(int i = r + 1; i < 6; i++){
                    casaOcupada = 0;
                    for(int j = c + 1;j < 6; j++){
                        if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            return false;
                        }

                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                    
                    casaOcupada = 0;
                    for(int j = c - 1;j >= 0; j--){
                        if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            return false;
                        }
                        
                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                }
                
                for(int i = r - 1; i >= 0; i--){
                    casaOcupada = 0;
                    for(int j = c + 1; j < 6; j++){
                        if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            return false;
                        }

                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                    
                    casaOcupada = 0;
                    for(int j = c - 1;j >= 0; j--){
                        if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            return false;
                        }
                        
                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                }
                break;
            case '4':
                for(int i = r + 1; i < 6; i++){
                    casaOcupada = 0;
                    for(int j = c + 1;j < 6; j++){
                        if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            return false;
                        }

                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                    
                    casaOcupada = 0;
                    for(int j = c - 1;j >= 0; j--){
                        if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            return false;
                        }
                        
                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                }
                
                for(int i = r - 1; i >= 0; i--){
                    casaOcupada = 0;
                    for(int j = c + 1;j < 6; j++){
                        if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            return false;
                        }

                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                    
                    casaOcupada = 0;
                    for(int j = c - 1;j >= 0; j--){
                        if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            return false;
                        }
                        
                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                }
                break;
            default:
                break;
        }

        return false;              
    }
    
    private boolean verificarCasaOrigemVálida(int r, int c){
        return !(this.matriz[r][c] == 'X' || this.matriz[r][c] == '0');
    }

    private boolean verificarCasaDestinoVálidaComum(int r1, int c1, int r2, int c2){
        if (this.matriz[r2][c2] != '0') {
            return false;
        }

        switch (this.matriz[r1][c1]) {
            case '1': // brancas
                if (r2 < r1){ // Brancas só podem mover para cima
                    if (this.matriz[r2][c2] == '0') { // A casa de destino deve estar vazia
                        if(r2 == r1 - 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                            this.matriz[r2][c2] = this.matriz[r1][c1];
                            if (this.matriz[r2][c2] == '1' && r2 == 0) {
                                this.matriz[r2][c2] = '3';
                            }
                            this.comeu = false;
                            this.matriz[r1][c1] = '0';
                            return true;
                        } else if (r2 == r1 - 2 && (c2 == c1 - 2 || c2 == c1 + 2)) { // Verifica se há uma peça adversária para capturar
                            int tempR = r1 - 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (this.matriz[tempR][tempC] == '2' || this.matriz[tempR][tempC] == '4') { // Verifica se a peça adversária está na casa intermediária
                                this.matriz[r2][c2] = this.matriz[r1][c1];
                                if (this.matriz[r2][c2] == '1' && r2 == 0) {
                                    this.matriz[r2][c2] = '3';
                                }
                                this.matriz[r1][c1] = '0';
                                this.matriz[tempR][tempC] = '0';
                                this.comeu = podeComer(r2, c2);
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false; 
                        }
                    }
                } 
                break;
            case '2': 
                if (r2 > r1){ 
                    if (this.matriz[r2][c2] == '0') { 
                        if(r2 == r1 + 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                             this.matriz[r2][c2] = this.matriz[r1][c1];
                            if (this.matriz[r2][c2] == '2' && r2 == 5) {
                                this.matriz[r2][c2] = '4';
                            }
                            this.matriz[r1][c1] = '0';
                            this.comeu = false;
                            return true;
                        } else if (r2 == r1 + 2 && (c2 == c1 - 2 || c2 == c1 + 2)) {
                            int tempR = r1 + 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (this.matriz[tempR][tempC] == '1' || this.matriz[tempR][tempC] == '3') { 
                                this.matriz[r2][c2] = this.matriz[r1][c1];
                                if (this.matriz[r2][c2] == '2' && r2 == 5) {
                                    this.matriz[r2][c2] = '4';
                                }
                                this.matriz[r1][c1] = '0';
                                this.matriz[tempR][tempC] = '0';
                                this.comeu = podeComer(r2, c2);
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false; 
                        }
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private boolean verificarCasaDestinoVálidaDama(int r1, int c1, int r2, int c2){
        if (this.matriz[r2][c2] != '0') {
            return false;
        }

        if (c1 == c2 || r1 == r2){ // previne movimento horizontal ou vertical
            return false;
        }

        int contPeca = 0;
        int linhaParada = -1;
        int colunaParada = -1;
        int proxLinha = -1;
        int proxColuna = -1;
        int tempR = r1;
        int tempC = c1;

        switch (this.matriz[r1][c1]){
            case '3':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && this.matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '3';
                        this.comeu = false;
                        return true;
                    }
                    if(r2 > r1){
                        while(r2 > tempR){
                            tempR++;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(this.matriz[tempR][tempC] == '2' || this.matriz[tempR][tempC] == '4'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR + 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (this.matriz[tempR][tempC] == '1' || this.matriz[tempR][tempC] == '3') {
                                return false;
                            }
                        }
                    } else if(r2 < r1){
                        while(r2 < tempR){
                            tempR--;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(this.matriz[tempR][tempC] == '2' || this.matriz[tempR][tempC] == '4'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR - 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (this.matriz[tempR][tempC] == '1' || this.matriz[tempR][tempC] == '3') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        this.matriz[r1][c1] = '0';
                        this.matriz[proxLinha][proxColuna] = '3';
                        this.matriz[linhaParada][colunaParada] = '0';
                        this.comeu = podeComer(proxLinha, proxColuna);
                        return true;
                    } else if (contPeca == 0){
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '3';
                        this.comeu = false;
                        return true;
                    }
                }
            break;
            case '4':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && this.matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '4';
                        return true;
                    }
                    if(r2 > r1){
                        while(r2 > tempR){
                            tempR++;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(this.matriz[tempR][tempC] == '1' || this.matriz[tempR][tempC] == '3'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR + 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (this.matriz[tempR][tempC] == '2' || this.matriz[tempR][tempC] == '4') {
                                return false;
                            }
                        }
                    } else if(r2 < r1){
                        while(r2 < tempR){
                            tempR--;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(this.matriz[tempR][tempC] == '1' || this.matriz[tempR][tempC] == '3'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR - 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (this.matriz[tempR][tempC] == '2' || this.matriz[tempR][tempC] == '4') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        this.matriz[r1][c1] = '0';
                        this.matriz[proxLinha][proxColuna] = '4';
                        this.matriz[linhaParada][colunaParada] = '0';
                        this.comeu = podeComer(proxLinha, proxColuna);
                        return true;
                    } else if (contPeca == 0){
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '4';
                        this.comeu = false;
                        return true;
                    }
                }
            break;
            default:
            break;
        }
        return false;
    }

    public boolean fazerMovimento(int r1, int c1, int r2, int c2) {
        
        boolean casaDeOrigemValida = verificarCasaOrigemVálida(r1, c1); // verificar se a casa de origem é válida
        if (!casaDeOrigemValida) {
            return false; // A casa de origem é inválida ou vazia
        }

        if ((c1 == c2) || (r1 == r2)){
            return false; // Impede movimentos verticais ou horizontais
        }

        boolean podeMover; // verifica se é um movimento válido
        if (this.matriz[r1][c1] == '1' || this.matriz[r1][c1] == '2'){
            podeMover = verificarCasaDestinoVálidaComum(r1, c1, r2, c2);
        } else{
            podeMover = verificarCasaDestinoVálidaDama(r1, c1, r2, c2);
        }
        
        System.out.println(podeMover);
        if (podeMover && this.comeu == false){
            if (this.turno == 1){
                this.turno = 2;
            } else {
                this.turno = 1;
            }
        }

        return podeMover;
    }

    public boolean verificaMovimento () {
        return true;
    }
    

    public char[][] getMatriz() {
        return this.matriz;
    }

    public void setMatriz(char[][] matriz) {
        this.matriz = matriz;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }
}
