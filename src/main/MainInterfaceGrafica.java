package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * @author Douglas
 */
public final class MainInterfaceGrafica extends JFrame {

    // define os modos de decisão da ia
    private enum ModoIA {
        // ia fixa de 10 níveis, com reuso da árvore
        PADRAO_10_NOS,
        // ia com profundidade escolhida no controle deslizante
        DIFICULDADE_1_A_9
    }

    // tamanho fixo do tabuleiro (6x6)
    private final int TAMANHO = 6;
    // matriz de botões da interface gráfica
    private final CasaBotao[][] tabuleiroInterface = new CasaBotao[TAMANHO][TAMANHO];
    // profundidade usada no modo de dificuldade
    private int profundidade; // profundidade da árvore (nível de dificuldade)
    // true = jogador controla brancas, false = jogador controla pretas
    private boolean pecasJogador; // true = jogador joga de brancas, false = jogador joga de pretas
    // modo da ia escolhido no início
    private ModoIA modoIA;
    // trava para evitar clique enquanto a ia está pensando
    private boolean processandoIA;
    // random usado para desempates e jogadas aleatórias
    private final Random random = new Random();
    // ponteiro para o nó atual da árvore da ia
    private Node arvoreAtualIA;
    // quantos níveis ainda faltam consumir na árvore atual
    private int profundidadeRestanteArvore;
    
    
    /*
        casa inválida: -1
        vazio: 0
        brancas: 1
        pretas: 2
        damas: 3 (branca) ou 4 (preta)

        -> regras do jogo

            - definir quem utilizará as peças brancas (começa o jogo)
            - obrigatório comer a peça
            - não é permitido comer para trás
            - uma peça pode comer múltiplas peças, em qualquer direção,
            desde que a primeira seja para frente
            - a dama pode andar infinitas casas, respeitando o limite do tabuleiro
            - a dama pode comer para trás
            - a dama pode comer múltiplas peças
            - a última peça a ser comida pela dama indica a posição
            que a dama deverá parar (posição subsequente na direção da comida)
            - na impossibilidade de efetuar jogadas,
            o jogador travado perde o jogo
    */
    private Tabuleiro tabuleiroLogico; 
    // guarda a casa que o jogador selecionou como origem
    private int linhaOrigem = -1, colOrigem = -1;
    // guarda parâmetros atuais para permitir reinício rápido
    private ConfigJogo configAtual;

    // construtor principal da janela
    public MainInterfaceGrafica() {
        
        /*
            tabuleiro do jogo
        */
        tabuleiroLogico = new Tabuleiro();

        ConfigJogo config = mostrarConfigInicial();

        if(config == null){
            dispose();
            return;
        }

        setTitle("DISCIPLINA - IA - MINI JOGO DE DAMA");
        setSize(800, 800);
        setLayout(new GridLayout(TAMANHO, TAMANHO));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        aplicarConfigJogo(config);
        sincronizarInterface(tabuleiroLogico.getMatriz()); 

        setVisible(true);
        // se a ia começar, já dispara o turno automático
        executarTurnoIASeNecessario();
    }

    // cria todos os botões do tabuleiro e liga os cliques
    private void inicializarComponentes() {
        // este for percorre cada linha do tabuleiro visual
        for (int i = 0; i < TAMANHO; i++) {
            // este for percorre cada coluna da linha atual
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiroInterface[i][j] = new CasaBotao();

                // este if alterna as cores para montar o padrão em xadrez
                if ((i + j) % 2 == 0) {
                    tabuleiroInterface[i][j].setBackground(new Color(235, 235, 208)); // bege
                } else {
                    tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));  // verde
                }

                int linha = i;
                int coluna = j;
                tabuleiroInterface[i][j].addActionListener(e -> tratarClique(linha, coluna));
                add(tabuleiroInterface[i][j]);
            }
        }
    }

    // aplica os parâmetros escolhidos e reseta o estado da partida
    private void aplicarConfigJogo(ConfigJogo config) {
        this.configAtual = config;
        this.profundidade = config.profundidade;
        this.pecasJogador = config.pecasJogador;
        this.modoIA = config.modoIA;
        this.processandoIA = false;
        this.arvoreAtualIA = null;
        this.profundidadeRestanteArvore = 0;
        this.tabuleiroLogico.reiniciar();
        this.linhaOrigem = -1;
        this.colOrigem = -1;
        restaurarCoresTabuleiro();
        sincronizarInterface(tabuleiroLogico.getMatriz());
    }

    // restaura as cores originais do tabuleiro (sem destaques)
    private void restaurarCoresTabuleiro() {
        // estes dois for removem destaques e recolocam as cores originais
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 == 0) {
                    tabuleiroInterface[i][j].setBackground(new Color(235, 235, 208));
                } else {
                    tabuleiroInterface[i][j].setBackground(new Color(119, 149, 86));
                }
            }
        }
    }

    // monta a árvore de possibilidades da ia de forma recursiva
    private void montarArvoreIA(Node no, int profundidade, boolean vez, char[][] matriz){
        // este if encerra a expansão quando chega no limite de profundidade
        if(profundidade <= 0){
            return;
        }
        Tabuleiro tabuleiroGerador = new Tabuleiro();
        boolean obrigadoComer = tabuleiroGerador.verificarAlgumaPecaPodeComer(vez, matriz);
        ArrayList<Jogada> jogadasPossiveis = tabuleiroGerador.retornaJogadasPossiveis(matriz, vez, obrigadoComer);
        int proxProfundidade = profundidade - 1;
        // este for cria um filho para cada jogada possível no estado atual
        for (Jogada jogada : jogadasPossiveis) {
            Node novoNo = new Node();
            novoNo.setOrigin(jogada.getOrigem());
            novoNo.setDest(jogada.getDestino());

            int posOrigem[] = Codificadora.decodificar(jogada.getOrigem());
            int posDestino[] = Codificadora.decodificar(jogada.getDestino());

            Tabuleiro simulador = new Tabuleiro();
            char[][] antes = simulador.copiarMatriz(matriz);
            char[][] depois = simulador.fazerMovimento(posOrigem[0], posOrigem[1], posDestino[0], posDestino[1], vez, antes);
            
            novoNo.setMatrix(simulador.copiarMatriz(depois));
            boolean proximaVez = simulador.getTurno();
            novoNo.setTurn(proximaVez);

            no.addChild(novoNo);
            this.montarArvoreIA (novoNo, proxProfundidade, proximaVez, novoNo.getMatrix());                                                        
        }   
    }

    // roda minmax recursivamente a partir do nó informado
    private void minMaxJogoDama(Node node) {

        // este if trata o caso de nó folha, onde a heurística define o valor
        if (node.getChild().isEmpty()) {
            int minMax = verificarGanhadorHeuristica(node); // para cada nó folha roda heurística para verificar ganhador
            node.setMinMax(minMax); 
        } else {
            // este for calcula o minmax de todos os filhos antes de subir para o pai
            for (int i = 0; i < node.getChild().size(); i++) {
                Node child = node.getChild().get(i);
                if (child.getMinMax() == Integer.MIN_VALUE){
                    minMaxJogoDama (child);
                }
            }

            if(this.pecasJogador){
                if (node.isTurn()) {
                    node.setMinMax(minimo(node.getChild()));
                }  else {
                    node.setMinMax(maximo(node.getChild()));
                }
            } else {
                if (!node.isTurn()) {
                    node.setMinMax(minimo(node.getChild()));
                }  else {
                    node.setMinMax(maximo(node.getChild()));
                }
            }
        }

    }

    // heurística do projeto, baseada em contagem ponderada de peças
    private int verificarGanhadorHeuristica(Node no){
        char[][] matriz = no.getMatrix();
        int pecasBrancas = 0, pecasPretas = 0;

        for(int i = 0; i < TAMANHO; i++){
            int inicioDoJ; // variável para percorrer apenas casas ímpares
            if(i % 2 == 0){
                inicioDoJ = 1; // se for par, começa em uma coluna ímpar
            } else{
                inicioDoJ = 0; // se for ímpar, começa em uma coluna par
            }

            for(int j = inicioDoJ; j < TAMANHO; j+=2){
                switch (matriz[i][j]) {
                    case '1':
                        pecasBrancas++;
                        break;
                    case '3':
                        pecasBrancas += 3;
                        break;
                    case '2':
                        pecasPretas++;
                        break;
                    case '4':
                        pecasPretas += 3;
                        break;
                    default:
                        break;
                }
                
            }
        }

        if(this.pecasJogador){
            if(pecasBrancas > pecasPretas){
                return -1;
            } else if(pecasPretas > pecasBrancas){
                return 1;
            } else{
                return 0;
            }
        } else{
            if(pecasPretas > pecasBrancas){
                return -1;
            } else if (pecasBrancas > pecasPretas) {
                return 1;
            } else{
                return 0;
            }
        }
    }

    // retorna o menor valor minmax entre os filhos
    private int minimo(ArrayList<Node> nos){
        int min = Integer.MAX_VALUE;
        for(Node no: nos){
            min = Math.min(min, no.getMinMax());
        }
        return min;
    }

    // retorna o maior valor minmax entre os filhos
    private int maximo(ArrayList<Node> nos){
        int max = Integer.MIN_VALUE;
        for(Node no: nos){
            max = Math.max(max, no.getMinMax());
        }
        return max;
    }

    // compara duas matrizes de tabuleiro posição por posição
    private boolean matrizesIguais(char[][] a, char[][] b) {
        if (a == null || b == null) {
            return false;
        }

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // reconstrói do zero a árvore no modo padrão (10 níveis)
    private void reconstruirArvorePadrao10Nos() {
        char[][] estadoAtual = tabuleiroLogico.copiarMatriz(tabuleiroLogico.getMatriz());
        Node raiz = new Node();
        profundidadeRestanteArvore = 10;
        raiz.setTurn(tabuleiroLogico.getTurno());
        raiz.setMatrix(estadoAtual);
        montarArvoreIA(raiz, 10, raiz.isTurn(), raiz.getMatrix());
        minMaxJogoDama(raiz);
        arvoreAtualIA = raiz;
    }

    // garante que a árvore atual está válida para o estado atual
    private void garantirArvoreAlinhada() {
        if (modoIA != ModoIA.PADRAO_10_NOS) {
            arvoreAtualIA = null;
            profundidadeRestanteArvore = 0;
            return;
        }

        boolean precisaReconstruir = arvoreAtualIA == null
                || profundidadeRestanteArvore <= 0
                || !matrizesIguais(arvoreAtualIA.getMatrix(), tabuleiroLogico.getMatriz())
                || arvoreAtualIA.isTurn() != tabuleiroLogico.getTurno()
                || arvoreAtualIA.getChild().isEmpty();

        if (precisaReconstruir) {
            reconstruirArvorePadrao10Nos();
        }
    }

    // avança o ponteiro da árvore para o estado atual do tabuleiro
    private void avancarArvoreParaEstadoAtual() {
        if (modoIA != ModoIA.PADRAO_10_NOS || arvoreAtualIA == null) {
            return;
        }

        if (matrizesIguais(arvoreAtualIA.getMatrix(), tabuleiroLogico.getMatriz())
                && arvoreAtualIA.isTurn() == tabuleiroLogico.getTurno()) {
            return;
        }

        for (Node filho : arvoreAtualIA.getChild()) {
            if (matrizesIguais(filho.getMatrix(), tabuleiroLogico.getMatriz())
                    && filho.isTurn() == tabuleiroLogico.getTurno()) {
                arvoreAtualIA = filho;
                if (profundidadeRestanteArvore > 0) {
                    profundidadeRestanteArvore--;
                }
                return;
            }
        }

        arvoreAtualIA = null;
        profundidadeRestanteArvore = 0;
    }

    // executa um turno da ia
    private void fazerMovimentoComIA(){
        if (processandoIA) {
            return;
        }

        boolean turnoIA = tabuleiroLogico.getTurno() != pecasJogador;
        if (!turnoIA) {
            return;
        }

        processandoIA = true;

        // se está no meio de captura em sequência, a ia só pode continuar essa sequência
        if (tabuleiroLogico.temSequenciaCapturaAtiva()) {
            boolean obrigadoComer = tabuleiroLogico.verificarAlgumaPecaPodeComer(tabuleiroLogico.getTurno(), tabuleiroLogico.getMatriz());
            ArrayList<Jogada> jogadasValidas = tabuleiroLogico.retornaJogadasPossiveis(
                    tabuleiroLogico.getMatriz(), tabuleiroLogico.getTurno(), obrigadoComer);

            if (jogadasValidas.isEmpty()) {
                processandoIA = false;
                return;
            }

            Jogada jogadaEscolhida = jogadasValidas.get(random.nextInt(jogadasValidas.size()));
            int[] origem = Codificadora.decodificar(jogadaEscolhida.getOrigem());
            int[] destino = Codificadora.decodificar(jogadaEscolhida.getDestino());
            ResultadoMovimento resultado = tabuleiroLogico.fazerMovimentoComResultado(
                    origem[0], origem[1], destino[0], destino[1], tabuleiroLogico.getTurno(), tabuleiroLogico.getMatriz());

                avancarArvoreParaEstadoAtual();

            sincronizarInterface(tabuleiroLogico.getMatriz());
            processandoIA = false;

            if (resultado.getEstadoJogo() != EstadoJogo.EM_ANDAMENTO) {
                mostrarMensagemFim(resultado.getEstadoJogo());
                return;
            }

            executarTurnoIASeNecessario();
            return;
        }

        // escolhe como obter a raiz: reuso da árvore (modo padrão) ou árvore nova (modo dificuldade)
        Node raiz;
        if (modoIA == ModoIA.PADRAO_10_NOS) {
            garantirArvoreAlinhada();
            raiz = arvoreAtualIA;
        } else {
            int profundidadeBusca = profundidade;
            char[][] estadoAtual = tabuleiroLogico.copiarMatriz(tabuleiroLogico.getMatriz());

            raiz = new Node();
            raiz.setTurn(tabuleiroLogico.getTurno());
            raiz.setMatrix(estadoAtual);
            montarArvoreIA(raiz, profundidadeBusca, raiz.isTurn(), raiz.getMatrix());
            minMaxJogoDama(raiz);
        }

        if (raiz == null || raiz.getChild().isEmpty()) {
            processandoIA = false;
            mostrarMensagemFim(tabuleiroLogico.getEstadoJogoAtual());
            return;
        }

        // escolhe o próximo nó/jogada conforme o modo de ia
        Node escolhido;
        if (modoIA == ModoIA.DIFICULDADE_1_A_9) {
            int indice = random.nextInt(raiz.getChild().size());
            escolhido = raiz.getChild().get(indice);
        } else {
            ArrayList<Node> melhores = new ArrayList<>();
            for (Node child : raiz.getChild()) {
                if (child.getMinMax() == raiz.getMinMax()) {
                    melhores.add(child);
                }
            }

            if (melhores.isEmpty()) {
                escolhido = raiz.getChild().get(0);
            } else {
                escolhido = melhores.get(random.nextInt(melhores.size()));
            }
        }

        int[] origem = Codificadora.decodificar(escolhido.getOrigin());
        int[] destino = Codificadora.decodificar(escolhido.getDest());
        ResultadoMovimento resultado = tabuleiroLogico.fazerMovimentoComResultado(
                origem[0], origem[1], destino[0], destino[1], tabuleiroLogico.getTurno(), tabuleiroLogico.getMatriz());

        // no modo padrão, move o ponteiro da árvore junto com o estado do jogo
        if (modoIA == ModoIA.PADRAO_10_NOS) {
            arvoreAtualIA = escolhido;
            avancarArvoreParaEstadoAtual();
        }

        sincronizarInterface(tabuleiroLogico.getMatriz());
        processandoIA = false;

        if (resultado.getEstadoJogo() != EstadoJogo.EM_ANDAMENTO) {
            mostrarMensagemFim(resultado.getEstadoJogo());
            return;
        }

        executarTurnoIASeNecessario();

    }

    // dispara a ia automaticamente quando for a vez dela
    private void executarTurnoIASeNecessario() {
        boolean turnoIA = tabuleiroLogico.getTurno() != pecasJogador;
        if (turnoIA) {
            SwingUtilities.invokeLater(this::fazerMovimentoComIA);
        }
    }

    // mostra mensagem de fim de jogo e abre opção de reinício
    private void mostrarMensagemFim(EstadoJogo estadoJogo) {
        switch (estadoJogo) {
            case EMPATE:
                JOptionPane.showMessageDialog(this, "Fim de jogo: empate");
                break;
            case VITORIA_BRANCAS:
                JOptionPane.showMessageDialog(this, "Fim de jogo: vitória das brancas");
                break;
            case VITORIA_PRETAS:
                JOptionPane.showMessageDialog(this, "Fim de jogo: vitória das pretas");
                break;
            default:
                break;
        }

        perguntarReinicio();
    }

    // pergunta se reinicia com os mesmos parâmetros ou com novos
    private void perguntarReinicio() {
        Object[] opcoes = {"Mesmos parâmetros", "Novos parâmetros", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Deseja reiniciar a partida?",
                "Reiniciar jogo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        if (escolha == 0) {
            aplicarConfigJogo(this.configAtual);
            executarTurnoIASeNecessario();
        } else if (escolha == 1) {
            ConfigJogo novoConfig = mostrarConfigInicial();
            if (novoConfig != null) {
                aplicarConfigJogo(novoConfig);
                executarTurnoIASeNecessario();
            }
        }
    }

    // trata clique do jogador no tabuleiro
    private void tratarClique(int linha, int col) {
        if (processandoIA) {
            return;
        }

        if (tabuleiroLogico.getEstadoJogoAtual() != EstadoJogo.EM_ANDAMENTO) {
            mostrarMensagemFim(tabuleiroLogico.getEstadoJogoAtual());
            return;
        }

        boolean turnoIA = tabuleiroLogico.getTurno() != pecasJogador;
        if (turnoIA) {
            return;
        }
        
        // caso 1: nenhuma peça selecionada ainda
        if (linhaOrigem == -1) {
            
            // este if verifica se a casa clicada contém alguma peça válida
            if (tabuleiroLogico.getMatriz()[linha][col] != '0' && tabuleiroLogico.getMatriz()[linha][col] != 'X') {
                if (tabuleiroLogico.getTurno() && (tabuleiroLogico.getMatriz()[linha][col] == '1' || tabuleiroLogico.getMatriz()[linha][col] == '3') && this.pecasJogador) {
                    linhaOrigem = linha;
                    colOrigem = col;
                    tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // destaque do clique
                } else if (!tabuleiroLogico.getTurno() && (tabuleiroLogico.getMatriz()[linha][col] == '2' || tabuleiroLogico.getMatriz()[linha][col] == '4') && !this.pecasJogador) {
                    linhaOrigem = linha;
                    colOrigem = col;
                    tabuleiroInterface[linha][col].setBackground(Color.YELLOW); // destaque do clique
                }
            }
        } 
        // caso 2: já existe uma peça selecionada, tentando mover
        else {
            
            // este if permite cancelar a seleção ao clicar na mesma peça
            if (linhaOrigem == linha && colOrigem == col) {
                cancelarSelecao();
                return;
            }
            

            ResultadoMovimento resultado = tabuleiroLogico.fazerMovimentoComResultado(
                    linhaOrigem, colOrigem, linha, col, tabuleiroLogico.getTurno(), tabuleiroLogico.getMatriz());

            cancelarSelecao();
            sincronizarInterface(tabuleiroLogico.getMatriz());

            if (!resultado.isValido()) {
                return;
            }

            avancarArvoreParaEstadoAtual();

            if (resultado.getEstadoJogo() != EstadoJogo.EM_ANDAMENTO) {
                mostrarMensagemFim(resultado.getEstadoJogo());
                return;
            }

            executarTurnoIASeNecessario();
        }
    }

    // cancela seleção atual e remove destaque da casa
    private void cancelarSelecao() {
        if (linhaOrigem != -1) {
            // restaura a cor original
            tabuleiroInterface[linhaOrigem][colOrigem].setBackground(new Color(119, 149, 86));
        }
        linhaOrigem = -1;
        colOrigem = -1;
    }

    // ponto de entrada da aplicação
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterfaceGrafica::new);
    }
    
    /*
    * atualiza a interface gráfica com base na matriz lógica do tabuleiro.
    * este método será chamado após cada jogada da ia.
    */
    // atualiza as peças desenhadas em cada casa com base na matriz lógica
    public void sincronizarInterface(char[][] matriz) {
       for (int i = 0; i < TAMANHO; i++) {
           for (int j = 0; j < TAMANHO; j++) {
               char peca = matriz[i][j];
               tabuleiroInterface[i][j].setTipoPeca(peca);
            }
        }
    }

    private class CasaBotao extends JButton {

        // tipo da peça desenhada nessa casa
        private char tipoPeca = '0';

        // atualiza o tipo e redesenha o botão
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
            // desenha peça branca
            if (tipoPeca == '1' || tipoPeca == '3') { 
                g2.setColor(Color.WHITE);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
                g2.setColor(Color.BLACK);
                g2.drawOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            // desenha peça preta
            } else if (tipoPeca == '2' || tipoPeca == '4') { 
                g2.setColor(Color.BLACK);
                g2.fillOval(margem, margem, getWidth() - 2 * margem, getHeight() - 2 * margem);
            }

            // representação de dama (uma borda dourada)
            if (tipoPeca == '3' || tipoPeca == '4') { 
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(margem + 5, margem + 5, getWidth() - 2 * margem - 10, getHeight() - 2 * margem - 10);
            }
        }
    }

    // guarda os parâmetros escolhidos no início/reinício da partida
    private static class ConfigJogo{
        final int profundidade;
        final boolean pecasJogador; // true - jogador começa de brancas, false - jogador começa de pretas
        final ModoIA modoIA;

        // construtor dos parâmetros de partida
        ConfigJogo(int profundidade, boolean pecasJogador, ModoIA modoIA){
            this.profundidade = profundidade;
            this.pecasJogador = pecasJogador;
            this.modoIA = modoIA;
        }
    }

    // mostra o diálogo inicial para escolher parâmetros do jogo
    private ConfigJogo mostrarConfigInicial() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        JSlider dificuldadeSlider = new JSlider(1, 9, 3);
        dificuldadeSlider.setMajorTickSpacing(1);
        dificuldadeSlider.setPaintTicks(true);
        dificuldadeSlider.setPaintLabels(true);
        dificuldadeSlider.setSnapToTicks(true);

        JComboBox<String> pecaInicialBox = new JComboBox<>(new String[] {
        "Brancas", "Pretas"
        });

        JComboBox<String> modoIABOX = new JComboBox<>(new String[] {
        "IA padrão (10 nós)", "IA dificuldade (1-9)"
        });

        panel.add(new JLabel("Dificuldade (1-9):"));
        panel.add(dificuldadeSlider);
        panel.add(new JLabel("Peça do jogador:"));
        panel.add(pecaInicialBox);
        panel.add(new JLabel("Modo da IA:"));
        panel.add(modoIABOX);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Configuração Inicial", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        int profundidadeEscolhida = dificuldadeSlider.getValue();
        boolean jogadorBrancas = "Brancas".equals(pecaInicialBox.getSelectedItem());
        ModoIA modoIAEscolhido = "IA padrão (10 nós)".equals(modoIABOX.getSelectedItem())
            ? ModoIA.PADRAO_10_NOS
            : ModoIA.DIFICULDADE_1_A_9;

        return new ConfigJogo(profundidadeEscolhida, jogadorBrancas, modoIAEscolhido);
    }
}
