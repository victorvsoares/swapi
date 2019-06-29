package br.com.vsoares.swapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import br.com.vsoares.swapi.model.Planeta;

public interface PlanetaRepository extends JpaRepository<Planeta, Integer>, JpaSpecificationExecutor<Planeta> { 

	Optional<Planeta> findByNomeIgnoreCase(@Param("nome") String nome); 
	
}
