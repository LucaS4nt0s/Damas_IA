package main;

// enum simples para dizer em que situação a partida está
public enum EstadoJogo {
	// jogo ainda não terminou
	EM_ANDAMENTO,
	// quem joga com peças brancas venceu
	VITORIA_BRANCAS,
	// quem joga com peças pretas venceu
	VITORIA_PRETAS,
	// partida empatada
	EMPATE
}
