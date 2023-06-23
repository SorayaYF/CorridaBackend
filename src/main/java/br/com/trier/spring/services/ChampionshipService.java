package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.domain.Championship;

public interface ChampionshipService {

	Championship findById(Integer id);

	Championship insert(Championship championship);

	List<Championship> listAll();

	Championship update(Championship championship);

	void delete(Integer id);

	List<Championship> findByYear(Integer year);

	List<Championship> findByDescriptionContaining(String description);

	List<Championship> findByYearBetween(Integer yearIn, Integer yearFin);
	
}
