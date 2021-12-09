package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //classe de configuração
public class AppConfig {

	@Bean //anotação de metedo (Esta dizendo que essa instancia vai ser um componente gerenciado pelo springboot e vai poder injetar em outras classes e componentes
	public BCryptPasswordEncoder passwordEncoder() {
		 
		return new BCryptPasswordEncoder(); //foi instanciado
		
	}
	 
}
