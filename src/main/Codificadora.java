package main;

public class Codificadora {
    // mapa fixo das casas válidas do tabuleiro (6x6)
    // x = casa inválida, letras = casas jogáveis
    private final static char[][] mapaTabuleiro = {
        {'X', 'A', 'X', 'B', 'X', 'C'},
        {'D', 'X', 'E', 'X', 'F', 'X'},
        {'X', 'G', 'X', 'H', 'X', 'I'},
        {'J', 'X', 'K', 'X', 'L', 'X'},
        {'X', 'M', 'X', 'N', 'X', 'O'},
        {'P', 'X', 'Q', 'X', 'R', 'X'},
    };

    // converte uma letra (ex: 'a') para coordenada [linha, coluna]
    public static int[] decodificar(char posicao){
        int coordenadas[] = {0,0};
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                if(mapaTabuleiro[i][j] == posicao){
                    coordenadas[0] = i;
                    coordenadas[1] = j;
                    return coordenadas;
                }
            }
        }
        return coordenadas;
    }

    // converte coordenada [linha, coluna] para letra da casa no mapa
    public static char codificar(int linha, int coluna){
        // proteção para evitar coordenada fora do tabuleiro
        if (linha < 0 || linha >= 6 || coluna < 0 || coluna >= 6) {
            throw new IllegalArgumentException("Coordenada invalida: (" + linha + "," + coluna + ")");
        }
        return mapaTabuleiro[linha][coluna];
    }
}
