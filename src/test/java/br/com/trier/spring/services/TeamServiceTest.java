package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Team;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class TeamServiceTest extends BaseTests {

	@Autowired
	TeamService timeService;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findIdValid() {
		Team time = timeService.findById(1);
		assertNotNull(time);
		assertEquals(1, time.getId());
		assertEquals("Ferrari", time.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> timeService.findById(10));
		assertEquals("A equipe 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void listAll() {
		assertEquals(2, timeService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> timeService.listAll());
		assertEquals("Nenhuma equipe cadastrada", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar time")
	void insert() {
		Team time = new Team(null, "RedBull");
		timeService.insert(time);
		assertEquals(1, timeService.listAll().size());
		assertEquals(1, time.getId());
		assertEquals("RedBull", time.getName());
	}

	@Test
	@DisplayName("Cadastrar equipe com nome duplicado")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void insertWithSameName() {
		Team time = new Team(null, "Ferrari");
		var ex = assertThrows(IntegrityViolation.class, () -> timeService.insert(time));
		assertEquals("Nome já cadastrado: Ferrari", ex.getMessage());

	}

	@Test
	@DisplayName("Alterar equipe")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void update() {
		Team time = timeService.findById(1);
		assertNotNull(time);
		assertEquals(1, time.getId());
		assertEquals("Ferrari", time.getName());
		time = new Team(1, "RedBull");
		timeService.update(time);
		assertEquals(2, timeService.listAll().size());
		assertEquals(1, time.getId());
		assertEquals("RedBull", time.getName());
	}

	@Test
	@DisplayName("Alterar equipe inexistente")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void updateInvalid() {
		Team time = new Team(10, "RedBull");
		var ex = assertThrows(ObjectNotFound.class, () -> timeService.update(time));
		assertEquals("A equipe 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir equipe")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void delete() {
		assertEquals(2, timeService.listAll().size());
		timeService.delete(1);
		assertEquals(1, timeService.listAll().size());
		assertEquals(2, timeService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir equipe inexistente")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void deleteNonexistent() {
		assertEquals(2, timeService.listAll().size());
		var ex = assertThrows(ObjectNotFound.class, () -> timeService.delete(10));
		assertEquals("A equipe 10 não existe", ex.getMessage());
		assertEquals(2, timeService.listAll().size());
		assertEquals(1, timeService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Procurar equipe por nome")
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void findByName() {
		assertEquals(1, timeService.findByNameStartsWithIgnoreCase("m").size());
		var ex = assertThrows(ObjectNotFound.class, () -> timeService.findByNameStartsWithIgnoreCase("A"));
		assertEquals("Nenhuma equipe inicia com A", ex.getMessage());
	}
}
