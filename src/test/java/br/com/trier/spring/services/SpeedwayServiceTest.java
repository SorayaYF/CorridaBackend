package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Speedway;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
class SpeedwayServiceTest extends BaseTests {

	@Autowired
	SpeedwayService speedwayService;

	@Autowired
	CountryService countryService;

	@Test
	@DisplayName("Buscar por id")
	void findIdValid() {
		Speedway speedway = speedwayService.findById(1);
		assertNotNull(speedway);
		assertEquals(1, speedway.getId());
		assertEquals("Pista 1", speedway.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> speedwayService.findById(10));
		assertEquals("A pista 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	void listAll() {
		assertEquals(2, speedwayService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> speedwayService.listAll());
		assertEquals("Nenhuma pista cadastrada", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar pista")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void insert() {
		Speedway speedway = new Speedway(null, "Pista 3", 1000, countryService.findById(1));
		speedwayService.insert(speedway);
		assertEquals(1, speedwayService.listAll().size());
		assertEquals(1, speedway.getId());
		assertEquals("Pista 3", speedway.getName());
	}

	@Test
	@DisplayName("Cadastrar pista com tamanho invalido")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void insertWithInvalidSize() {
		Speedway speedway = new Speedway(null, "Pista 3", 0, countryService.findById(1));
		var ex = assertThrows(IntegrityViolation.class, () -> speedwayService.insert(speedway));
		assertEquals("Tamanho da pista inválido", ex.getMessage());

	}

	@Test
	@DisplayName("Alterar pista")
	void update() {
		Speedway speedway = new Speedway(1, "Pista 3", 1000, countryService.findById(1));
		speedwayService.update(speedway);
		assertEquals(2, speedwayService.listAll().size());
		assertEquals(1, speedway.getId());
		assertEquals("Pista 3", speedway.getName());
	}

	@Test
	@DisplayName("Alterar pista inexistente")
	void updateInvalid() {
		Speedway speedway = new Speedway(10, "Pista 3", 1000, countryService.findById(1));
		var ex = assertThrows(ObjectNotFound.class, () -> speedwayService.update(speedway));
		assertEquals("A pista 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir pista")
	void delete() {
		Speedway speedway = speedwayService.findById(1);
		assertNotNull(speedway);
		assertEquals(1, speedway.getId());
		assertEquals("Pista 1", speedway.getName());
		speedwayService.delete(1);
		var ex = assertThrows(ObjectNotFound.class, () -> speedwayService.findById(1));
		assertEquals("A pista 1 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir pista inexistente")
	void deleteNonexistent() {
		var ex = assertThrows(ObjectNotFound.class, () -> speedwayService.delete(10));
		assertEquals("A pista 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar pistas por nome")
	void findByName() {
		List<Speedway> lista = speedwayService.findByNameStartsWithIgnoreCase("P");
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Encontrar pistas por país")
	void findByCountry() {
		List<Speedway> lista = speedwayService.findByCountryOrderBySizeDesc(countryService.findById(1));
		assertEquals(1, lista.size());
	}

	@Test
	@DisplayName("Encontrar pistas por tamanho entre")
	void findBySizeBetween() {
		List<Speedway> lista = speedwayService.findBySizeBetween(1000, 2000);
		assertEquals(2, lista.size());
	}

}
