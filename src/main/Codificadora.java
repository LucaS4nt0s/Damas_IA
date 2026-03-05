package main;

public class Codificadora {
    private final static char[][] mapaTabuleiro = {
        {'0', 'A', '0', 'B', '0', 'C'},
        {'D', '0', 'E', '0', 'F', '0'},
        {'0', 'G', '0', 'H', '0', 'I'},
        {'J', '0', 'K', '0', 'L', '0'},
        {'0', 'M', '0', 'N', '0', 'O'},
        {'P', '0', 'Q', '0', 'R', '0'},
    };

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

    public static char codificar(int linha, int coluna){
        return mapaTabuleiro[linha][coluna];
    }
}
