package main;

// objeto de retorno para informar o resultado completo de uma jogada
public class ResultadoMovimento {
	// diz se o movimento foi aceito
	private final boolean valido;
	// diz se houve captura de peça nessa jogada
	private final boolean capturou;
	// diz se o turno continua para o mesmo jogador
	private final boolean continuaTurno;
	// turno atual depois da jogada (true=brancas)
	private final boolean turnoBrancas;
	// estado geral da partida depois da jogada
	private final EstadoJogo estadoJogo;
	// mensagem amigável para interface/debug
	private final String mensagem;

	// construtor com todos os dados do resultado
	public ResultadoMovimento(boolean valido, boolean capturou, boolean continuaTurno, boolean turnoBrancas,
			EstadoJogo estadoJogo, String mensagem) {
		this.valido = valido;
		this.capturou = capturou;
		this.continuaTurno = continuaTurno;
		this.turnoBrancas = turnoBrancas;
		this.estadoJogo = estadoJogo;
		this.mensagem = mensagem;
	}

	// retorna se foi válido
	public boolean isValido() {
		return valido;
	}

	// retorna se capturou peça
	public boolean isCapturou() {
		return capturou;
	}

	// retorna se o turno continua com o mesmo jogador
	public boolean isContinuaTurno() {
		return continuaTurno;
	}

	// retorna de quem é o turno (true=brancas)
	public boolean isTurnoBrancas() {
		return turnoBrancas;
	}

	// retorna estado global da partida
	public EstadoJogo getEstadoJogo() {
		return estadoJogo;
	}

	// retorna mensagem textual do resultado
	public String getMensagem() {
		return mensagem;
	}
}
