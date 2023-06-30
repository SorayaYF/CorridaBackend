package br.com.trier.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import br.com.trier.spring.BaseTests;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import br.com.trier.spring.utils.DateUtils;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
class RaceServiceTest extends BaseTests {

	@Autowired
	RaceService service;
	
	@Autowired
	SpeedwayService speedwayService;
	
	@Autowired
	ChampionshipService championshipService;
	
	@Test
	@DisplayName("Buscar por id")
	void findIdValid() {
		Race race = service.findById(1);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals(28, race.getDate().getDayOfMonth());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	void findIdInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(10));
		assertEquals("A corrida 10 não existe", ex.getMessage());
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
		assertEquals("Nenhuma corrida cadastrada", ex.getMessage());
	}

	@Test
	@DisplayName("Cadastrar corrida")
	@Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	void insert() {
		Race race = new Race(null, DateUtils.strToZonedDateTime("01/01/2000"), speedwayService.findById(1), championshipService.findById(1) );
		service.insert(race);
		assertEquals(1, service.listAll().size());
		assertEquals(1, race.getId());
		assertEquals(2000, race.getDate().getYear());
	}


	@Test
	@DisplayName("Alterar corrida")
	void update() {
		Race race = new Race(1, DateUtils.strToZonedDateTime("01/01/2000"), speedwayService.findById(1), championshipService.findById(1) );
		service.update(race);
		assertEquals(2, service.listAll().size());
		assertEquals(1, race.getId());
		assertEquals(2000, race.getDate().getYear());
	}
	
	@Test
	@DisplayName("Alterar corrida que não condiz com ano do campeonato")
	void updateInvalidDate() {
		Race race = new Race(1, DateUtils.strToZonedDateTime("01/01/2001"), speedwayService.findById(1), championshipService.findById(1) );
		var ex = assertThrows(IntegrityViolation.class, () -> service.update(race));
		assertEquals("Ano da corrida diferente do ano do campeonato", ex.getMessage());
	}

	@Test
	@DisplayName("Alterar corrida inexistente")
	void updateInvalid() {
		Race race = new Race(10, DateUtils.strToZonedDateTime("01/01/2000"), speedwayService.findById(1), championshipService.findById(1) );
		var ex = assertThrows(ObjectNotFound.class, () -> service.update(race));
		assertEquals("A corrida 10 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir corrida")
	void delete() {
		Race race = service.findById(1);
		assertNotNull(race);
		assertEquals(1, race.getId());
		assertEquals(2000, race.getDate().getYear());
		service.delete(1);
		var ex = assertThrows(ObjectNotFound.class, () -> service.findById(1));
		assertEquals("A corrida 1 não existe", ex.getMessage());
	}

	@Test
	@DisplayName("Excluir corrida inexistente")
	void deleteInvalid() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.delete(10));
		assertEquals("A corrida 10 não existe", ex.getMessage());
	}

	
	@Test
	@DisplayName("Encontrar por data")
	void findByDate() {
		List<Race> lista = service.findByDate("10/10/2010");
		assertEquals(1, lista.size());			
	}
	
	@Test
	@DisplayName("Encontrar por data inexistente")
	void findByDateNonExist() {
		List<Race> lista = service.findByDate("10/10/2010");
		assertEquals(1, lista.size());	
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByDate("07/06/2000"));
		assertEquals("Nenhuma corrida cadastrada com data 07/06/2000", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por data entre")
	void findByDateBetween() {
		List<Race> lista = service.findByDateBetween("06/06/2000", "10/10/2010");
		assertEquals(2, lista.size());	
	}
	
	@Test
	@DisplayName("Encontrar por data entre inexistente")
	void findByDateBetweenNonExist() {	
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByDateBetween("10/10/2000", "06/06/2010"));
		assertEquals("Nenhuma corrida cadastrada com data entre 10/10/2000 e 06/06/2010", ex.getMessage());
	}

	@Test
	@DisplayName("Encontrar por campeonato ordenado por data")
	void findByChampionshipOrderByDate() {
		List<Race> lista = service.findByChampionshipOrderByDate(championshipService.findById(1));
		assertEquals(2, lista.size());
	}
	
	@Test
	@DisplayName("Encontrar por campeonato ordenado por data inexistente")
	void findByChampionshipOrderByDateNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findByChampionshipOrderByDate(championshipService.findById(2)));
		assertEquals("Nenhuma corrida cadastrada no campeonato Formula E", ex.getMessage());
	}

	@Test
	@DisplayName("Encontra por pista ordenado por data")
	void findByRunwayOrderByDate() {
		List<Race> lista = service.findBySpeedwayOrderByDate(speedwayService.findById(1));
		assertEquals(2, lista.size());
	}
	
	@Test
	@DisplayName("Encontra por pista ordenado por data inexistente")
	void findByRunwayOrderByDateNonExist() {
		var ex = assertThrows(ObjectNotFound.class, () -> service.findBySpeedwayOrderByDate(speedwayService.findById(2)));
		assertEquals("Nenhuma corrida cadastrada na pista Pista 2", ex.getMessage());
	}
	


}
