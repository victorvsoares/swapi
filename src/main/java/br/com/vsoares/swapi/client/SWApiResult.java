package br.com.vsoares.swapi.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SWApiResult {

	private long count;
	
	@JsonProperty(value = "results")
	private List<PlanetResult> planets = Lists.newArrayList();
	
}
