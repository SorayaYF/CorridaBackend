package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Country;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
class CountryServiceTest extends BaseTests {

	@Autowired
	CountryService countryService;

	@Test
	@DisplayName("Buscar por id")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findIdValid() {
		Country pais = countryService.findById(1);
		assertNotNull(pais);
		assertEquals(1, pais.getId());
		assertEquals("Brasil", pais.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> countryService.findById(10));
		assertEquals("O país 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Listar Todos")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void listAll() {
		assertEquals(2, countryService.listAll().size());
	}

	@Test
	@DisplayName("Listar Todos sem cadastro")
	void listAllEmpty() {
		var ex = assertThrows(ObjectNotFound.class, () -> countryService.listAll());
		assertEquals("Nenhum país cadastrado", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar pais")
	void insert() {
		Country pais = new Country(null, "EUA");
		countryService.insert(pais);
		assertEquals(1, countryService.listAll().size());
		assertEquals(1, pais.getId());
		assertEquals("EUA", pais.getName());
	}

	@Test
	@DisplayName("Cadastrar pais com nome duplicado")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void insertWithSameName() {
		Country pais = new Country(null, "Brasil");
		var ex = assertThrows(IntegrityViolation.class, () -> countryService.insert(pais));
		assertEquals("Nome já cadastrado: Brasil", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar pais")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void update() {
		Country pais = countryService.findById(1);
		assertNotNull(pais);
		assertEquals(1, pais.getId());
		assertEquals("Brasil", pais.getName());
		pais = new Country(1, "EUA");
		countryService.update(pais);
		assertEquals(2, countryService.listAll().size());
		assertEquals(1, pais.getId());
		assertEquals("EUA", pais.getName());
	}

	@Test
	@DisplayName("Alterar pais inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updateInvalid() {
		Country pais = new Country(10, "ABC");
		var ex = assertThrows(ObjectNotFound.class, () -> countryService.update(pais));
		assertEquals("O país 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir pais")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void delete() {
		assertEquals(2, countryService.listAll().size());
		countryService.delete(1);
		assertEquals(1, countryService.listAll().size());
		assertEquals(2, countryService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Excluir pais inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void deleteNonexistent() {
		assertEquals(2, countryService.listAll().size());
		var ex = assertThrows(ObjectNotFound.class, () -> countryService.delete(10));
		assertEquals("O país 10 não existe", ex.getMessage());
		assertEquals(2, countryService.listAll().size());
		assertEquals(1, countryService.listAll().get(0).getId());
	}

	@Test
	@DisplayName("Procurar por nome")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByName() {
		assertEquals(1, countryService.findByNameStartsWithIgnoreCase("br").size());
		var ex = assertThrows(ObjectNotFound.class, () -> countryService.findByNameStartsWithIgnoreCase("x"));
		assertEquals("Nenhum país inicia com x", ex.getMessage());

	}

}
