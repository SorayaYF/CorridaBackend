package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.PilotRace;
import br.com.trier.spring.domain.Race;

public interface PilotRaceService {

	PilotRace findById(Integer id);

	PilotRace insert(PilotRace pilotRace);

	List<PilotRace> listAll();

	PilotRace update(PilotRace pilotRace);

	void delete(Integer id);

	List<PilotRace> findByPilot(Pilot pilot);

	List<PilotRace> findByRace(Race race);

}
