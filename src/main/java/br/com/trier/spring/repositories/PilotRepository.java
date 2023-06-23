package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Country;
import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.Team;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer> {

	List<Pilot> findByNameStartsWithIgnoreCase(String name);

	List<Pilot> findByCountryOrderByName(Country country);

	List<Pilot> findByTeamOrderByName(Team team);
}
