package br.com.vsoares.swapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vsoares.swapi.client.SWApi;
import br.com.vsoares.swapi.controller.exception.RecursoNaoEncontradoException;
import br.com.vsoares.swapi.model.Planeta;
import br.com.vsoares.swapi.repository.PlanetaRepository;

@Service
public class PlanetaService {

	@Autowired
	private PlanetaRepository planetaRepository; 
	
	@Autowired
	private SWApi swApi;
	
	@Transactional
	public Planeta salvar(Planeta planeta) {

		try {

			Planeta swPlaneta = swApi.findPlaneta(planeta.getNome()).orElseThrow(() -> new RecursoNaoEncontradoException("Planeta não encontrado api do Star Wars"));
			
			planeta.setQtdAparicoesEmFilmes(swPlaneta.getQtdAparicoesEmFilmes());
			
			planeta = planetaRepository.save(planeta);

			return planeta;
			
		} catch (DataIntegrityViolationException  e) {
			throw new DataIntegrityViolationException ("Planeta já cadastrado.");
		}	
	}
	
	@Transactional
	public void remover(Planeta planeta) {
		planetaRepository.delete(planeta);	
	}
	
}
