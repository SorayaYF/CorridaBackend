package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

	Team findByName(String name);

	List<Team> findByNameStartsWithIgnoreCase(String name);

}
