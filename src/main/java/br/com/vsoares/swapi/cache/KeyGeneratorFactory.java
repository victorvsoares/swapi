package br.com.vsoares.swapi.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyGeneratorFactory {

	@Bean(name = "cacheKeyGenerator")
	public CacheKeyGenerator createCacheKeyGenerator() {
		return new CacheKeyGenerator("br.com.vsoares.swapi");
	}
	
	
}
