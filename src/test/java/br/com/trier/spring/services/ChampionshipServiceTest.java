package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class ChampionshipServiceTest extends BaseTests {

	@Autowired
	ChampionshipService campeonatoService;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findIdValid() {
		Championship campeonato = campeonatoService.findById(1);
		assertNotNull(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals("Formula 1", campeonato.getDescription());
		assertEquals(2000, campeonato.getYear());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.findById(10));
		assertEquals("O campeonato 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void listAll() {
		assertEquals(2, campeonatoService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.listAll());
		assertEquals("Nenhum campeonato cadastrado", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar campeonato")
	void insert() {
		Championship campeonato = new Championship(null, "Formula 2", 2005);
		campeonatoService.insert(campeonato);
		assertEquals(1, campeonatoService.listAll().size());
		assertEquals(1, campeonato.getId());
		assertEquals("Formula 2", campeonato.getDescription());
		assertEquals(2005, campeonato.getYear());
	}

	@Test
	@DisplayName("Cadastrar campeonato com ano inválido")
	void insertWithInvalidYear() {
		Championship campeonato = new Championship(null, "Formula 2", 1980);
		var ex = assertThrows(IntegrityViolation.class, () -> campeonatoService.insert(campeonato));
		assertEquals("O campeonato deve estar ente 1990 e %s".formatted(LocalDate.now().plusYears(1).getYear()),
				ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar campeonato com ano nulo")
	void insertNull() {
		Championship campeonato = new Championship(null, "Formula 2", null);
		var ex = assertThrows(IntegrityViolation.class, () -> campeonatoService.insert(campeonato));
		assertEquals("O ano não pode ser nulo!", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void update() {
		Championship campeonato = campeonatoService.findById(1);
		assertNotNull(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals(2000, campeonato.getYear());
		assertEquals("Formula 1", campeonato.getDescription());
		campeonato = new Championship(1, "Formula 2", 1999);
		campeonatoService.update(campeonato);
		assertEquals(2, campeonatoService.listAll().size());
		assertEquals(1, campeonato.getId());
		assertEquals("Formula 2", campeonato.getDescription());
		assertEquals(2005, campeonato.getYear());
	}

	@Test
	@DisplayName("Altera campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void updateInvalid() {
		Championship campeonato = new Championship(10, "Formula 2", 2005);
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.update(campeonato));
		assertEquals("O campeonato 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void delete() {
		assertEquals(2, campeonatoService.listAll().size());
		campeonatoService.delete(1);
		assertEquals(2, campeonatoService.listAll().size());
		assertEquals(1, campeonatoService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void deleteNonexistent() {
		assertEquals(2, campeonatoService.listAll().size());
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.delete(10));
		assertEquals("O campeonato 10 não existe", ex.getMessage());
		assertEquals(2, campeonatoService.listAll().size());
		assertEquals(1, campeonatoService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Procurar campeonato por ano")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByYear() {
		assertEquals(1, campeonatoService.findByYear(2000).size());
	}

	@Test
	@DisplayName("Procurar campeonato por ano inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByYearNonexistent() {
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByYear(2005));
		assertEquals("Nenhum campeonato em 2005", ex.getMessage());

	}

	@Test
	@DisplayName("Procurar campeonato por ano inválido")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByInvalidYear() {
		var ex = assertThrows(IntegrityViolation.class, () -> campeonatoService.findByYear(1980));
		assertEquals("O campeonato deve estar ente 1990 e %s".formatted(LocalDate.now().plusYears(1).getYear()),
				ex.getMessage());

	}

	@Test
	@DisplayName("Procurar campeonato por descrição")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByDescription() {
		assertEquals(2, campeonatoService.findByDescriptionContaining("f").size());
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByDescriptionContaining("y"));
		assertEquals("Nenhum campeonato contem y", ex.getMessage());
	}

	@Test
	@DisplayName("Procurar campeonato por ano entre")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByYearBetween() {
		assertEquals(2, campeonatoService.findByYearBetween(2000, 2010).size());
		var ex = assertThrows(ObjectNotFound.class, () -> campeonatoService.findByYearBetween(2000, 2010));
		assertEquals("Nenhum campeonato entre 2000 e 2010", ex.getMessage());
	}

}