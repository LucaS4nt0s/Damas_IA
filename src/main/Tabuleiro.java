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
    private boolean obrigadoComer = false;
    private ArrayList<Peca> pecas = new ArrayList<>();
    private int id;
    private int pecasPretas = 6;
    private int pecasBrancas = 6;

    public Tabuleiro() {
        this.matriz = new char[TAMANHO][TAMANHO];
        inicializar();
    }

    private void inicializar() {
        pecas.clear();
        this.id = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 2) {
                        matriz[i][j] = '2'; // Pretas
                        Peca peca = new Peca(this.id, '2', i, j);
                        pecas.addLast(peca);
                        id++;
                    } else if (i > 3) {
                        matriz[i][j] = '1'; // Brancas
                        Peca peca = new Peca(this.id, '1', i, j);
                        pecas.addFirst(peca);
                        id++;
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

        int casaOcupada;

        System.out.println("r: " + r + " c: " + c);
        System.out.println(this.matriz[r][c]);
        switch (this.matriz[r][c]) {
            case '1':
                if(r < 2){
                    return false;
                }
                
                if(c < 4){
                    if(this.matriz[r-1][c+1] == '2' || this.matriz[r-1][c+1] == '4'){
                        if(this.matriz[r-2][c+2] == '0'){
                            return true;
                        }
                    }
                }
                
                if(c > 1){
                    if(this.matriz[r-1][c-1] == '2' || this.matriz[r-1][c-1] == '4'){
                        if(this.matriz[r-2][c-2] == '0'){
                            return true;
                        }
                    }
                }
                
                break;
            case '2':
                if(r > 3){
                    return false;
                }

                if(c < 4){
                    if(this.matriz[r+1][c+1] == '1' || this.matriz[r+1][c+1] == '3'){
                        if(this.matriz[r+2][c+2] == '0'){
                            return true;
                        }
                    }
                }

                if(c > 1){
                    if(this.matriz[r+1][c-1] == '1' || this.matriz[r+1][c-1] == '3'){
                        if(this.matriz[r+2][c-2] == '0'){
                            return true;
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
                            break;
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
                            break;
                        }
                        
                        if(this.matriz[i][j] == '0' && casaOcupada == 1){
                            return true;
                        }
                    }
                }
                
                for(int i = r - 1; i >= 0; i--){
                    casaOcupada = 0;
                    for(int j = c + 1;j < 6; j++){
                        if(this.matriz[i][j] == '2' || this.matriz[i][j] == '4'){
                            casaOcupada++;
                        } else if(this.matriz[i][j] == '1' || this.matriz[i][j] == '3'){
                            break;
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
                            break;
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
                            break;
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
                            break;
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
                            break;
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
                            break;
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

    private boolean verificarAlgumaPecaPodeComer(){
        for(Peca peca: pecas){
            if(this.turno == 1){
                if(peca.getTipo() == '1'){
                    if(podeComer(peca.getLinha(), peca.getColuna())){
                        return true;
                    }
                }
            } else if (this.turno == 2){
                if(peca.getTipo() == '2'){
                    if(podeComer(peca.getLinha(), peca.getColuna())){
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    private boolean verificarCasaOrigemVálida(int r, int c){
        if(this.matriz[r][c] == 'X' || this.matriz[r][c] == '0'){
            return false;
        }
        
        if(obrigadoComer){
            return podeComer(r, c);
        } else {
            return true;
        }
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
                            if(obrigadoComer){
                                return false;
                            } 
                            this.matriz[r2][c2] = this.matriz[r1][c1];
                            if (this.matriz[r2][c2] == '1' && r2 == 0) {
                                this.matriz[r2][c2] = '3';
                            }
                            this.comeu = false;
                            this.matriz[r1][c1] = '0';
                            for(Peca peca: pecas){
                                if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                    peca.setLinha(r2);
                                    peca.setColuna(c2);
                                }
                                if (peca.getLinha() == 0 && peca.getTipo() == '1') {
                                        peca.setTipo('3');
                                }
                            }
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
                                for(Peca peca: pecas){
                                    if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                        peca.setLinha(r2);
                                        peca.setColuna(c2);
                                    }
                                    if (peca.getLinha() == 0 && peca.getTipo() == '1') {
                                        peca.setTipo('3');
                                    }
                                }
                                pecas.removeIf(p -> p.getLinha() == tempR && p.getColuna() == tempC);
                                pecasPretas--;
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
                            if(obrigadoComer){
                                return false;
                            }
                            this.matriz[r2][c2] = this.matriz[r1][c1];
                            if (this.matriz[r2][c2] == '2' && r2 == 5) {
                                this.matriz[r2][c2] = '4';
                            }
                            this.matriz[r1][c1] = '0';
                            this.comeu = false;
                            for(Peca peca: pecas){
                                if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                    peca.setLinha(r2);
                                    peca.setColuna(c2);
                                }
                                if (peca.getLinha() == 5 && peca.getTipo() == '2') {
                                        peca.setTipo('4');
                                }
                            }
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
                                for(Peca peca: pecas){
                                    if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                        peca.setLinha(r2);
                                        peca.setColuna(c2);
                                    }
                                    if (peca.getLinha() == 5 && peca.getTipo() == '2') {
                                        peca.setTipo('4');
                                    }
                                }
                                pecas.removeIf(p -> p.getLinha() == tempR && p.getColuna() == tempC);
                                pecasBrancas--;
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
                        if(obrigadoComer){
                            return false;
                        }
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '3';
                        this.comeu = false;
                        for(Peca peca: pecas){
                            if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                peca.setLinha(proxLinha);
                                peca.setColuna(proxColuna);
                            }
                        }
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
                        for(Peca peca: pecas){
                            if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                peca.setLinha(proxLinha);
                                peca.setColuna(proxColuna);
                            }
                        }
                        final int finalLinha = linhaParada;
                        final int finalColuna = colunaParada;
                        pecas.removeIf(p -> p.getLinha() == finalLinha && p.getColuna() == finalColuna);
                        this.comeu = podeComer(proxLinha, proxColuna);
                        pecasPretas--;

                        return true;
                    } else if (contPeca == 0){
                        if(obrigadoComer){
                            return false;
                        }
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '3';
                        this.comeu = false;
                        for(Peca peca: pecas){
                            if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                peca.setLinha(r2);
                                peca.setColuna(c2);
                            }
                        }
                        return true;
                    }
                }
            break;
            case '4':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && this.matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        if(obrigadoComer){
                            return false;
                        }
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
                        for(Peca peca: pecas){
                            if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                peca.setLinha(proxLinha);
                                peca.setColuna(proxColuna);
                            }
                        }
                        final int finalLinha = linhaParada;
                        final int finalColuna = colunaParada;
                        pecas.removeIf(p -> p.getLinha() == finalLinha && p.getColuna() == finalColuna);
                        this.comeu = podeComer(proxLinha, proxColuna);
                        pecasBrancas--;

                        return true;
                    } else if (contPeca == 0){
                        if(obrigadoComer){
                            return false;
                        }
                        this.matriz[r1][c1] = '0';
                        this.matriz[r2][c2] = '4';
                        for(Peca peca: pecas){
                            if(peca.getLinha() == r1 && peca.getColuna() == c1){
                                peca.setLinha(r2);
                                peca.setColuna(c2);
                            }
                        }
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
        
        obrigadoComer = verificarAlgumaPecaPodeComer();
       
        if (!verificarCasaOrigemVálida(r1, c1)) {
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

        obrigadoComer = false;

        if(pecasBrancas == 0 || pecasPretas == 0){
            System.out.println("Fim de jogo");
            inicializar();
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

    public ArrayList<Peca> getPecas(){
        return this.pecas;
    }

    public boolean getObrigadoComer(){
        return this.obrigadoComer;
    }

    public int getPecasBrancas(){
        return pecasBrancas;
    }

    public int getPecasPretas(){
        return pecasPretas;
    }
}
