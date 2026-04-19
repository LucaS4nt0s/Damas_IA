package main;

public class ResultadoMovimento {
	private final boolean valido;
	private final boolean capturou;
	private final boolean continuaTurno;
	private final boolean turnoBrancas;
	private final EstadoJogo estadoJogo;
	private final String mensagem;

	public ResultadoMovimento(boolean valido, boolean capturou, boolean continuaTurno, boolean turnoBrancas,
			EstadoJogo estadoJogo, String mensagem) {
		this.valido = valido;
		this.capturou = capturou;
		this.continuaTurno = continuaTurno;
		this.turnoBrancas = turnoBrancas;
		this.estadoJogo = estadoJogo;
		this.mensagem = mensagem;
	}

	public boolean isValido() {
		return valido;
	}

	public boolean isCapturou() {
		return capturou;
	}

	public boolean isContinuaTurno() {
		return continuaTurno;
	}

	public boolean isTurnoBrancas() {
		return turnoBrancas;
	}

	public EstadoJogo getEstadoJogo() {
		return estadoJogo;
	}

	public String getMensagem() {
		return mensagem;
	}
}
