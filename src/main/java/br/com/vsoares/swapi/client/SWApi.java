package br.com.vsoares.swapi.client;

import static br.com.vsoares.swapi.client.SWApiClient.PAGE_SIZE;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.vsoares.swapi.model.Planeta;

@Service
public class SWApi {

	@Autowired
	private SWApiClient client;

	public Optional<Planeta> findPlaneta(String nome) {
		
		SWApiResult result = client.findPlanet(nome);
		
		Optional<Planeta> planeta = result.getPlanets()
										  .stream()
										  .filter(p -> p.getName().equalsIgnoreCase(nome))
										  .map(toPlaneta())
										  .findFirst();
		
		return planeta;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Planeta> findAllPlanets(int page) {
		
		SWApiResult result = client.findAllPlanets(page + 1);
		
		List<Planeta> planetas = result.getPlanets().stream().map(toPlaneta()).collect(Collectors.toList());
		
		return new PageImpl(planetas, PageRequest.of(page, PAGE_SIZE), result.getCount());
	}
	
	private Function<PlanetResult, Planeta> toPlaneta() {
		return p -> new Planeta(p.getName(), p.getClimate(), p.getTerrain(), p.getFilms().size());
	}

}
