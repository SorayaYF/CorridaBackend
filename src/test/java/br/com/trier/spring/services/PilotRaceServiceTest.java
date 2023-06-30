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
import br.com.trier.spring.domain.PilotRace;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/equipe.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pais.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/campeonato.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/pista.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/corrida.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/resources/sqls/piloto_corrida.sql")
class PilotRaceServiceTest extends BaseTests {

    @Autowired
    PilotRaceService service;

    @Autowired
    PilotService pilotService;

    @Autowired
    RaceService raceService;

    @Test
    @DisplayName("Buscar por id")
    void findByIdValid() {
        PilotRace pilotRace = service.findById(1);
        assertNotNull(pilotRace);
        assertEquals(1, pilotRace.getId());
        assertEquals(1, pilotRace.getPlacing());
        assertEquals(1, pilotRace.getPilot().getId());
        assertEquals(1, pilotRace.getRace().getId());
    }

    @Test
    @DisplayName("Buscar por id inexistente")
    void findByIdInvalid() {
    	var ex = assertThrows(ObjectNotFound.class, () -> service.findById(10));
        assertEquals("10 não existe", ex.getMessage());
    }

    @Test
    @DisplayName("Listar Todos")
    void listAll() {
        List<PilotRace> pilotRaceList = service.listAll();
        assertEquals(2, pilotRaceList.size());
    }

    @Test
    @DisplayName("Cadastrar piloto em corrida")
    @Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
    @Sql({ "classpath:/resources/sqls/piloto.sql" })
    @Sql({ "classpath:/resources/sqls/corrida.sql" })
    void insert() {
        Pilot pilot = pilotService.findById(1);
        Race race = raceService.findById(1);
        PilotRace pilotRace = new PilotRace(null, 5, pilot, race);
        service.insert(pilotRace);
        List<PilotRace> pilotRaceList = service.listAll();
        assertEquals(1, pilotRaceList.size());
        PilotRace insertedPilotRace = pilotRaceList.get(0);
        assertEquals(1, insertedPilotRace.getId());
        assertEquals(pilot, insertedPilotRace.getPilot());
        assertEquals(race, insertedPilotRace.getRace());
        assertEquals(5, insertedPilotRace.getPlacing());
    }

    @Test
    @DisplayName("Excluir piloto em corrida")
    void delete() {
        PilotRace pilotRace = service.findById(1);
        assertNotNull(pilotRace);
        assertEquals(1, pilotRace.getId());
        service.delete(1);
        assertThrows(ObjectNotFound.class, () -> service.findById(1));
    }

    @Test
    @DisplayName("Excluir piloto em corrida inexistente")
    void deleteNonexistent() {
        assertThrows(ObjectNotFound.class, () -> service.delete(10));
    }

    @Test
    @DisplayName("Procurar pilotos em uma corrida")
    void findByRace() {
        Race race = raceService.findById(1);
        List<PilotRace> pilotRaceList = service.findByRace(race);
        assertEquals(1, pilotRaceList.size());
    }

    @Test
    @DisplayName("Procurar corridas de um piloto")
    void findByPilot() {
        Pilot pilot = pilotService.findById(1);
        List<PilotRace> pilotRaceList = service.findByPilot(pilot);
        assertEquals(2, pilotRaceList.size());
    }
}
