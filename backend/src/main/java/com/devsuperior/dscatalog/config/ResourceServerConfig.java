package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = {"/oauth/token"}; //rota publica para logar
	
	private static final String[] OPERATOR_OR_ADMIN = {"/products/**","/categories/**"}; //as rotas padrao liberadas para operador e admin
	
	private static final String[] ADMIN = {"/users/**"}; //rota para admin
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore); //com isso aqui o Resource server consegue analisar o token e ver se esta batendo, se expirou ou nao se é valido etc
		
		
	}

	@Override
	public void configure(HttpSecurity http) throws Exception { //configurar as rotas quem pode acessar oq
		
		http.authorizeRequests() 
		.antMatchers(PUBLIC).permitAll() //antMatchers define autorizações (quem tiver acessando a rota do vetor PUBLIC vai ta liberado nao pede loguin
		.antMatchers(HttpMethod.GET,OPERATOR_OR_ADMIN).permitAll() //libera somente o metodo GET do OPERATOR_OR_ADMIN
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")// as rotas do OPERATOR_OR_ADMIN - hasAnyRole possui algum dos papeis- pode acessar se tiver operator ou admin cadastrado no banco de dados Role_operator e Role_admin
		.antMatchers(ADMIN).hasAnyRole("ADMIN")// a rota admin só pode por administradores
		.anyRequest().authenticated();// qualquer outra rota só pode se for autenticado, tem que ta logado
		
	}
	
	

}
