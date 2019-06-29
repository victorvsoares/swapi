package br.com.vsoares.swapi.controller.error;

import lombok.Getter;

@Getter
public class Erro {

	private String campo;

	private String mensagem;

	public Erro(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}

	public Erro(String mensagem) {
		this.mensagem = mensagem;
	}

}
