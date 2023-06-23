package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.domain.Country;
import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.Team;

public interface PilotService {

	Pilot findById(Integer id);

	Pilot insert(Pilot pilot);

	List<Pilot> listAll();

	Pilot update(Pilot pilot);

	void delete(Integer id);

	List<Pilot> findByNameStartsWithIgnoreCase(String name);

	List<Pilot> findByCountryOrderByName(Country country);

	List<Pilot> findByTeamOrderByName(Team team);

}
