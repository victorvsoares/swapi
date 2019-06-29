package br.com.vsoares.swapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="swapi", url = "https://swapi.co/api/")
interface SWApiClient {

	int PAGE_SIZE = 10;
	
	@GetMapping("planets/?search={name}")
	SWApiResult findPlanet(@PathVariable("name") String name);
	
	@GetMapping("planets/?page={page}")
	SWApiResult findAllPlanets(@PathVariable("page") int page);
	
}
