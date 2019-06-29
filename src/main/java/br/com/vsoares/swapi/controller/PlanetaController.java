package br.com.vsoares.swapi.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.vsoares.swapi.client.SWApi;
import br.com.vsoares.swapi.controller.exception.RecursoNaoEncontradoException;
import br.com.vsoares.swapi.model.Planeta;
import br.com.vsoares.swapi.repository.PlanetaRepository;
import br.com.vsoares.swapi.request.PlanetaRequest;
import br.com.vsoares.swapi.service.PlanetaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/planetas")
@Api("Api de Planetas ")
public class PlanetaController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private PlanetaService planetaService;
	
	@Autowired
	private PlanetaRepository planetaRepository;
	
	@Autowired
	private SWApi swApi; 

	@ApiOperation(value = "Adiciona um novo planeta")
	@PostMapping
	@ResponseStatus(value = CREATED)
	public ResponseEntity<?> adicionar(@RequestBody @Valid PlanetaRequest planetaRequest, UriComponentsBuilder ucBuilder) {
		Planeta usuario = planetaService.salvar(planetaRequest.toPlaneta());		
		return ResponseEntity.created(ucBuilder.path("/planetas/{id}").buildAndExpand(usuario.getId()).toUri()).build();
	}
	
	@ApiOperation(value = "Lista todos os planetas do banco de dados")
	@GetMapping
	@ResponseStatus(value = OK)
	public ResponseEntity<Page<Planeta>> listar(@RequestParam(name = "page") Optional<Integer> page) {
		Page<Planeta> planetas = planetaRepository.findAll(PageRequest.of(page.orElse(0), PAGE_SIZE));
		return ResponseEntity.ok(planetas);
	}
	
	@ApiOperation(value = "Lista todos os planetas da API do Star Wars")
	@GetMapping(value = "/swapi")
	@ResponseStatus(value = OK)
	public ResponseEntity<Page<Planeta>> listarDaApi(@RequestParam(name = "page") Optional<Integer> page) {
		Page<Planeta> planetas = swApi.findAllPlanets(page.orElse(0));
		return ResponseEntity.ok(planetas);
	}
	
	@ApiOperation(value = "Obtém um planeta do banco de dados pelo id")
	@GetMapping(value = "/{id}")
	@ResponseStatus(value = OK)
	public ResponseEntity<Planeta> obter(@PathVariable("id") Optional<Planeta> planeta) {
		return ResponseEntity.ok(planeta.orElseThrow(() -> new RecursoNaoEncontradoException("Planeta não encontrado.")));
	}
	
	@ApiOperation(value = "Obtém um planeta do banco de dados pelo nome")
	@GetMapping(params = "nome")
	@ResponseStatus(value = OK)
	public ResponseEntity<Planeta> obter(@RequestParam("nome") String nome) {
		Planeta planeta = planetaRepository.findByNomeIgnoreCase(nome).orElseThrow(() -> new RecursoNaoEncontradoException("Planeta não encontrado."));
		return ResponseEntity.ok(planeta);
	}
	
	@ApiOperation(value = "Remove um planeta do banco de dados")
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(value = OK)
	public ResponseEntity<Void> remover(@PathVariable("id") Optional<Planeta> planeta) {
		planetaService.remover(planeta.orElseThrow(() -> new RecursoNaoEncontradoException("Planeta não encontrado.")));
		return ResponseEntity.ok().build();
	}

	
}
