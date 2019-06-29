package br.com.vsoares.swapi.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class SWApiRequestInterceptor implements RequestInterceptor {
	
	@Override
	public void apply(RequestTemplate template) {
		
		Map<String, Collection<String>> headers = Maps.newHashMap();
        headers.put("User-Agent", Arrays.asList("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"));
		
		template.headers(headers);
	}
}