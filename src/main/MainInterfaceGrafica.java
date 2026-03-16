package main;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Douglas
 */
public final class MainInterfaceGrafica extends JFrame {

    private final int TAMANHO = 6;
    private final CasaBotao[][] tabuleiroInterface = new CasaBotao[TAMANHO][TAMANHO];
    private final int profundidade = 3; // profundidade da árvore (Começa com 10, falta implementar escolha de dificuldade)
    
    /*
        Casa Inválida: -1
        Vazio: 0
        Brancas: 1
        Pretas: 2
        Damas: 3 (branca) ou 4 (preta)
    
        -> REGRAS DO JOGO

            - DEFINIR QUEM UTILIZARÁ AS PEÇAS BRANCAS (COMEÇA O JOGO)
            - OBRIGATÓRIO COMER A PEÇA
            - NÃO É PERMITIDO COMER PRA TRÁS
            - UMA PEÇA PODE COMER MÚLTIPLAS PEÇAS, EM QUALQUER
            DIREÇÃO, DESDE QUE A PRIMEIRA SEJA PARA FRENTE
            - A DAMA PODE ANDAR INFINITAS CASAS, RESPEITANDO O LIMITE DO TABULEIRO
            - A DAMA PODE COMER PRA TRÁS
            - A DAMA PODE COMER MÚLTIPLAS PEÇAS
            - A ÚLTIMA PEÇA A SER COMIDA PELA DAMA 
            INDICA A POSIÇÃO QUE A DAMA DEVERÁ PARAR 
            (POSIÇÃO SUBSEQUENTE NA DIREÇÃO DA COMIDA)
            - NA IMPOSSIBILIDADE DE EFETUAR JOGADAS, 
            O JOGADOR TRAVADO PERDE O JOGO
    */
    private final Tabuleiro tabuleiroLogico; 
    private final Tabuleiro tabuleiroIA = new Tabuleiro();
    private int linhaOrigem = -1, colOrigem = -1;

    public MainInterfaceGrafica() {
        
        /*
            TABULEIRO DO JOGO
        */
        tabuleiroLogico = new Tabuleiro();

        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(TAMANHO, TAMANHO));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        sincronizarInterface(tabuleiroLogico.getMatriz()); 

        setVisible(true);
    }

    private void inicializarComponentes() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiroInterface[i][j] = new CasaBotao();

                // Cores do tabuleiro
                if ((i + j) % 2 == 0) {
                    tabuleiroInterface[i][j].setBackground(new Color(235, 235, 208)); // Bege
                } else {
                    tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // Verde
                }

                int linha = i;
                int coluna = j;
                tabuleiroInterface[i][j].addActionListener(e -> tratarClique(linha, coluna));
                add(tabuleiroInterface[i][j]);
            }
        }

        /*
            CRIAÇÃO DA ÁRVORE
        
            - PARA O ESTADO DO TABULEIRO, VERIFICAR JOGADAS POSSÍVEIS;
            - PARA CADA JOGADA POSSÍVEL, CRIA UM NOVO NÓ;
            - ADICIONAMOS OS NÓS NA ÁRVORE;
            - ENTRAMOS RECURSIVAMENTE NOS NÓS FILHOS;
        */
        Node arvore = new Node();
        
        this.montarArvoreIA (arvore, profundidade, true, tabuleiroIA.getMatriz());

        mostrarArvore(arvore);
    }

    private void montarArvoreIA(Node no, int profundidade, boolean vez, char[][] matriz){
        if(profundidade <= 0){
            return;
        }
        Tabuleiro tabuleiroCalculo = new Tabuleiro();
        ArrayList<Jogada> jogadasPossiveis = tabuleiroCalculo.retornaJogadasPossiveis(matriz, vez, false);
        profundidade--;
        for (Jogada jogada : jogadasPossiveis) {
            Node novoNo = new Node();
            novoNo.setOrigin(jogada.getOrigem());
            novoNo.setDest(jogada.getDestino());
            int posOrigem[] = Codificadora.decodificar(jogada.getOrigem());
            int posDestino[] = Codificadora.decodificar(jogada.getDestino());
            char[][]matrix = tabuleiroCalculo.fazerMovimento(posOrigem[0], posOrigem[1], posDestino[0], posDestino[1], vez, matriz);
            novoNo.setMatrix(matrix);
            boolean proximaVez = tabuleiroCalculo.calcularVez(matriz, matrix, jogada.getOrigem(), jogada.getDestino());
            novoNo.setTurn(proximaVez);
            no.addChild(novoNo);
            this.montarArvoreIA (novoNo, profundidade, proximaVez, matrix);                                                        
        }   
    }

    private void mostrarArvore(Node arvore){
        System.out.println("{ Origem :"+ arvore.getOrigin() + ", Destino:" + arvore.getDest() + " }");
        ArrayList<Node> filhos = arvore.getChild();

        if(filhos != null){
            for(Node no:filhos){
                mostrarArvore(no);
            }
        }
    }

    private void minMaxJogoDama(Node node) {

        if (node.getChild().isEmpty()) {
            int minMax = verificarGanhadorHeuristica(node);
            node.setMinMax(minMax);
        } else {
            for (int i = 0; i < node.getChild().size(); i++) {
                Node child = node.getChild().get(i);
                if (child.getMinMax() == Integer.MIN_VALUE){
                    minMaxJogoDama (child);
                }
            }
            /*
                jogada das brancas - branca é o usuário
             */
            if (node.isTurn()) {
                int min = minimo(node.getChild());
                node.setMinMax(min);
            } /*
            jogada das pretas - preta é a IA
             */ else {
                int max = maximo(node.getChild());
                node.setMinMax(max);
            }
        }

    }

    private int verificarGanhadorHeuristica(Node no){
        char[][] matriz = no.getMatrix();
        int pecasBrancas = 0, pecasPretas = 0;

        for(int i = 0; i < TAMANHO; i++){
            int inicioDoJ;
            if(i % 2 == 0){
                inicioDoJ = 1;
            } else{
                inicioDoJ = 0;
            }

            for(int j = inicioDoJ; j < TAMANHO; j+=2){
                if(matriz[i][j] == '1' || matriz[i][j] == '3'){
                    pecasBrancas++;
                } else if(matriz[i][j] == '2' || matriz[i][j] == '4'){
                    pecasPretas++;
                }
                
            }
        }

        if(pecasBrancas > pecasPretas && no.isTurn()){
            return 1;
        } else if(pecasPretas > pecasBrancas && no.isTurn()){
            return -1;
        } else if(pecasPretas == pecasBrancas && no.isTurn()){
            return 0;
        } 

        if(pecasBrancas > pecasPretas && !no.isTurn()){
            return -1;
        } else if(pecasPretas > pecasBrancas && !no.isTurn()){
            return 1;
        } else {
            return 0;
        } 
    }

    private int minimo(ArrayList<Node> nos){
        int min = 0;
        for(Node no: nos){
            min += no.getMinMax();
            minimo(no.getChild());
        }
        return min;
    }

    private int maximo(ArrayList<Node> nos){
        int max = 0;
        for(Node no: nos){
            max += no.getMinMax();
            maximo(no.getChild());
        }
        return max;
    }

    private void tratarClique(int linha, int col) {
        
        // Caso 1: Nenhuma peça selecionada ainda
        if (linhaOrigem == -1) {
            
            // Verifica se a casa clicada contém QUALQUER peça (1, 2, 3 ou 4)
            if (tabuleiroLogico.getMatriz()[linha][col] != '0' && tabuleiroLogico.getMatriz()[linha][col] != 'X') {
                if (tabuleiroLogico.getTurno() && (tabuleiroLogico.getMatriz()[linha][col] == '1' || tabuleiroLogico.getMatriz()[linha][col] == '3')) {
                    linhaOrigem = linha;
                    colOrigem = col;
                    tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // Destaque do clique
                } else if (!tabuleiroLogico.getTurno() && (tabuleiroLogico.getMatriz()[linha][col] == '2' || tabuleiroLogico.getMatriz()[linha][col] == '4')) {
                    linhaOrigem = linha;
                    colOrigem = col;
                    tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // Destaque do clique
                }
            }
        } 
        // Caso 2: Já existe uma peça selecionada, tentando mover
        else {
            
            // Se clicar na mesma peça, cancela a seleção
            if (linhaOrigem == linha && colOrigem == col) {
                cancelarSelecao();
                return;
            }

            char[][] tabuleiroAtualizado = tabuleiroLogico.fazerMovimento(linhaOrigem, colOrigem, linha, col, tabuleiroLogico.getTurno(), tabuleiroLogico.getMatriz());

            cancelarSelecao();
            sincronizarInterface(tabuleiroAtualizado);
        }
    }

    private void cancelarSelecao() {
        if (linhaOrigem != -1) {
            // Restaura a cor original
            tabuleiroInterface[linhaOrigem][colOrigem].setBackground(new Color(119, 149, 86));
        }
        linhaOrigem = -1;
        colOrigem = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterfaceGrafica::new);
    }
    
    /*
    * Atualiza a interface gráfica com base na matriz lógica do Tabuleiro. Este
    * método será chamado após cada jogada da IA.
    */
   public void sincronizarInterface(char[][] matriz) {
       for (int i = 0; i < TAMANHO; i++) {
           for (int j = 0; j < TAMANHO; j++) {
               char peca = matriz[i][j];
               tabuleiroInterface[i][j].setTipoPeca(peca);
            }
        }
    }

    private class CasaBotao extends JButton {

        private char tipoPeca = '0';

        public void setTipoPeca(char tipo) {
            this.tipoPeca = tipo;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int margem = 10;
            // Brancas
            if (tipoPeca == '1' || tipoPeca == '3') { 
                g2.setColor(Color.WHITE);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
                g2.setColor(Color.BLACK);
                g2.drawOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            // Pretas
            } else if (tipoPeca == '2' || tipoPeca == '4') { 
                g2.setColor(Color.BLACK);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            }

            // Representação de Dama (uma borda dourada)
            if (tipoPeca == '3' || tipoPeca == '4') { 
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(margem + 5, margem + 5, getWidth() - 2 * margem - 10, getHeight() - 2 * margem - 10);
            }
        }
    }
}
