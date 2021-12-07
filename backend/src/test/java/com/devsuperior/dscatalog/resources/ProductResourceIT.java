package com.devsuperior.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc  //para teste em camada WEB
@Transactional  //para cada teste ele reinicie os dados do banco
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName()	 throws Exception{ //esse throws Exception precisa ser colocado na camada WEB
		
		ResultActions result = 
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
						.accept(MediaType.APPLICATION_JSON));
						
		result.andExpect(status().isOk()); //Esperar que o status da resposta seja ok "200"
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));    //jsonPath serve para acessar objeto no caso o "totalElementes"
																					// .value para comparar o valor da variavel countTotalProducts se está igual a do "totalElementes"
		result.andExpect(jsonPath("$.content").exists()); // vendo se o componente "content" existe
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro")); //verificando o nome na primeira posição
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer")); //verificando o nome na segunda posição
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa")); //verificando o nome na terceira posição
		
		
	}
	
	@Test
	public void updateShouldReturnProductDTOwhenIdExists() throws Exception {
		
		ProductDTO productDTO= Factory.createProductDTO();		
		// transformar objeto java em string json
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		String expectedName = productDTO.getName(); // peguei e guardei o nome antes de altera-lo
		String expectedDescription = productDTO.getDescription();
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));

	}
	
	@Test
	public void updateShouldReturnNotFoundwhenIdDoesNotExists() throws Exception {
		
		ProductDTO productDTO= Factory.createProductDTO();		
		// transformar objeto java em string json
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
			
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	
	}
	
	
}
