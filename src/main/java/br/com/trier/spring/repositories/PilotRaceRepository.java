package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.PilotRace;
import br.com.trier.spring.domain.Race;

@Repository
public interface PilotRaceRepository extends JpaRepository<PilotRace, Integer> {

	List<PilotRace> findPlacing(Integer placing);

	List<PilotRace> findByPilot(Pilot pilot);

	List<PilotRace> findByRace(Race race);
}
