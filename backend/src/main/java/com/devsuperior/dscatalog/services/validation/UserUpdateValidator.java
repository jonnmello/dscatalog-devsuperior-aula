package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	@Autowired
	private HttpServletRequest request; //serve para pegar o codigo da requisição do postman, que é o numero do usuario exemplo, {{host}}/users/1
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		@SuppressWarnings("unchecked")//para tirar o amarelinho
		var uriVars = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);//vai pegar um mapinha um dicionario com atributos da url e guardar em uma variavel
		long userId = Long.parseLong(uriVars.get("id"));// pegou o id e converteu ele para long usando o Long.parseLong()
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		
		User user = repository.findByEmail(dto.getEmail()); //tentando buscar email no repository por padrao se ele nao encontrar ele retorna nullo
		if(user != null && userId != user.getId()) { //se o id for do mesmo usuario e não tiver vazio 
									//esse email, é o campo na classe User e logo depois a message
			list.add(new FieldMessage("email", "Email já existe")); // incluindo o erro na lista para nao voltar vazio (sem erro)
		}
		
		for (FieldMessage e : list) { //percorrendo a lista fieldmessage e inserindo os erros abaixo na lista do beanvalidation
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
