package br.com.vsoares.swapi.model;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "PLANETA", uniqueConstraints = @UniqueConstraint(columnNames = "NOME",  name = "UK_PLANETA_NOME"))
@Entity
@NoArgsConstructor(access = PACKAGE)
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Planeta {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Setter(NONE)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NOME", nullable = false, length = 150)
	@NotBlank(message = "O nome do planeta deve ser informado.")
	@Size(min=1, max = 150, message = "O tamanho do nome deve ter entre {min} e {max} caracteres.")
	private String nome;
	
	@Column(name = "CLIMA", nullable = false, length = 100)
	@NotBlank(message = "O clima do planeta deve ser informado.")
	@Size(min=1, max = 150, message = "O tamanho do clima deve ter entre {min} e {max} caracteres.")
	private String clima;
	
	@Column(name = "TERRENO", nullable = false, length = 200)
	@NotBlank(message = "O terreno do planeta deve ser informado.")
	@Size(min=1, max = 200, message = "O tamanho do clima deve ter entre {min} e {max} caracteres.")
	private String terreno;
	
	@Column(name = "QTD_APARICOES_FILMES")
	@NotNull(message = "A quantidade de aparições deve ser informada.")
	private Integer qtdAparicoesEmFilmes;

	public Planeta(String nome, String clima, String terreno, Integer qtdAparicoesEmFilmes) {
		this();
		this.nome = nome;
		this.clima = clima;
		this.terreno = terreno;
		this.qtdAparicoesEmFilmes = qtdAparicoesEmFilmes;
	}
	
}
