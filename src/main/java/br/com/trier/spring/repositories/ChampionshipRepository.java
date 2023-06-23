package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Championship;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Integer> {
	List<Championship> findByYear(Integer year);

	List<Championship> findByDescriptionContainingIgnoreCase(String description);

	List<Championship> findByYearBetween(Integer yearIn, Integer yearFin);
}
