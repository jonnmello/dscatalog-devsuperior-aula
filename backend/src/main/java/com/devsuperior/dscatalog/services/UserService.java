package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //tem a disposição esse serviço passwordEncoder vai ser usada para transformar a senha cadastrada do usuario em o codigo hast grandão, codigo decript da senha

	@Autowired
	private UserRepository repository; // injeção de dependencia
	
	@Autowired
	private RoleRepository roleRepository; // injeção de dependencia

	

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));

	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {

		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) { //como vou inserir novo usuario
		User entity = new User(); //instanciar um usuario vazio
		copyDtoToEntity(dto, entity);//vou copiar os dados do dto para entidade
		entity.setPassword(passwordEncoder.encode(dto.getPassword())); //da um setpassword na senha que veio com dto, não é a senha que a pessoa digita (esse .encode que transforma uma string qualquer naquele codigo grande
		entity = repository.save(entity);// ai eu vou salvar
		return new UserDTO(entity); // e retorno o dto salvo

	}

	

	@Transactional
	public UserDTO update(Long id, UserDTO dto) { //para atualizar, pegar o id que quero atualizar
		try {
			User entity = repository.getOne(id);// e instanciar com o getone(getone só instancia uma entidade monitorada no jpa, ele não vai no banco de dados 
			copyDtoToEntity(dto, entity); // copiar os dados dto para entidade
			entity = repository.save(entity);// salvar
			return new UserDTO(entity);//e retorna o objeto atualizado
		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Id not found" + id);

		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {

			throw new ResourceNotFoundException("Id not found" + id);
		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity Violation");
		}
		
		}
	private void copyDtoToEntity(UserDTO dto, User entity) { //copiar os dados de um userDTO para um UserEntity
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
					
		entity.getRoles().clear(); // instanciar os roles do usuario, pegar e limpar com clear()
		for(RoleDTO roleDto : dto.getRoles()) { // para cada roleDTO no meu dto.getRoles (mudando as entidades)
			Role role = roleRepository.getOne(roleDto.getId());//instanciar um role provisorio com getone e ai inserir o role instanciado na entidade
			entity.getRoles().add(role); //inserir role na entidade
		}
		
		
	}

}
