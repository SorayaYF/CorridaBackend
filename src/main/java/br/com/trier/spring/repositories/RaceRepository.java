package br.com.trier.spring.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.domain.Speedway;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer> {

	List<Race> findByDate(ZonedDateTime date);

	List<Race> findByDateBetween(ZonedDateTime dateIn, ZonedDateTime dateFin);

	List<Race> findByChampionshipOrderByDate(Championship championship);

	List<Race> findBySpeedwayOrderByDate(Speedway speedway);

}
