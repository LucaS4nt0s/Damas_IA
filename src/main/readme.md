Neste trabalho você deverá desenvolver um jogo de damas utilizando algoritmos de busca
em árvores. Os jogadores serão o usuário e a máquina, representada aqui por um algoritmo, que
deverá tomar a decisão de forma inteligente. A "inteligência" da máquina deverá ser baseada no
algoritmo MiniMax, que é um algoritmo utilizado em Inteligência Artificial clássica. Basicamente,
este algoritmo deverá gerar as possibilidades no jogo de damas, cujo tabuleiro é reduzido (6x6), e
avaliar quais as melhores jogadas que o computador deverá tomar em cada estado do jogo. Para
isso, você deverá desenvolver toda lógica do jogo utilizando o código base disponibilizado na
página da disciplina. O código base pode ser alterado, porém toda a lógica de interface gráfica do
jogo deverá ser mantida, conforme exemplo apresentado na Figura 1.

A seguir, são apresentadas alguns requisitos funcionais obrigatórios nas regras do jogo
desenvolvido neste projeto:
● O usuário poderá definir quem utilizará as peças brancas, ou seja, quem começa o jogo;
● É obrigatório realizar o movimento de captura do máximo de peças em um única jogada;
● Não é permitido iniciar o movimento de captura de peças “para trás”, exceto para as
damas, que podem iniciar o movimento de captura em qualquer direção;
● O movimento da é livre nas diagonais (múltiplas casas);
● A última peça a ser capturada indica a posição que a peça deverá parar (posição
subsequente na direção da captura), inclusive para as damas;
● Na impossibilidade de efetuar jogadas, o jogador travado perde o jogo
● Se existirem somente duas damas e não for possível realizar um captura na jogada seguinte,
então deverá ser declarado empate entre os jogadores.
O algoritmo MiniMax deverá avaliar todas as possibilidades de jogadas e selecionar a
"melhor" a cada iteração. Para padronizar, a máquina deverá ser representada pelo Max, enquanto
o usuário será o Min. A função de avaliação será:
➔ +1 - vitória do Max;
➔ -1 - vitória do Min;
➔ 0 - empate;
Assumindo que o espaço de busca cresce exponencialmente de acordo com o número de
jogadas a cada iteração, algumas definições para lidar com essa restrição devem ser definidas para
o desenvolvimento do projeto, incluindo:
● A classe Node, disponibilizada no código base, deverá ser obrigatoriamente utilizada para
construção da árvore de possibilidades;
● A altura mínima da árvore deverá ser de 10 nós na avaliação do algoritmo MiniMax
o Caso a altura da árvore não seja suficiente para avaliar qual jogador é o ganhador,
você deverá implementar uma heurística para analisar a n-ésima jogada e inferir
quem está ganhando ou perdendo (+1, -1 ou 0);
● Quando a altura máxima (10 nós) for atingida na avaliação do MiniMax, o software deverá
executar novamente o algoritmo MiniMax a partir do ponto atual para classificar
novamente as jogadas futuras. Essa ação deverá ser realizada quantas vezes for necessário
até que o fim do jogo seja atingido.
● O usuário poderá informar a profundidade máxima que o algoritmo MiniMax poderá
descer na árvore de possibilidades a partir do ponto da jogada. Isso deverá determinar o nível de inteligência do algoritmo (poderá ser de 1 a 9). A partir do nível máximo atingido,
as jogadas deverão ser aleatórias, para simular a inteligência do algoritmo;
● Além de implementar a função de avaliação sugerida (+1, -1 ou 0),, você deverá
gerar/inventar uma heurística diferente para avaliar o MiniMax das jogadas, de forma a
preencher a árvore de possibilidades;