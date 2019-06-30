package br.com.vsoares.swapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="swapi", url = "https://swapi.co/api/", fallback = SWApiClient.SWApiFallback.class)
interface SWApiClient {

	int PAGE_SIZE = 10;
	
	@GetMapping("planets/?search={name}")
	SWApiResult findPlanet(@PathVariable("name") String name);
	
	@GetMapping("planets/?page={page}")
	SWApiResult findAllPlanets(@PathVariable("page") int page);
	
	@Component
	static  class SWApiFallback implements SWApiClient {
		
		@Override
		public SWApiResult findPlanet(String name) {
			return new SWApiResult();
		}
		
		@Override
		public SWApiResult findAllPlanets(int page) {
			return new SWApiResult();
		}
		
	}
}

