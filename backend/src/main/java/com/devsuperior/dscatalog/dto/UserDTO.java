package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	@NotBlank(message = "Campo obrigatório") //o firstname nao pode ser vazio
	private String firstName;
	private String lastName;
	@Email(message = "Favor entrar com email valido")
	private String email;
	
	//criar uma list roledto para transitar no json os dados do usuario e permissoes dele
	Set<RoleDTO> roles = new HashSet<>();
	
	
	
	public UserDTO() {
		
		
	}

	public UserDTO(long id, String firstName, String lastName, String email) {
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	
	}
	
	public UserDTO(User entity) {
		
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		//pegando a lista de roles que veio junto com entity(usuario)(carregou a lista junto pq usou @ManyToMany(fetch = FetchType.EAGER))
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		//vai acessar lista usuarios, para cada role la dentro ele vai instanciar um roleDTO a partir dele e inserir na lista de roles aqui do objeto (Set<RoleDTO> roles = new HashSet<>();)
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	
	
}
