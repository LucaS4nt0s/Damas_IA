package main;

import java.util.ArrayList;

/**
 * @author Douglas
 */
public class Tabuleiro implements Cloneable {

    private char[][] matriz;
    private final int TAMANHO = 6;
    private int turno = 0; // 1 para brancas, 2 para pretas
    private boolean comeuBrancas = false;
    private boolean comeuPretas = false;
    private boolean obrigadoComer = false;
    private int pecasPretas = 6;
    private int pecasBrancas = 6;

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

    public ArrayList<Peca> verificarOndeTemPeca(char[][] matriz){
        ArrayList<Peca> pecas = new ArrayList<>();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < matriz[i].length; j += 2) {
                switch (matriz[i][j]){
                    case '1', '2', '3', '4':
                        Peca novaPeca = new Peca(matriz[i][j], i, j);
                        pecas.add(novaPeca);
                        break;
                    default:
                        break;
                }
            }
        }
        return pecas;
    }

    public ArrayList<Jogada> retornaJogadasPossiveis(char[][] matriz, boolean vez){
        ArrayList<Jogada> jogadasPossiveis = new ArrayList<>();
        ArrayList<Peca> pecas = verificarOndeTemPeca(matriz);

        if(verificarAlgumaPecaPodeComer(vez, matriz)){
            for(Peca peca:pecas){
                if((peca.getTipo() == '1' || peca.getTipo() == '3') && vez == false){
                    continue;
                }
                if((peca.getTipo() == '2' || peca.getTipo() == '4') && vez == true){
                    continue;
                }
                if(podeComer(peca.getLinha(), peca.getColuna(), vez, matriz)){
                    switch (peca.getTipo()) {
                        case '1', '2':
                            for(int i = 2; i >= -2; i -= 4){
                                for(int j = 2; j >= -2; j -= 4){
                                    if((peca.getLinha() + i) >= 0 && (peca.getLinha() + i) <= 5 && (peca.getColuna() + j) >= 0 && (peca.getColuna() + j) <= 5){
                                        if(validaMovimentoComumSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j, matriz)){
                                            Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j);
                                            jogadasPossiveis.add(novaJogada);
                                        }
                                    }
                                }
                            }
                            break;
                        case '3', '4':
                            for(int i = 2; peca.getLinha() + i < 6 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i, matriz)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                            }

                            for(int i = 2; peca.getLinha() + i < 6 && peca.getColuna() - i >= 0; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i, matriz)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i);
                                    jogadasPossiveis.add(novaJogada);
                                    
                                }
                            }

                            for(int i = 2; peca.getLinha() - i >= 0 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i, matriz)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                            }

                            for(int i = 2; peca.getLinha() - i >= 0 && peca.getColuna() - i >= 0; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i, matriz)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                } 
            }
        } else{
            for(Peca peca:pecas){
                if((peca.getTipo() == '1' || peca.getTipo() == '3') && vez == false){
                    continue;
                }
                if((peca.getTipo() == '2' || peca.getTipo() == '4') && vez == true){
                    continue;
                }
                switch (peca.getTipo()) {
                    case '1', '2':
                        for(int i = 1; i >= -1; i -= 2){
                                for(int j = 1; j >= -1; j -= 2){
                                    if((peca.getLinha() + i) >= 0 && (peca.getLinha() + i) <= 5 && (peca.getColuna() + j) >= 0 && (peca.getColuna() + j) <= 5){
                                        if(validaMovimentoComumSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j, matriz)){
                                            Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j);
                                            jogadasPossiveis.add(novaJogada);
                                        }
                                    }
                                }
                            }
                        break;
                    case '3', '4':
                        for(int i = 1; peca.getLinha() + i < 6 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i, matriz)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                        }

                        for(int i = 1; peca.getLinha() + i < 6 && peca.getColuna() - i >= 0; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i, matriz)){
                                Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i);
                                jogadasPossiveis.add(novaJogada);
                            }
                        }

                        for(int i = 1; peca.getLinha() - i >= 0 && peca.getColuna() + i < 6; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i, matriz)){
                                Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i);
                                jogadasPossiveis.add(novaJogada);
                            }
                        }

                        for(int i = 1; peca.getLinha() - i >= 0 && peca.getColuna() - i >= 0; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i, matriz)){
                                Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i);
                                jogadasPossiveis.add(novaJogada);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return jogadasPossiveis;
    }

    public boolean validaMovimentoComumSemAlterarTabuleiro(int r1, int c1, int r2, int c2, char[][] matriz){
        if (!dentroLimites(r1, c1) || !dentroLimites(r2, c2)) {
            return false;
        }

        if (matriz[r2][c2] != '0') {
            return false;
        }

        switch (matriz[r1][c1]) {
            case '1': // brancas
                if(comeuBrancas && r1 < 4){
                    if(c1 < 4){
                        if(r2 == r1 + 2 && c2 == c1 + 2){
                            if(matriz[r1+1][c1+1] == '2' || matriz[r1+1][c1+1] == '4'){
                                if(matriz[r1+2][c1+2] == '0'){
                                    return true;
                                }
                            }
                        }
                    }
                    
                    if(c1 > 1){
                        if(r2 == r1 + 2 && c2 == c1 - 2){
                            if(matriz[r1+1][c1-1] == '2' || matriz[r1+1][c1-1] == '4'){
                                if(matriz[r1+2][c1-2] == '0'){
                                    return true;
                                }
                            }
                        }
                    }
                }

                if (r2 < r1){ // Brancas só podem mover para cima
                    if (matriz[r2][c2] == '0') { // A casa de destino deve estar vazia
                        if(r2 == r1 - 1 && (c2 == c1 - 1 || c2 == c1 + 1)) { 
                            if(!obrigadoComer){
                                return true;
                            } 
                        } else if (r2 == r1 - 2 && (c2 == c1 - 2 || c2 == c1 + 2)) { 
                            int tempR = r1 - 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') { 
                                return true;
                            } 
                        } else {
                            return false; 
                        }
                    }
                } 
                break;
            case '2': 
                if(comeuPretas && r1 > 1){
                    if(c1 < 4){
                        if(r2 == r1 - 2 && (c2 == c1 + 2 || c2 == c1 - 2)){
                            if(matriz[r1-1][c1+1] == '1' || matriz[r1-1][c1+1] == '3'){
                                if(matriz[r1-2][c1+2] == '0'){
                                    return true;
                                }
                            }
                        }
                    }
                    
                    if(c1 > 1){
                        if(r2 == r1 - 2 && (c2 == c1 + 2 || c2 == c1 - 2)){
                            if(matriz[r1-1][c1-1] == '1' || matriz[r1-1][c1-1] == '3'){
                                if(matriz[r1-2][c1-2] == '0'){
                                    return true;
                                }
                            }
                        }
                    }
                }

                if (r2 > r1){ 
                    if (matriz[r2][c2] == '0') { 
                        if(r2 == r1 + 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                            if(!obrigadoComer){
                                return true;
                            }
                        } else if (r2 == r1 + 2 && (c2 == c1 - 2 || c2 == c1 + 2)) {
                            int tempR = r1 + 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') { 
                                return true;
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

    public boolean validaMovimentoDamaSemAlterarTabuleiro(int r1, int c1, int r2, int c2, char[][] matriz){
        if (!dentroLimites(r1, c1) || !dentroLimites(r2, c2)) {
            return false;
        }

        if (matriz[r2][c2] != '0') {
            return false;
        }

        if (c1 == c2 || r1 == r2){ // previne movimento horizontal ou vertical
            return false;
        }

        int contPeca = 0;
        int tempR = r1;
        int tempC = c1;

        switch (matriz[r1][c1]){
            case '3':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        if(!obrigadoComer){
                            return true;
                        }
                    }
                    if(r2 > r1){
                        while(r2 > tempR){
                            tempR++;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4'){
                                contPeca++;
                            } else if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') {
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
                            if(matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4'){
                                contPeca++;
                            } else if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        return true;
                    } else if (contPeca == 0){
                        if(!obrigadoComer){
                            return true;
                        }
                    }
                }
            break;
            case '4':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        if(!obrigadoComer){
                            return true;
                        }
                    }
                    if(r2 > r1){
                        while(r2 > tempR){
                            tempR++;
                            if (c2 > c1){
                                tempC++;
                            } else{
                                tempC--;
                            }
                            if(matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3'){
                                contPeca++;
                            } else if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') {
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
                            if(matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3'){
                                contPeca++;
                            } else if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        return true;
                    } else if (contPeca == 0){
                        if(!obrigadoComer){
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

    public boolean podeComer(int r, int c, boolean vez, char[][] matriz){
        if(matriz[r][c] == 'X' || matriz[r][c] == '0'){
            return false;
        }

        if((matriz[r][c] == '1' || matriz[r][c] == '3') && vez == false){
            return false;
        }

        if((matriz[r][c] == '2' || matriz[r][c] == '4') && vez == true){
            return false;
        }

        int casaOcupada;

        System.out.println("r: " + r + " c: " + c);
        System.out.println(matriz[r][c]);
        switch (matriz[r][c]) {
            case '1':
                System.out.println("comeu: "+ comeuBrancas);
                if(comeuBrancas && r < 4){
                    if(c < 4){
                        if(matriz[r+1][c+1] == '2' || matriz[r+1][c+1] == '4'){
                            if(matriz[r+2][c+2] == '0'){
                                return true;
                            }
                        }
                    }
                    
                    if(c > 1){
                        if(matriz[r+1][c-1] == '2' || matriz[r+1][c-1] == '4'){
                            if(matriz[r+2][c-2] == '0'){
                                return true;
                            }
                        }
                    }
                }

                if(r < 2){
                    return false;
                }
                
                if(c < 4){
                    if(matriz[r-1][c+1] == '2' || matriz[r-1][c+1] == '4'){
                        if(matriz[r-2][c+2] == '0'){
                            return true;
                        }
                    }
                }
                
                if(c > 1){
                    if(matriz[r-1][c-1] == '2' || matriz[r-1][c-1] == '4'){
                        if(matriz[r-2][c-2] == '0'){
                            return true;
                        }
                    }
                }
                
                break;
            case '2':
                if(comeuPretas && r > 1){
                    if(c < 4){
                        if(matriz[r-1][c+1] == '1' || matriz[r-1][c+1] == '3'){
                            if(matriz[r-2][c+2] == '0'){
                                return true;
                            }
                        }
                    }
                    
                    if(c > 1){
                        if(matriz[r-1][c-1] == '1' || matriz[r-1][c-1] == '3'){
                            if(matriz[r-2][c-2] == '0'){
                                return true;
                            }
                        }
                    }
                }

                if(r > 3){
                    return false;
                }

                if(c < 4){
                    if(matriz[r+1][c+1] == '1' || matriz[r+1][c+1] == '3'){
                        if(matriz[r+2][c+2] == '0'){
                            return true;
                        }
                    }
                }

                if(c > 1){
                    if(matriz[r+1][c-1] == '1' || matriz[r+1][c-1] == '3'){
                        if(matriz[r+2][c-2] == '0'){
                            return true;
                        }
                    }
                }
                break;
            case '3':
                casaOcupada = 0;
                for(int i = 1; r + i < 6 && c + i < 6; i++){
                    if(matriz[r+i][c+i] == '2' || matriz[r+i][c+i] == '4'){
                        casaOcupada++;
                    } else if(matriz[r+i][c+i] == '1' || matriz[r+i][c+i] == '3'){
                        break;
                    }

                    if(matriz[r+i][c+i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }
                    
                casaOcupada = 0;
                for(int i = 1; r + i < 6 && c - i >= 0; i++){
                    if(matriz[r+i][c-i] == '2' || matriz[r+i][c-i] == '4'){
                        casaOcupada++;
                    } else if(matriz[r+i][c-i] == '1' || matriz[r+i][c-i] == '3'){
                        break;
                    }

                    if(matriz[r+i][c-i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }
                
                
                casaOcupada = 0;
                for(int i = 1; r - i >= 0 && c + i < 6; i++){
                    if(matriz[r-i][c+i] == '2' || matriz[r-i][c+i] == '4'){
                        casaOcupada++;
                    } else if(matriz[r-i][c+i] == '1' || matriz[r-i][c+i] == '3'){
                        break;
                    }

                    if(matriz[r-i][c+i] == '0' && casaOcupada == 1){
                        return true;
                    }
                    
                }
                    
                casaOcupada = 0;
                for(int i = 1; r - i >= 0 && c - i >= 0; i++){
                    if(matriz[r-i][c-i] == '2' || matriz[r-i][c-i] == '4'){
                        casaOcupada++;
                    } else if(matriz[r-i][c-i] == '1' || matriz[r-i][c-i] == '3'){
                        break;
                    }

                    if(matriz[r-i][c-i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }

                break;
            case '4':
                casaOcupada = 0;
                for(int i = 1; r + i < 6 && c + i < 6; i++){
                    if(matriz[r+i][c+i] == '1' || matriz[r+i][c+i] == '3'){
                        casaOcupada++;
                    } else if(matriz[r+i][c+i] == '2' || matriz[r+i][c+i] == '4'){
                        break;
                    }

                    if(matriz[r+i][c+i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }

                    
                casaOcupada = 0;
                for(int i = 1; r + i < 6 && c - i >= 0; i++){
                    if(matriz[r+i][c-i] == '1' || matriz[r+i][c-i] == '3'){
                        casaOcupada++;
                    } else if(matriz[r+i][c-i] == '2' || matriz[r+i][c-i] == '4'){
                        break;
                    }

                    if(matriz[r+i][c-i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }
                
                casaOcupada = 0;
                for(int i = 1; r - i >= 0 && c + i < 6; i++){
                    if(matriz[r-i][c+i] == '1' || matriz[r-i][c+i] == '3'){
                        casaOcupada++;
                    } else if(matriz[r-i][c+i] == '2' || matriz[r-i][c+i] == '4'){
                        break;
                    }

                    if(matriz[r-i][c+i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }    

                casaOcupada = 0;
                for(int i = 1; r - i >= 0 && c - i >= 0; i++){
                    if(matriz[r-i][c-i] == '1' || matriz[r-i][c-i] == '3'){
                        casaOcupada++;
                    } else if(matriz[r-i][c-i] == '2' || matriz[r-i][c-i] == '4'){
                        break;
                    }

                    if(matriz[r-i][c-i] == '0' && casaOcupada == 1){
                        return true;
                    }
                }
                break;
            default:
                break;
        }

        return false;              
    }

    public boolean verificarAlgumaPecaPodeComer(boolean vez, char[][] matriz){
        ArrayList<Peca> pecas = verificarOndeTemPeca(this.matriz);
        for(Peca peca: pecas){
            if(vez == true){
                if(peca.getTipo() == '1' || peca.getTipo() == '3'){
                    if(podeComer(peca.getLinha(), peca.getColuna(), vez, matriz)){
                        return true;
                    }
                }
            } else if (vez == false){
                if(peca.getTipo() == '2' || peca.getTipo() == '4'){
                    if(podeComer(peca.getLinha(), peca.getColuna(), vez, matriz)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    public boolean verificarCasaOrigemVálida(int r, int c, boolean vez, char[][] matriz){
        if(this.matriz[r][c] == 'X' || this.matriz[r][c] == '0'){
            return false;
        }
        
        if(obrigadoComer){
            return podeComer(r, c, vez, matriz);
        } else {
            return true;
        }
    }

    private boolean dentroLimites(int r, int c) {
        return r >= 0 && r < TAMANHO && c >= 0 && c < TAMANHO;
    }

    public boolean verificarCasaDestinoVálidaComum(int r1, int c1, int r2, int c2, boolean vez, char[][] matriz){
        if (!dentroLimites(r1, c1) || !dentroLimites(r2, c2)) {
            return false;
        }

        if (matriz[r2][c2] != '0') {
            return false;
        }


        switch (matriz[r1][c1]) {
            case '1': // brancas
                if(comeuBrancas && r1 < 4){
                    if(c1 < 4){
                        if(r2 == r1 + 2 && c2 == c1 + 2){
                            if(matriz[r1+1][c1+1] == '2' || matriz[r1+1][c1+1] == '4'){
                                if(matriz[r1+2][c1+2] == '0'){
                                    matriz[r1][c1] = '0';
                                    matriz[r1+1][c1+1] = '0';
                                    matriz[r2][c2] = '1';
                                    this.comeuBrancas = true;
                                    this.obrigadoComer = podeComer(r2, c2, vez, matriz);
                                    return true;
                                }
                            }
                        }
                    }
                    
                    if(c1 > 1){
                        if(r2 == r1 + 2 && c2 == c1 - 2){
                            if(matriz[r1+1][c1-1] == '2' || matriz[r1+1][c1-1] == '4'){
                                if(matriz[r1+2][c1-2] == '0'){
                                    matriz[r1][c1] = '0';
                                    matriz[r1+1][c1-1] = '0';
                                    matriz[r2][c2] = '1';
                                    this.comeuBrancas = true;
                                    this.obrigadoComer = podeComer(r2, c2, vez, matriz);
                                    return true;
                                }
                            }
                        }
                    }
                }

                if (r2 < r1){ // Brancas só podem mover para cima
                    if (matriz[r2][c2] == '0') { // A casa de destino deve estar vazia
                        if(r2 == r1 - 1 && (c2 == c1 - 1 || c2 == c1 + 1)) { 
                            if(obrigadoComer){
                                return false;
                            } 
                            matriz[r2][c2] = matriz[r1][c1];
                            if (matriz[r2][c2] == '1' && r2 == 0) {
                                matriz[r2][c2] = '3';
                            }
                            this.comeuBrancas = false;
                            matriz[r1][c1] = '0';
                            return true;
                        } else if (r2 == r1 - 2 && (c2 == c1 - 2 || c2 == c1 + 2)) { // Verifica se há uma peça adversária para capturar
                            int tempR = r1 - 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') { // Verifica se a peça adversária está na casa intermediária
                                matriz[r2][c2] = matriz[r1][c1];
                                if (matriz[r2][c2] == '1' && r2 == 0) {
                                    matriz[r2][c2] = '3';
                                }
                                matriz[r1][c1] = '0';
                                matriz[tempR][tempC] = '0';
                                pecasPretas--;
                                this.comeuBrancas = true;
                                this.obrigadoComer = podeComer(r2, c2, vez, matriz);
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
                if(comeuPretas && r1 > 1){
                    if(c1 < 4){
                        if(r2 == r1 - 2 && (c2 == c1 + 2 || c2 == c1 - 2)){
                            if(matriz[r1-1][c1+1] == '1' || matriz[r1-1][c1+1] == '3'){
                                if(matriz[r1-2][c1+2] == '0'){
                                    matriz[r1][c1] = '0';
                                    matriz[r1-1][c1+1] = '0';
                                    matriz[r2][c2] = '2';
                                    this.comeuPretas = true;
                                    this.obrigadoComer = podeComer(r2, c2, vez, matriz);
                                    return true;
                                }
                            }
                        }
                    }
                    
                    if(c1 > 1){
                        if(r2 == r1 - 2 && (c2 == c1 + 2 || c2 == c1 - 2)){
                            if(matriz[r1-1][c1-1] == '1' || matriz[r1-1][c1-1] == '3'){
                                if(matriz[r1-2][c1-2] == '0'){
                                    matriz[r1][c1] = '0';
                                    matriz[r1-1][c1-1] = '0';
                                    matriz[r2][c2] = '2';
                                    this.comeuPretas = true;
                                    this.obrigadoComer = podeComer(r2, c2, vez, matriz);
                                    return true;
                                }
                            }
                        }
                    }
                }

                if (r2 > r1){ 
                    if (matriz[r2][c2] == '0') { 
                        if(r2 == r1 + 1 && (c2 == c1 - 1 || c2 == c1 + 1)) {  
                            if(obrigadoComer){
                                return false;
                            }
                            matriz[r2][c2] = matriz[r1][c1];
                            if (matriz[r2][c2] == '2' && r2 == 5) {
                                matriz[r2][c2] = '4';
                            }
                            matriz[r1][c1] = '0';
                            this.comeuPretas = false;
                            return true;
                        } else if (r2 == r1 + 2 && (c2 == c1 - 2 || c2 == c1 + 2)) {
                            int tempR = r1 + 1;
                            int tempC = (c2 == c1 - 2) ? c1 - 1 : c1 + 1;
                            if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') { 
                                matriz[r2][c2] = matriz[r1][c1];
                                if (matriz[r2][c2] == '2' && r2 == 5) {
                                    matriz[r2][c2] = '4';
                                }
                                matriz[r1][c1] = '0';
                                matriz[tempR][tempC] = '0';
                                pecasBrancas--;
                                this.comeuPretas = true;
                                this.obrigadoComer = podeComer(r2, c2, vez, matriz);
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

    public boolean verificarCasaDestinoVálidaDama(int r1, int c1, int r2, int c2, boolean vez, char[][] matriz){
        if (!dentroLimites(r1, c1) || !dentroLimites(r2, c2)) {
            return false;
        }

        if (matriz[r2][c2] != '0') {
            return false;
        }

        if (c1 == c2 || r1 == r2){ // previne movimento horizontal ou vertical
            return false;
        }

        int contPeca = 0;
        int linhaParada = -1;
        int colunaParada = -1;
        int proxLinha = r2;
        int proxColuna = c2;
        int tempR = r1;
        int tempC = c1;

        switch (matriz[r1][c1]){
            case '3':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        if(obrigadoComer){
                            return false;
                        }
                        matriz[r1][c1] = '0';
                        matriz[r2][c2] = '3';
                        this.comeuBrancas = false;
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
                            if(matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR + 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') {
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
                            if(matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR - 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        matriz[r1][c1] = '0';
                        matriz[proxLinha][proxColuna] = '3';
                        matriz[linhaParada][colunaParada] = '0';
                        this.comeuBrancas = true;
                        this.obrigadoComer = podeComer(proxLinha, proxColuna, vez, matriz);
                        pecasPretas--;

                        return true;
                    } else if (contPeca == 0){
                        if(obrigadoComer){
                            return false;
                        }
                        matriz[r1][c1] = '0';
                        matriz[r2][c2] = '3';
                        this.comeuBrancas = false;
                        return true;
                    }
                }
            break;
            case '4':
                if((Math.abs(r2 - r1) == Math.abs(c2 - c1)) && matriz[r2][c2] == '0'){ // é diagonal 
                    if (Math.abs(r2 - r1) == 1){
                        if(obrigadoComer){
                            return false;
                        }
                        matriz[r1][c1] = '0';
                        matriz[r2][c2] = '4';
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
                            if(matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR + 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') {
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
                            if(matriz[tempR][tempC] == '1' || matriz[tempR][tempC] == '3'){
                                contPeca++;
                                linhaParada = tempR;
                                colunaParada = tempC;
                                proxLinha = tempR - 1;
                                if (c2 > c1){
                                    proxColuna = tempC + 1;
                                } else{
                                    proxColuna = tempC - 1;
                                }
                            } else if (matriz[tempR][tempC] == '2' || matriz[tempR][tempC] == '4') {
                                return false;
                            }
                        }
                    }

                    if(contPeca == 1){
                        matriz[r1][c1] = '0';
                        matriz[proxLinha][proxColuna] = '4';
                        matriz[linhaParada][colunaParada] = '0';
                        this.comeuPretas = true;
                        this.obrigadoComer = podeComer(proxLinha, proxColuna, vez, matriz);
                        pecasBrancas--;

                        return true;
                    } else if (contPeca == 0){
                        if(obrigadoComer){
                            return false;
                        }
                        matriz[r1][c1] = '0';
                        matriz[r2][c2] = '4';
                        this.comeuPretas = false;
                        return true;
                    }
                }
            break;
            default:
            break;
        }
        return false;
    }

    public boolean fazerMovimento(int r1, int c1, int r2, int c2, boolean vez, char[][] matriz) {
        
        obrigadoComer = verificarAlgumaPecaPodeComer(vez, matriz);
       
        if (!verificarCasaOrigemVálida(r1, c1, vez, matriz)) {
            return false; // A casa de origem é inválida ou vazia
        }

        if ((c1 == c2) || (r1 == r2)){
            return false; // Impede movimentos verticais ou horizontais
        }

        boolean podeMover; // verifica se é um movimento válido
        if (matriz[r1][c1] == '1' || matriz[r1][c1] == '2'){
            podeMover = verificarCasaDestinoVálidaComum(r1, c1, r2, c2, vez, matriz);
        } else{
            podeMover = verificarCasaDestinoVálidaDama(r1, c1, r2, c2, vez, matriz);
        }
        
        System.out.println(podeMover);

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

    public boolean getObrigadoComer(){
        return this.obrigadoComer;
    }
}
