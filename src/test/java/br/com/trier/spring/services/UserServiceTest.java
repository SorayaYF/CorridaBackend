package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.User;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class UserServiceTest extends BaseTests{

	@Autowired
	UserService service;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testFindByIdValid() {
		User user = service.findById(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("User 1", user.getName());
		assertEquals("email1", user.getEmail());
		assertEquals("senha1", user.getPassword());
		assertEquals("ADMIN,USER", user.getRoles());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testFindByIdInvalid() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testListAll() {
		assertEquals(2, service.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	@Sql
	void testListAllEmpty() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.listAll());
		assertEquals("Nenhum usuário cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Cadastrar usuário")
	void testInsertUser() {
		User user = new User(3, "User 3", "email3", "senha3", "ADMIN");
		service.insert(user);
		assertEquals(1, service.listAll().size());
		assertEquals(3, user.getId());
		assertEquals("User 3", user.getName());
		assertEquals("email3", user.getEmail());
		assertEquals("senha3", user.getPassword());
		assertEquals("ADMIN", user.getRoles());
	}

	@Test
	@DisplayName("Alterar usuário")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testUpdateUser() {
		User user = service.findById(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("User 1", user.getName());
		assertEquals("email1", user.getEmail());
		assertEquals("senha1", user.getPassword());
		assertEquals("ADMIN,USER", user.getRoles());
		user = new User(1, "User 3", "email3", "senha3", "ADMIN");
		service.update(user);
		assertEquals(2, service.listAll().size());
		assertEquals(1, user.getId());
		assertEquals("User 3", user.getName());
		assertEquals("email3", user.getEmail());
		assertEquals("senha3", user.getPassword());
		assertEquals("ADMIN", user.getRoles());
	}

	@Test
	@DisplayName("Alterar usuário inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testUpdateInvalidUser() {
		var user = new User(10, "User 3", "email3", "senha3", "ADMIN");
		var exception = assertThrows(ObjectNotFound.class, () -> service.update(user));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Excluir usuário")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testDeleteUser() {
		assertEquals(2, service.listAll().size());
		service.delete(1);
		assertEquals(1, service.listAll().size());
		assertEquals(2, service.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir usuário inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testDeleteNonexistentUser() {
		var exception = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Alterar usuário com e-mail duplicado")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testUpdateUserWithDuplicateEmail() {
		var user = new User(1, "insert", "email2", "senha", "ADMIN");
		var exception = assertThrows(IntegrityViolation.class, () -> service.update(user));
		assertEquals("Email já existente: email2", exception.getMessage());
	}

	@Test
	@DisplayName("Cadastrar usuário com e-mail duplicado")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testInsertUserWithDuplicateEmail() {
		User user = new User(null, "insert", "email1", "senha", "");
		var exception = assertThrows(IntegrityViolation.class, () -> service.insert(user));
		assertEquals("Email já existente: email1", exception.getMessage());
	}

	@Test
	@DisplayName("Procurar usuário por nome que inicia com")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void testFindUsersByNameStartsWith() {
		var userList = service.findByNameStartsWithIgnoreCase("u");
		assertEquals(2, userList.size());
		userList = service.findByNameStartsWithIgnoreCase("User 1");
		assertEquals(1, userList.size());
		var exception = assertThrows(ObjectNotFound.class, () -> service.findByNameStartsWithIgnoreCase("c"));
		assertEquals("Nenhum nome de usuário inicia com c cadastrado", exception.getMessage());
	}
}
