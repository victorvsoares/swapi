package br.com.vsoares.swapi.client;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanetResult {
	
	private String name;
	
	private String climate;
	
	private String terrain;
	
	private List<String> films = Lists.newArrayList();

}
