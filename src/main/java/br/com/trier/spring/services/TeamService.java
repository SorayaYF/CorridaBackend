package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.domain.Team;

public interface TeamService {

	Team findById(Integer id);

	Team insert(Team team);

	List<Team> listAll();

	Team update(Team team);

	void delete(Integer id);

	List<Team> findByNameStartsWithIgnoreCase(String name);

}
