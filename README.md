# 🏁 Damas_IA - Jogo de Damas com Inteligência Artificial em Java

![Java](https://img.shields.io/badge/Language-Java-orange.svg)
![AI](https://img.shields.io/badge/Algorithm-Minimax%20%7C%20Heuristics-blueviolet.svg)
![GUI](https://img.shields.io/badge/GUI-Java%20Swing-blue.svg)
![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen.svg)

## 📌 Visão Geral
O **Damas_IA** é uma aplicação completa do clássico jogo de Damas (tabuleiro $6 \times 6$), implementada em **Java** com interface gráfica interativa em **Swing**.

O principal objetivo do projeto é demonstrar a aplicação prática de algoritmos de **Inteligência Artificial Clássica para Jogos de Tabuleiro de Soma-Zero**, utilizando representação em árvore de busca (`Node.java`), funções de avaliação heurística de estado e o algoritmo **Minimax** para calcular e prever os melhores movimentos do adversário controlado pelo computador.

---

## 🚀 Funcionalidades Principais
- 🎮 **Regras Clássicas Completas**:
  - Movimentação diagonal de peças normais e damas promovidas.
  - Capturas simples e sequenciais (saltos múltiplos obrigatórios).
  - Promoção automática a dama ao atingir o lado oposto do tabuleiro.
  - Verificação de vitória por eliminação de peças ou bloqueio total de jogadas válidas.
- 🤖 **Modos de Inteligência Artificial**:
  - **Modo Padrão**: Expansão em profundidade com controle de nós visitados.
  - **Níveis de Dificuldade (1 a 9)**: Ajuste progressivo da profundidade da árvore de busca heurística e do horizonte de previsão de jogadas.
- 🖥️ **Interface Gráfica com Swing**:
  - Renderização visual do tabuleiro com realce de casas válidas e peças selecionadas.
  - Painel de controle para seleção de dificuldade, alternância de turnos e reinício de partida.
- ⚡ **Otimização de Memória nos Nós**:
  - Representação interna da matriz de jogo em array compacto de `char` (`char[][]`), minimizando o consumo de heap durante a expansão combinatória da árvore de estados.

---

## 🛠️ Tecnologias e Ferramentas
- **Linguagem**: Java (JDK 8+)
- **Interface Gráfica**: Java Swing & AWT (`JFrame`, `JPanel`, `Graphics`, listeners de eventos)
- **Algoritmos e Paradigmas**:
  - Teoria dos Jogos / Jogos de Informação Perfeita
  - Algoritmo Minimax com Avaliação Heurística
  - Estrutura de Dados em Árvore de Decisão (`Node.java`)

---

## 🏛️ Lógica Heurística da IA
A função de utilidade calcula o valor de cada estado de jogo com base em múltiplos fatores estratégicos ponderados:
$$\text{Score} = w_1 \cdot (\Delta \text{Peças}) + w_2 \cdot (\Delta \text{Damas}) + w_3 \cdot (\text{Posicionamento Central}) + w_4 \cdot (\text{Peças Ameaçadas})$$
Isso permite que a máquina não apenas busque capturas imediatas, mas também posicione suas peças de forma a proteger a retaguarda e controlar o centro do tabuleiro.

---

## 📂 Estrutura do Repositório
```plaintext
Damas_IA/
├── src/main/
│   ├── MainInterfaceGrafica.java    # Janela principal Swing, loop de jogo e renderização
│   ├── Tabuleiro.java               # Estado do tabuleiro 6x6, regras e validação de jogadas
│   ├── Node.java                    # Nó da árvore de busca Minimax e função heurística
│   ├── Peca.java                    # Entidade que representa peças normais e damas
│   ├── Jogada.java                  # Representação de um movimento (origem -> destino -> capturas)
│   ├── EstadoJogo.java              # Enum para controle de turnos e status de vitória
│   ├── Codificadora.java            # Mapeamento e serialização de casas válidas
│   ├── ResultadoMovimento.java      # Feedback e validação das jogadas executadas
│   └── Readme.pdf                   # Documentação acadêmica complementar do projeto
```

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
- **Java Development Kit (JDK 8 ou superior)** instalado.

### Compilação e Execução
1. Clone o repositório:
   ```bash
   git clone https://github.com/LucaS4nt0s/Damas_IA.git
   cd Damas_IA/src
   ```
2. Compile todos os arquivos-fonte:
   ```bash
   javac main/*.java
   ```
3. Execute a interface gráfica do jogo:
   ```bash
   java main.MainInterfaceGrafica
   ```

---

## 👨‍💻 Autor
Desenvolvido por **Luca Samuel dos Santos** ([@LucaS4nt0s](https://github.com/LucaS4nt0s)).
