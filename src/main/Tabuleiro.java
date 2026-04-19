package main;

import java.util.ArrayList;

/**
 * @author Douglas
 */
public class Tabuleiro {

    // matriz principal do jogo
    private char[][] matriz; // matriz tipo char para economizar bytes nos nós
    // tamanho fixo do tabuleiro
    private final int TAMANHO = 6;
    // turno atual (true=brancas, false=pretas)
    private boolean turno = true; // true para brancas, false para pretas
    // flags usadas para controlar sequência de captura
    private boolean comeuBrancas = false;
    private boolean comeuPretas = false;
    // contadores de peças restantes de cada lado
    private int pecasPretas = 6;
    private int pecasBrancas = 6;
    // guarda origem fixa durante captura em sequência
    private int linhaSequenciaCaptura = -1;
    private int colunaSequenciaCaptura = -1;
    // liga/desliga o filtro de captura máxima (útil em simulações)
    private boolean aplicarFiltroCapturaMaxima = true;

    // construtor padrão
    public Tabuleiro() { // construtor
        this.matriz = new char[TAMANHO][TAMANHO];
        inicializar();
    }

    // reinicia o tabuleiro para estado inicial
    public void reiniciar() {
        inicializar();
    }

    // preenche o tabuleiro com peças iniciais e limpa estado auxiliar
    private void inicializar() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 2) {
                        matriz[i][j] = '2'; // pretas
                    } else if (i > 3) {
                        matriz[i][j] = '1'; // brancas
                    } else {
                        matriz[i][j] = '0'; // vazio
                    }
                } else {
                    matriz[i][j] = 'X'; // casa inválida
                }
            }
        }
        setTurno(true); // começa com as brancas
        this.comeuBrancas = false;
        this.comeuPretas = false;
        this.linhaSequenciaCaptura = -1;
        this.colunaSequenciaCaptura = -1;
        pecasPretas = 6;
        pecasBrancas = 6;
    }

    /*
        Implementação dos métodos - getMovimentosPossiveis(), fazerMovimento(), etc
    */

    // retorna lista de peças encontradas no tabuleiro informado
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

    // gera todas as jogadas válidas para o jogador da vez
    public ArrayList<Jogada> retornaJogadasPossiveis(char[][] matriz, boolean vez, boolean obrigadoComer){
        ArrayList<Jogada> jogadasPossiveis = new ArrayList<>();
        ArrayList<Peca> pecas = verificarOndeTemPeca(matriz);

        // bloco quando existe captura obrigatória
        if(verificarAlgumaPecaPodeComer(vez, matriz)){
            // este for passa por todas as peças para achar capturas válidas
            for(Peca peca:pecas){
                // este if limita as jogadas quando já existe sequência de captura ativa
                if (temSequenciaCapturaAtiva() && (peca.getLinha() != linhaSequenciaCaptura || peca.getColuna() != colunaSequenciaCaptura)) {
                    continue;
                }
                if((peca.getTipo() == '1' || peca.getTipo() == '3') && vez == false){
                    continue;
                }
                if((peca.getTipo() == '2' || peca.getTipo() == '4') && vez == true){
                    continue;
                }
                if(podeComer(peca.getLinha(), peca.getColuna(), matriz)){
                    switch (peca.getTipo()) {
                        case '1', '2':
                            for(int i = 2; i >= -2; i -= 4){
                                for(int j = 2; j >= -2; j -= 4){
                                    if((peca.getLinha() + i) >= 0 && (peca.getLinha() + i) <= 5 && (peca.getColuna() + j) >= 0 && (peca.getColuna() + j) <= 5){
                                        if(validaMovimentoComumSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j, matriz, obrigadoComer)){
                                            Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j);
                                            jogadasPossiveis.add(novaJogada);
                                        }
                                    }
                                }
                            }
                            break;
                        case '3', '4':
                            for(int i = 2; peca.getLinha() + i < 6 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i, matriz, obrigadoComer)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                            }

                            for(int i = 2; peca.getLinha() + i < 6 && peca.getColuna() - i >= 0; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i, matriz, obrigadoComer)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i);
                                    jogadasPossiveis.add(novaJogada);
                                    
                                }
                            }

                            for(int i = 2; peca.getLinha() - i >= 0 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i, matriz, obrigadoComer)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                            }

                            for(int i = 2; peca.getLinha() - i >= 0 && peca.getColuna() - i >= 0; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i, matriz, obrigadoComer)){
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
            // bloco de movimentos normais quando ninguém é obrigado a capturar
            // este for passa por todas as peças para gerar movimentos simples
            for(Peca peca:pecas){
                // este if mantém a mesma peça quando há sequência de captura em andamento
                if (temSequenciaCapturaAtiva() && (peca.getLinha() != linhaSequenciaCaptura || peca.getColuna() != colunaSequenciaCaptura)) {
                    continue;
                }
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
                                        if(validaMovimentoComumSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j, matriz, obrigadoComer)){
                                            Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + j);
                                            jogadasPossiveis.add(novaJogada);
                                        }
                                    }
                                }
                            }
                        break;
                    case '3', '4':
                        for(int i = 1; peca.getLinha() + i < 6 && peca.getColuna() + i < 6; i++){
                                if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i, matriz, obrigadoComer)){
                                    Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() + i);
                                    jogadasPossiveis.add(novaJogada);
                                }
                        }

                        for(int i = 1; peca.getLinha() + i < 6 && peca.getColuna() - i >= 0; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i, matriz, obrigadoComer)){
                                Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() + i, peca.getColuna() - i);
                                jogadasPossiveis.add(novaJogada);
                            }
                        }

                        for(int i = 1; peca.getLinha() - i >= 0 && peca.getColuna() + i < 6; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i, matriz, obrigadoComer)){
                                Jogada novaJogada = new Jogada(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() + i);
                                jogadasPossiveis.add(novaJogada);
                            }
                        }

                        for(int i = 1; peca.getLinha() - i >= 0 && peca.getColuna() - i >= 0; i++){
                            if(validaMovimentoDamaSemAlterarTabuleiro(peca.getLinha(), peca.getColuna(), peca.getLinha() - i, peca.getColuna() - i, matriz, obrigadoComer)){
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

        // se houver captura, mantém só jogadas que levam ao maior número de capturas
        if (aplicarFiltroCapturaMaxima && verificarAlgumaPecaPodeComer(vez, matriz)) {
            return filtrarJogadasDeCapturaMaxima(jogadasPossiveis, matriz, vez);
        }

        return jogadasPossiveis;
    }

    // filtra as jogadas para manter apenas as melhores sequências de captura
    private ArrayList<Jogada> filtrarJogadasDeCapturaMaxima(ArrayList<Jogada> jogadas, char[][] matriz, boolean vez) {
        if (jogadas.isEmpty()) {
            return jogadas;
        }

        int melhor = 0;
        ArrayList<Jogada> melhores = new ArrayList<>();

        for (Jogada jogada : jogadas) {
            int capturas = contarCapturasDaSequencia(jogada, matriz, vez);
            if (capturas > melhor) {
                melhor = capturas;
                melhores.clear();
                melhores.add(jogada);
            } else if (capturas == melhor) {
                melhores.add(jogada);
            }
        }

        return melhores;
    }

    // conta quantas capturas totais uma jogada inicial pode render
    private int contarCapturasDaSequencia(Jogada jogada, char[][] matriz, boolean vez) {
        int[] origem = Codificadora.decodificar(jogada.getOrigem());
        int[] destino = Codificadora.decodificar(jogada.getDestino());

        Tabuleiro simulador = new Tabuleiro();
        simulador.aplicarFiltroCapturaMaxima = false;
        char[][] antes = simulador.copiarMatriz(matriz);
        char[][] depois = simulador.fazerMovimento(origem[0], origem[1], destino[0], destino[1], vez, antes);

        int capturas = 0;
        if (contarPecasJogaveis(antes) > contarPecasJogaveis(depois)) {
            capturas = 1;
            capturas += simulador.contarMaximoCapturasRecursivo(destino[0], destino[1], depois, vez);
        }

        return capturas;
    }

    // Busca recursiva da maior sequência de capturas a partir de uma peça
    private int contarMaximoCapturasRecursivo(int linha, int coluna, char[][] matriz, boolean vez) {
        ArrayList<Jogada> capturas = gerarJogadasCapturaDaPeca(linha, coluna, matriz, true);
        if (capturas.isEmpty()) {
            return 0;
        }

        int melhor = 0;
        for (Jogada jogada : capturas) {
            int[] origem = Codificadora.decodificar(jogada.getOrigem());
            int[] destino = Codificadora.decodificar(jogada.getDestino());

            Tabuleiro simulador = new Tabuleiro();
            simulador.aplicarFiltroCapturaMaxima = false;
            char[][] antes = simulador.copiarMatriz(matriz);
            char[][] depois = simulador.fazerMovimento(origem[0], origem[1], destino[0], destino[1], vez, antes);

            int capturasAgora = 0;
            if (contarPecasJogaveis(antes) > contarPecasJogaveis(depois)) {
                capturasAgora = 1 + simulador.contarMaximoCapturasRecursivo(destino[0], destino[1], depois, vez);
            }

            if (capturasAgora > melhor) {
                melhor = capturasAgora;
            }
        }

        return melhor;
    }

    // Gera somente jogadas de captura para uma peça específica
    private ArrayList<Jogada> gerarJogadasCapturaDaPeca(int linha, int coluna, char[][] matriz, boolean aposPrimeiraCaptura) {
        ArrayList<Jogada> capturas = new ArrayList<>();

        if (!dentroLimites(linha, coluna)) {
            return capturas;
        }

        char tipo = matriz[linha][coluna];
        if (tipo == '0' || tipo == 'X') {
            return capturas;
        }

        boolean comeuBrancasAnterior = this.comeuBrancas;
        boolean comeuPretasAnterior = this.comeuPretas;

        if (aposPrimeiraCaptura && tipo == '1') {
            this.comeuBrancas = true;
        }
        if (aposPrimeiraCaptura && tipo == '2') {
            this.comeuPretas = true;
        }

        if (tipo == '1' || tipo == '2') {
            for (int dr = 2; dr >= -2; dr -= 4) {
                for (int dc = 2; dc >= -2; dc -= 4) {
                    int novaLinha = linha + dr;
                    int novaColuna = coluna + dc;
                    if (dentroLimites(novaLinha, novaColuna)
                            && validaMovimentoComumSemAlterarTabuleiro(linha, coluna, novaLinha, novaColuna, matriz, true)) {
                        capturas.add(new Jogada(linha, coluna, novaLinha, novaColuna));
                    }
                }
            }
        } else if (tipo == '3' || tipo == '4') {
            for (int i = 2; linha + i < 6 && coluna + i < 6; i++) {
                if (validaMovimentoDamaSemAlterarTabuleiro(linha, coluna, linha + i, coluna + i, matriz, true)) {
                    capturas.add(new Jogada(linha, coluna, linha + i, coluna + i));
                }
            }

            for (int i = 2; linha + i < 6 && coluna - i >= 0; i++) {
                if (validaMovimentoDamaSemAlterarTabuleiro(linha, coluna, linha + i, coluna - i, matriz, true)) {
                    capturas.add(new Jogada(linha, coluna, linha + i, coluna - i));
                }
            }

            for (int i = 2; linha - i >= 0 && coluna + i < 6; i++) {
                if (validaMovimentoDamaSemAlterarTabuleiro(linha, coluna, linha - i, coluna + i, matriz, true)) {
                    capturas.add(new Jogada(linha, coluna, linha - i, coluna + i));
                }
            }

            for (int i = 2; linha - i >= 0 && coluna - i >= 0; i++) {
                if (validaMovimentoDamaSemAlterarTabuleiro(linha, coluna, linha - i, coluna - i, matriz, true)) {
                    capturas.add(new Jogada(linha, coluna, linha - i, coluna - i));
                }
            }
        }

        this.comeuBrancas = comeuBrancasAnterior;
        this.comeuPretas = comeuPretasAnterior;
        return capturas;
    }

    // valida movimento de peça comum sem alterar a matriz original
    public boolean validaMovimentoComumSemAlterarTabuleiro(int r1, int c1, int r2, int c2, char[][] matriz, boolean obrigadoComer){
        if (!dentroLimites(r1, c1) || !dentroLimites(r2, c2)) {
            return false;
        }

        if (matriz[r2][c2] != '0') {
            return false;
        }

        switch (matriz[r1][c1]) {
            case '1': // brancas
                if(comeuBrancas && r1 < 4){
                    if (c1 < 4 && r2 == r1 + 2 && c2 == c1 + 2) { 
                        if ((matriz[r1+1][c1+1] == '2' || matriz[r1+1][c1+1] == '4')
                            && matriz[r1+2][c1+2] == '0') {
                            return true;
                        }
                    }

                    if (c1 > 1 && r2 == r1 + 2 && c2 == c1 - 2) { 
                        if ((matriz[r1+1][c1-1] == '2' || matriz[r1+1][c1-1] == '4')
                            && matriz[r1+2][c1-2] == '0') {
                            return true;
                        }
                    }
                }

                if (r2 < r1){ // brancas só podem mover para cima
                    if (matriz[r2][c2] == '0') { // a casa de destino deve estar vazia
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
                if (comeuPretas && r1 > 1) {
                    if (c1 < 4 && r2 == r1 - 2 && c2 == c1 + 2) { 
                        if ((matriz[r1-1][c1+1] == '1' || matriz[r1-1][c1+1] == '3')
                            && matriz[r1-2][c1+2] == '0') {
                            return true;
                        }
                    }

                    if (c1 > 1 && r2 == r1 - 2 && c2 == c1 - 2) { 
                        if ((matriz[r1-1][c1-1] == '1' || matriz[r1-1][c1-1] == '3')
                            && matriz[r1-2][c1-2] == '0') {
                            return true;
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

    // valida movimento de dama sem alterar a matriz original
    public boolean validaMovimentoDamaSemAlterarTabuleiro(int r1, int c1, int r2, int c2, char[][] matriz, boolean obrigadoComer){
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
                        if(r2 > r1 && c2 > c1){
                            if(matriz[r2 - 1][c2 - 1] == '2' || matriz[r2 - 1][c2 - 1] == '4'){
                                return true;
                            }
                        }

                        if(r2 > r1 && c2 < c1){
                            if(matriz[r2 - 1][c2 + 1] == '2' || matriz[r2 - 1][c2 + 1] == '4'){
                                return true;
                            }
                        }

                        if(r2 < r1 && c2 > c1){
                            if(matriz[r2 + 1][c2 - 1] == '2' || matriz[r2 + 1][c2 - 1] == '4'){
                                return true;
                            }
                        }

                        if(r2 < r1 && c2 < c1){
                            if(matriz[r2 + 1][c2 + 1] == '2' || matriz[r2 + 1][c2 + 1] == '4'){
                                return true;
                            }
                        }
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
                        if(r2 > r1 && c2 > c1){
                            if(matriz[r2 - 1][c2 - 1] == '1' || matriz[r2 - 1][c2 - 1] == '3'){
                                return true;
                            }
                        }

                        if(r2 > r1 && c2 < c1){
                            if(matriz[r2 - 1][c2 + 1] == '1' || matriz[r2 - 1][c2 + 1] == '3'){
                                return true;
                            }
                        }

                        if(r2 < r1 && c2 > c1){
                            if(matriz[r2 + 1][c2 - 1] == '1' || matriz[r2 + 1][c2 - 1] == '3'){
                                return true;
                            }
                        }

                        if(r2 < r1 && c2 < c1){
                            if(matriz[r2 + 1][c2 + 1] == '1' || matriz[r2 + 1][c2 + 1] == '3'){
                                return true;
                            }
                        }
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

    // verifica se a peça da posição informada consegue capturar alguém
    public boolean podeComer(int r, int c, char[][] matriz){
        if(matriz[r][c] == 'X' || matriz[r][c] == '0'){
            return false;
        }

        int casaOcupada;
        switch (matriz[r][c]) {
            case '1':
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

    // verifica se existe pelo menos uma peça da vez com captura disponível
    public boolean verificarAlgumaPecaPodeComer(boolean vez, char[][] matriz){
        ArrayList<Peca> pecas = verificarOndeTemPeca(matriz);
        for(Peca peca: pecas){
            if(vez == true){
                if(peca.getTipo() == '1' || peca.getTipo() == '3'){
                    if(podeComer(peca.getLinha(), peca.getColuna(), matriz)){
                        return true;
                    }
                }
            } else if (vez == false){
                if(peca.getTipo() == '2' || peca.getTipo() == '4'){
                    if(podeComer(peca.getLinha(), peca.getColuna(), matriz)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    // valida se a casa escolhida pode ser origem da jogada atual
    public boolean verificarCasaOrigemVálida(int r, int c, boolean vez, char[][] matriz, boolean obrigadoComer){
        if(matriz[r][c] == 'X' || matriz[r][c] == '0'){
            return false;
        }

        if (linhaSequenciaCaptura != -1 && colunaSequenciaCaptura != -1
                && (r != linhaSequenciaCaptura || c != colunaSequenciaCaptura)) {
            return false;
        }
        
        if(obrigadoComer){
            return podeComer(r, c, matriz);
        } else {
            return true;
        }
    }

    // checa se coordenada está dentro do tabuleiro
    private boolean dentroLimites(int r, int c) {
        return r >= 0 && r < TAMANHO && c >= 0 && c < TAMANHO;
    }

    // cria uma cópia profunda da matriz
    public char[][] copiarMatriz(char[][] origem) {
        char[][] copia = new char[TAMANHO][TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            copia[i] = origem[i].clone();
        }
        return copia;
    }

    // conta quantas peças válidas existem no tabuleiro
    private int contarPecasJogaveis(char[][] matriz) {
        int total = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                if (matriz[i][j] == '1' || matriz[i][j] == '2' || matriz[i][j] == '3' || matriz[i][j] == '4') {
                    total++;
                }
            }
        }
        return total;
    }

    // conta separadamente peças comuns e damas de cada lado
    private int[] contarTipos(char[][] matriz) {
        int[] contagem = new int[4];
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = (i % 2 == 0) ? 1 : 0; j < TAMANHO; j += 2) {
                switch (matriz[i][j]) {
                    case '1':
                        contagem[0]++;
                        break;
                    case '2':
                        contagem[1]++;
                        break;
                    case '3':
                        contagem[2]++;
                        break;
                    case '4':
                        contagem[3]++;
                        break;
                    default:
                        break;
                }
            }
        }
        return contagem;
    }

    // compara duas matrizes posição por posição
    private boolean matrizesIguais(char[][] a, char[][] b) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // avalia o estado atual: vitória, empate ou jogo em andamento
    private EstadoJogo avaliarEstadoJogo(char[][] matriz, boolean vezAtual) {
        int[] tipos = contarTipos(matriz);
        int brancasTotal = tipos[0] + tipos[2];
        int pretasTotal = tipos[1] + tipos[3];

        if (brancasTotal == 0) {
            return EstadoJogo.VITORIA_PRETAS;
        }

        if (pretasTotal == 0) {
            return EstadoJogo.VITORIA_BRANCAS;
        }

        int totalPecas = brancasTotal + pretasTotal;
        if (totalPecas == 2 && tipos[2] == 1 && tipos[3] == 1 && tipos[0] == 0 && tipos[1] == 0) {
            boolean capturaBrancas = verificarAlgumaPecaPodeComer(true, matriz);
            boolean capturaPretas = verificarAlgumaPecaPodeComer(false, matriz);
            if (!capturaBrancas && !capturaPretas) {
                return EstadoJogo.EMPATE;
            }
        }

        boolean obrigadoComer = verificarAlgumaPecaPodeComer(vezAtual, matriz);
        ArrayList<Jogada> jogadas = retornaJogadasPossiveis(matriz, vezAtual, obrigadoComer);
        if (jogadas.isEmpty()) {
            return vezAtual ? EstadoJogo.VITORIA_PRETAS : EstadoJogo.VITORIA_BRANCAS;
        }

        return EstadoJogo.EM_ANDAMENTO;
    }

    // retorna o estado atual usando matriz e turno internos
    public EstadoJogo getEstadoJogoAtual() {
        return avaliarEstadoJogo(this.matriz, this.turno);
    }

    // executa movimento e retorna um objeto completo com status da jogada
    public ResultadoMovimento fazerMovimentoComResultado(int r1, int c1, int r2, int c2, boolean vez, char[][] matriz) {
        char[][] antes = copiarMatriz(matriz);
        int totalAntes = contarPecasJogaveis(antes);
        boolean vezOriginal = vez;

        char[][] depois = fazerMovimento(r1, c1, r2, c2, vez, matriz);
        boolean valido = !matrizesIguais(antes, depois);

        if (!valido) {
            return new ResultadoMovimento(false, false, false, this.turno, EstadoJogo.EM_ANDAMENTO, "Jogada inválida");
        }

        int totalDepois = contarPecasJogaveis(depois);
        boolean capturou = totalDepois < totalAntes;
        boolean continuaTurno = this.turno == vezOriginal;
        EstadoJogo estado = getEstadoJogoAtual();

        String mensagem = "Movimento realizado";
        if (estado == EstadoJogo.EMPATE) {
            mensagem = "Empate";
        } else if (estado == EstadoJogo.VITORIA_BRANCAS) {
            mensagem = "Vitória das brancas";
        } else if (estado == EstadoJogo.VITORIA_PRETAS) {
            mensagem = "Vitória das pretas";
        }

        return new ResultadoMovimento(true, capturou, continuaTurno, this.turno, estado, mensagem);
    }


    // executa de fato o movimento na matriz clonada
    public char[][] fazerMovimento(int r1, int c1, int r2, int c2, boolean vez, char[][] matriz) {
        char[][] matrizClone = copiarMatriz(matriz); 

        boolean obrigadoComer = verificarAlgumaPecaPodeComer(vez, matrizClone); // verifica se é obrigado a comer
        boolean comeu = false;
       
        if (!verificarCasaOrigemVálida(r1, c1, vez, matrizClone, obrigadoComer)) {
            return matrizClone; // a casa de origem é inválida ou vazia
        }

        if ((c1 == c2) || (r1 == r2)){
            return matrizClone; // impede movimentos verticais ou horizontais
        }

        boolean podeMover = false; // verifica se é um movimento válido
        
        ArrayList<Jogada> jogadas = retornaJogadasPossiveis(matrizClone, vez, obrigadoComer);
        
        // este for confirma se a jogada escolhida está na lista de jogadas válidas
        for (Jogada jogada : jogadas){
            if(jogada.getOrigem() == Codificadora.codificar(r1, c1) && jogada.getDestino() == Codificadora.codificar(r2, c2)){
                podeMover = true;
            }
        }

        // este if só executa alterações na matriz quando a jogada é válida
        if(podeMover){
            if(Math.abs(r1 - r2) == 1 && Math.abs(c1 - c2) == 1){
                matrizClone[r2][c2] = matrizClone[r1][c1];
                matrizClone[r1][c1] = '0';
            } else{
                if(r2 > r1 && c2 > c1){
                    if(r2 <= 5 && c2 <= 5 && (matrizClone[r2 - 1][c2 - 1] != '0' && matrizClone[r2 - 1][c2 - 1] != 'X')){
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r2-1][c2-1] = '0';
                        matrizClone[r1][c1] = '0';
                        if(matrizClone[r2][c2] == '1' || matrizClone[r2][c2] == '3'){
                            comeuBrancas = true;
                        } else if (matrizClone[r2][c2] == '2' || matrizClone[r2][c2] == '4'){
                            comeuPretas = true;
                        }
                        comeu = true;
                    } else{
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r1][c1] = '0';
                    }
                }
    
                if(r2 > r1 && c2 < c1){
                    if(r2 <= 5 && c2 >= 0 && (matrizClone[r2 - 1][c2 + 1] != '0' && matrizClone[r2 - 1][c2 + 1] != 'X')){
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r2-1][c2+1] = '0';
                        matrizClone[r1][c1] = '0';
                        if(matrizClone[r2][c2] == '1' || matrizClone[r2][c2] == '3'){
                            comeuBrancas = true;
                        } else if (matrizClone[r2][c2] == '2' || matrizClone[r2][c2] == '4'){
                            comeuPretas = true;
                        }
                        comeu = true;
                    } else{
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r1][c1] = '0';
                    }
                }
    
                if(r2 < r1 && c2 > c1){
                    if(r2 >= 0 && c2 <= 5 && (matrizClone[r2 + 1][c2 - 1] != '0' && matrizClone[r2 + 1][c2 - 1] != 'X')){
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r2+1][c2-1] = '0';
                        matrizClone[r1][c1] = '0';
                        if(matrizClone[r2][c2] == '1' || matrizClone[r2][c2] == '3'){
                            comeuBrancas = true;
                        } else if (matrizClone[r2][c2] == '2' || matrizClone[r2][c2] == '4'){
                            comeuPretas = true;
                        }
                        comeu = true;
                    } else{
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r1][c1] = '0';
                    }
                }
    
                if(r2 < r1 && c2 < c1){
                    if(r2 >= 0 && c2 >= 0 && (matrizClone[r2 + 1][c2 + 1] != '0' && matrizClone[r2 + 1][c2 + 1] != 'X')){
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r2+1][c2+1] = '0';
                        matrizClone[r1][c1] = '0';
                        if(matrizClone[r2][c2] == '1' || matrizClone[r2][c2] == '3'){
                            comeuBrancas = true;
                        } else if (matrizClone[r2][c2] == '2' || matrizClone[r2][c2] == '4'){
                            comeuPretas = true;
                        }
                        comeu = true;
                    } else{
                        matrizClone[r2][c2] = matrizClone[r1][c1];
                        matrizClone[r1][c1] = '0';
                    }
                }
            }

            switch(matrizClone[r2][c2]){
                case '1':
                    if(r2 == 0){
                        matrizClone[r2][c2] = '3';
                    }
                    break;
                case '2':
                    if(r2 == 5){
                        matrizClone[r2][c2] = '4';
                    }
                    break;
            }

            if (comeu) {
                if (matrizClone[r2][c2] == '1' || matrizClone[r2][c2] == '3') {
                    this.pecasPretas--;
                    comeuBrancas = true;
                } else if (matrizClone[r2][c2] == '2' || matrizClone[r2][c2] == '4') {
                    this.pecasBrancas--;
                    comeuPretas = true;
                }
            }

            boolean continuaCaptura = comeu && podeComer(r2, c2, matrizClone);
            if (!continuaCaptura) {
                vez = !vez;
                comeuBrancas = false;
                comeuPretas = false;
                linhaSequenciaCaptura = -1;
                colunaSequenciaCaptura = -1;
            } else {
                linhaSequenciaCaptura = r2;
                colunaSequenciaCaptura = c2;
            }

        }
        // System.out.println("tabuleiro pós movimento: ");
        // for(int i = 0; i < TAMANHO; i++) {
        //     for(int j = 0; j < TAMANHO; j++) {
        //         System.out.print(matrizClone[i][j] + " |");
        //     }
        //     System.out.println(); // nova linha após imprimir toda a matrizClone
        // }

        this.matriz = matrizClone;
        this.turno = vez;

        return matrizClone;
    }

    // método legado para calcular turno a partir de antes/depois
    public boolean calcularVez(char[][] matrizAntes, char[][] matrizDepois, char origem, char destino){
        int posOrigem[] = Codificadora.decodificar(origem);
        int posDestino[] = Codificadora.decodificar(destino);

        int linhaOrigem = posOrigem[0];
        int colunaOrigem = posOrigem[1];
        int linhaDestino = posDestino[0];
        int colunaDestino = posDestino[1];

        boolean comeu;

        switch (matrizAntes[linhaOrigem][colunaOrigem]) {
            case '1':
                if(Math.abs((linhaOrigem - linhaDestino)) == 2 && Math.abs((colunaOrigem - colunaDestino)) == 2){
                    comeu = true;
                } else{
                    return false; // vez das pretas (false)
                }

                if(comeu){
                    return podeComer(linhaDestino, colunaDestino, matrizDepois); // vez das brancas ainda (true)
                } else{
                    return false; // vez das pretas (false)
                }
            case '2':
                if(Math.abs((linhaOrigem - linhaDestino)) == 2 && Math.abs((colunaOrigem - colunaDestino)) == 2){
                    comeu = true;
                } else{
                    return true; // vez das brancas (true)
                }

                if(comeu){
                    return !podeComer(linhaDestino, colunaDestino, matrizDepois); // vez das pretas ainda (false)
                } else{
                    return true; // vez das brancas (true)
                }
            case '3':
                int contadorPeca = 0;

                if(linhaOrigem > linhaDestino && colunaOrigem > colunaDestino){
                    int tempR = linhaOrigem - 1;
                    int tempC = colunaOrigem - 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '2' || matrizAntes[tempR][tempC] == '4'){
                            contadorPeca++;
                        }
                        tempR--;
                        tempC--;
                    }while(tempR > linhaDestino);

                } else if(linhaOrigem > linhaDestino && colunaOrigem < colunaDestino){
                    int tempR = linhaOrigem - 1;
                    int tempC = colunaOrigem + 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '2' || matrizAntes[tempR][tempC] == '4'){
                            contadorPeca++;
                        }
                        tempR--;
                        tempC++;
                    }while(tempR > linhaDestino);

                } else if(linhaOrigem < linhaDestino && colunaOrigem > colunaDestino){
                    int tempR = linhaOrigem + 1;
                    int tempC = colunaOrigem - 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '2' || matrizAntes[tempR][tempC] == '4'){
                            contadorPeca++;
                        }
                        tempR++;
                        tempC--;
                    }while(tempR < linhaDestino);

                } else if(linhaOrigem < linhaDestino && colunaOrigem < colunaDestino){
                    int tempR = linhaOrigem + 1;
                    int tempC = colunaOrigem + 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '2' || matrizAntes[tempR][tempC] == '4'){
                            contadorPeca++;
                        }
                        tempR++;
                        tempC++;
                    }while(tempR < linhaDestino);
                }

                if(contadorPeca == 1){
                    comeu = true;
                } else{
                    return false; // vez das pretas (false)
                }

                if(comeu){
                    return podeComer(linhaDestino, colunaDestino, matrizDepois); // vez das brancas ainda (true)
                } else{
                    return false; // vez das pretas (false)
                }
            case '4':
                contadorPeca = 0;

                if(linhaOrigem > linhaDestino && colunaOrigem > colunaDestino){
                    int tempR = linhaOrigem - 1;
                    int tempC = colunaOrigem - 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '1' || matrizAntes[tempR][tempC] == '3'){
                            contadorPeca++;
                        }
                        tempR--;
                        tempC--;
                    }while(tempR > linhaDestino);

                } else if(linhaOrigem > linhaDestino && colunaOrigem < colunaDestino){
                    int tempR = linhaOrigem - 1;
                    int tempC = colunaOrigem + 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '1' || matrizAntes[tempR][tempC] == '3'){
                            contadorPeca++;
                        }
                        tempR--;
                        tempC++;
                    }while(tempR > linhaDestino);

                } else if(linhaOrigem < linhaDestino && colunaOrigem > colunaDestino){
                    int tempR = linhaOrigem + 1;
                    int tempC = colunaOrigem - 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '1' || matrizAntes[tempR][tempC] == '3'){
                            contadorPeca++;
                        }
                        tempR++;
                        tempC--;
                    }while(tempR < linhaDestino);
                } else if(linhaOrigem < linhaDestino && colunaOrigem < colunaDestino){
                    int tempR = linhaOrigem + 1;
                    int tempC = colunaOrigem + 1;

                    do{
                        if(matrizAntes[tempR][tempC] == '1' || matrizAntes[tempR][tempC] == '3'){
                            contadorPeca++;
                        }
                        tempR++;
                        tempC++;
                    }while(tempR < linhaDestino);
                }

                if(contadorPeca == 1){
                    comeu = true;
                } else{
                    return true; // vez das brancas (true)
                }

                if(comeu){
                    return !podeComer(linhaDestino, colunaDestino, matrizDepois); // vez das brancas ainda (true)
                } else{
                    return true; // vez das brancas (true)
                }
        }
        return false;
    }

    // método legado (mantido por compatibilidade)
    public boolean verificaMovimento () {
        return true;
    }
    
    // retorna matriz atual
    public char[][] getMatriz() {
        return this.matriz;
    }

    // substitui matriz atual
    public void setMatriz(char[][] matriz) {
        this.matriz = matriz;
    }

    // retorna turno atual
    public boolean getTurno() {
        return turno;
    }

    // diz se existe sequência de captura ativa
    public boolean temSequenciaCapturaAtiva() {
        return linhaSequenciaCaptura != -1 && colunaSequenciaCaptura != -1;
    }

    // define turno atual
    public void setTurno(boolean turno) {
        this.turno = turno;
    }
}
