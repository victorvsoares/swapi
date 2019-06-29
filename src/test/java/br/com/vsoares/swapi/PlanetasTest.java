package br.com.vsoares.swapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import br.com.vsoares.swapi.model.Planeta;
import br.com.vsoares.swapi.repository.PlanetaRepository;
import br.com.vsoares.swapi.request.PlanetaRequest;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = SwapiApplication.class)
@ActiveProfiles("test")
public class PlanetasTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private PlanetaRepository planetaRepository;
	
	private MockMvc mockMvc;

	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	

	@Test
    public void adicionar_planeta_sucesso() throws Exception {
        
		PlanetaRequest yavinIV = new PlanetaRequest("Yavin IV", "temperate, tropical", "jungle, rainforests");
			
		ResultActions result = mockMvc.perform(post("/planetas")
									  .contentType(APPLICATION_JSON_UTF8_VALUE)
									  .content(toJson(yavinIV))
									  .accept(APPLICATION_JSON_UTF8_VALUE))
									  .andExpect(status().isCreated());
		
	 	Optional<Planeta> planeta = planetaRepository.findByNomeIgnoreCase(yavinIV.getNome());

	 	assertThat(planeta.isPresent()).isTrue();
	 	assertThat(planeta.get().getNome()).isEqualTo(yavinIV.getNome());
	 	assertThat(planeta.get().getClima()).isEqualTo(yavinIV.getClima());
	 	assertThat(planeta.get().getTerreno()).isEqualTo(yavinIV.getTerreno());
	 	assertThat(planeta.get().getQtdAparicoesEmFilmes()).isEqualTo(1);
	 	
	 	result.andExpect(header().string(LOCATION, "http://localhost/planetas/" + planeta.get().getId()));
		
    }
	
	@Test
    public void adicionar_planeta_existente() throws Exception {
        
		PlanetaRequest alderaan = new PlanetaRequest("Alderaan", "temperate", "grasslands, mountains");
			
		mockMvc.perform(post("/planetas")
			   .contentType(APPLICATION_JSON_UTF8_VALUE)
			   .content(toJson(alderaan))
			   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isConflict())
			   .andExpect(jsonPath("mensagem", equalTo("Planeta já cadastrado.")));
    }
	
	@Test
    public void adicionar_planeta_inexistente_api() throws Exception {
        
		PlanetaRequest alderaan = new PlanetaRequest("Marte", "temperate", "grasslands, mountains");
			
		mockMvc.perform(post("/planetas")
			   .contentType(APPLICATION_JSON_UTF8_VALUE)
			   .content(toJson(alderaan))
			   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isNotFound())
			   .andExpect(jsonPath("mensagem", equalTo("Planeta não encontrado api do Star Wars")));
    }
	
	@Test
    public void adicionar_planeta_sem_campos_obrigatorios() throws Exception {
        
		PlanetaRequest alderaan = new PlanetaRequest(null, null, null);
			
		mockMvc.perform(post("/planetas")
			   .contentType(APPLICATION_JSON_UTF8_VALUE)
			   .content(toJson(alderaan))
			   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isBadRequest())
			   .andExpect(jsonPath("$[*].mensagem",  containsInAnyOrder(
		    				   "O nome do planeta deve ser informado.",
		    				   "O clima do planeta deve ser informado.",
		    				   "O terreno do planeta deve ser informado.")));
    }
	
    @Test
    public void obter_planeta_por_nome() throws Exception {
      
    	Planeta planeta = planetaRepository.findByNomeIgnoreCase("Alderaan").get();
			
		mockMvc.perform(get("/planetas?nome=Alderaan")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("id", equalTo(planeta.getId())))
	           .andExpect(jsonPath("nome", equalTo(planeta.getNome())))
	           .andExpect(jsonPath("clima", equalTo(planeta.getClima())))
	           .andExpect(jsonPath("terreno", equalTo(planeta.getTerreno())))
	           .andExpect(jsonPath("qtdAparicoesEmFilmes", equalTo(planeta.getQtdAparicoesEmFilmes())));
		
	}
    
    @Test
    public void obter_planeta_por_nome_inexistente() throws Exception {
      
		mockMvc.perform(get("/planetas?nome=Terra")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isNotFound())
			   .andExpect(jsonPath("mensagem", equalTo("Planeta não encontrado.")));
		
	}
    
    @Test
    public void obter_planeta_por_id() throws Exception {
      
    	Planeta planeta = planetaRepository.findById(1).get();
			
		mockMvc.perform(get("/planetas/1")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("id", equalTo(planeta.getId())))
	           .andExpect(jsonPath("nome", equalTo(planeta.getNome())))
	           .andExpect(jsonPath("clima", equalTo(planeta.getClima())))
	           .andExpect(jsonPath("terreno", equalTo(planeta.getTerreno())))
	           .andExpect(jsonPath("qtdAparicoesEmFilmes", equalTo(planeta.getQtdAparicoesEmFilmes())));
		
	}
    
    @Test
    public void obter_planeta_por_id_inexistente() throws Exception {
      
		mockMvc.perform(get("/planetas/100")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isNotFound())
			   .andExpect(jsonPath("mensagem", equalTo("Planeta não encontrado.")));
		
	}
    
    @Test
    public void remover_planeta() throws Exception {
      
    	Optional<Planeta> planeta = planetaRepository.findById(1);
    	
		mockMvc.perform(delete("/planetas/1")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isOk());
		
		Optional<Planeta> planeta2 = planetaRepository.findById(1);
		
		assertThat(planeta.isPresent()).isTrue();
		assertThat(planeta2.isPresent()).isFalse();
		
	}
    
    @Test
    public void remover_planeta_inexisente() throws Exception {
      
		mockMvc.perform(delete("/planetas/100")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isNotFound())
			   .andExpect(jsonPath("mensagem", equalTo("Planeta não encontrado.")));
		
	}
    
    @Test
    public void listar_planetas() throws Exception {
      
    	planetaRepository.save(new Planeta("Dagobah", "murky", "swamp, jungles", 0));
    	
		mockMvc.perform(get("/planetas")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("content", hasSize(2)))
			   .andExpect(jsonPath("content[0].nome", equalTo("Alderaan")));
	}
    
    @Test
    public void listar_planetas_SW_API() throws Exception {
      
		mockMvc.perform(get("/planetas/swapi?page=1")
	    	   .contentType(APPLICATION_JSON_UTF8_VALUE)
	    	   .accept(APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("content", hasSize(10)))
			   .andExpect(jsonPath("content[0].nome", equalTo("Utapau")))
			   .andExpect(jsonPath("content[0].clima", equalTo("temperate, arid, windy")))
			   .andExpect(jsonPath("content[0].terreno", equalTo("scrublands, savanna, canyons, sinkholes")))
			   .andExpect(jsonPath("content[0].qtdAparicoesEmFilmes", equalTo(1)))
			   .andExpect(jsonPath("content[1].nome", equalTo("Mustafar")))
			   .andExpect(jsonPath("content[9].nome", equalTo("Eriadu")));
		
	}
		
	private String toJson(Object obj) throws Exception {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new ParameterNamesModule());
		om.registerModule(new Jdk8Module());
		om.registerModule(new JavaTimeModule());
		return om.writeValueAsString(obj);
	}

	
}