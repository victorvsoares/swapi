package br.com.vsoares.swapi.request;

import static lombok.AccessLevel.PACKAGE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.vsoares.swapi.model.Planeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor
@Getter
public class PlanetaRequest {

	@NotBlank(message = "O nome do planeta deve ser informado.")
	@Size(min=1, max = 150, message = "O tamanho do nome deve ter entre {min} e {max} caracteres.")
	private String nome;
	
	@NotBlank(message = "O clima do planeta deve ser informado.")
	@Size(min=1, max = 150, message = "O tamanho do clima deve ter entre {min} e {max} caracteres.")
	private String clima;
	
	@NotBlank(message = "O terreno do planeta deve ser informado.")
	@Size(min=1, max = 200, message = "O tamanho do clima deve ter entre {min} e {max} caracteres.")
	private String terreno;
	
	public Planeta toPlaneta() {
		return new Planeta(nome, clima, terreno, 0);
	}
	
}
