package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest   
@Transactional
public class ProductServiceIT {
	
	// O @Transactional faz o banco zerar os dados depois de cada teste
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;  
	
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
	public void deleteShouldDeleteResourceWhenIdExists() {
		//ativando delete produto id
		service.delete(existingId);
		//verificando se é igual counttotalproducts -1 , com total de registro no banco esse (.count())
		Assertions.assertEquals(countTotalProducts -1, repository.count());
		
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		//verificando exerção ResourceNotFoundException
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			
			//ativando delete não existe  produto id
			service.delete(nonExistingId);
		});
		
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0size10() {
		//PageRequest é uma classe que implementa o Page - Criado objeto de pagina "page 0 size 10"
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		//atribuir o resultado da chamada de service.findallpaged para uma variavel tipo page
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		//Testar se a pagina não está vazia *como o banco tem 25 produtos chamando page 0 size 10 tem que vim
		//isEmpty() retorna verdadeiro se estiver vazio  
		Assertions.assertFalse(result.isEmpty());
		//Testar se o numero da pagina é realmente 0
		Assertions.assertEquals(0, result.getNumber());
		//Testar se o numero de produtos na pagina  é realmente 10
		Assertions.assertEquals(10, result.getSize());
		//Testar total de produtos 
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
		
	}
	
	@Test
	public void findAllPagedShouldReturnEmpytPageWhenPageDoesNotExists() {
		
		
		//PageRequest é uma classe que implementa o Page - Criado objeto de pagina "page 50 size 10"
		PageRequest pageRequest = PageRequest.of(50, 10); //criado objeto que não existe
		
		//atribuir o resultado da chamada de service.findallpaged para uma variavel tipo page
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
	
		//isEmpty() retorna verdadeiro se estiver vazio  
		//assertTrue tem que ser verdadeiro se o resultado estiver vazio
		Assertions.assertTrue(result.isEmpty());
		//Testar se a pagina está vazia *como o banco tem 25 produtos chamando page 50 size 10 tem que vim vazio
		
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSorteByName() {
		
		
		//PageRequest é uma classe que implementa o Page - Criado objeto de pagina "page 0 size 10 e ordenada por nome"
		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("name")); //criado objeto e ordenando por name 
		
		//atribuir o resultado da chamada de service.findallpaged para uma variavel tipo page
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
	
		//isEmpty() retorna verdadeiro se estiver vazio  
		//assertFalse se o resultado não é vazio (não pode ser vazio)
		Assertions.assertFalse(result.isEmpty());
		//Verficando se o primeiro item da lista esta correto - getcontent pega elementos da lista, get() pega a posiçao e getname o nome
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName()); // comparando numero 2
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName()); //comparando numero3

		
	}
	

}
