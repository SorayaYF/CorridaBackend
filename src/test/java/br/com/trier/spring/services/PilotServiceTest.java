package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
class PilotServiceTest extends BaseTests {

	@Autowired
	PilotService service;

	@Autowired
	CountryService countryService;

	@Autowired
	TeamService teamService;

	@Test
	@DisplayName("Buscar por id")
	void findIdValid() {
		Pilot pilot = service.findById(1);
		assertNotNull(pilot);
		assertEquals(1, pilot.getId());
		assertEquals("Piloto 1", pilot.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("O piloto 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	void listAll() {
		assertEquals(2, service.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.listAll());
		assertEquals("Nenhum piloto cadastrado", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar piloto")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	void insert() {
		Pilot pilot = new Pilot(null, "Piloto 3", countryService.findById(1), teamService.findById(1));
		service.insert(pilot);
		assertEquals(1, service.listAll().size());
		assertEquals(1, pilot.getId());
		assertEquals("Piloto 3", pilot.getName());
	}

	@Test
	@DisplayName("Alterar piloto")
	void update() {
		Pilot pilot = service.findById(1);
		assertNotNull(pilot);
		assertEquals(1, pilot.getId());
		assertEquals("Piloto 1", pilot.getName());
		pilot = new Pilot(1, "Piloto 3", countryService.findById(1), teamService.findById(1));
		service.update(pilot);
		assertEquals(2, service.listAll().size());
		assertEquals(1, pilot.getId());
		assertEquals("Piloto 3", pilot.getName());
	}

	@Test
	@DisplayName("Alterar piloto inexistente")
	void updateInvalid() {
		Pilot pilot = new Pilot(10, "Piloto 3", countryService.findById(1), teamService.findById(1));
		var ex = assertThrows(ObjectNotFound.class, () -> service.update(pilot));
		assertEquals("O piloto 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir piloto")
	void delete() {
		Pilot pilot = service.findById(1);
		assertNotNull(pilot);
		assertEquals(1, pilot.getId());
		assertEquals("Piloto 1", pilot.getName());
		service.delete(1);
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(1));
		assertEquals("O piloto 1 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir piloto inexistente")
	void deleteNonexistent() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("O piloto 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Procurar pilotos por nome")
	void findByNameStartsWithIgnoreCase() {
		List<Pilot> lista = service.findByNameStartsWithIgnoreCase("P");
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Procurar pilotos por país")
	void findByCountryOrderByName() {
		List<Pilot> lista = service.findByCountryOrderByName(countryService.findById(1));
		assertEquals(1, lista.size());
	}

	@Test
	@DisplayName("Procurar pilotos por time")
	void findByTeamOrderByName() {
		List<Pilot> lista = service.findByTeamOrderByName(teamService.findById(1));
		assertEquals(1, lista.size());
	}

}