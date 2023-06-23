package br.com.trier.spring.services;

import java.util.List;

import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.domain.Speedway;

public interface RaceService {
	
	Race findById(Integer id);
	
	Race insert(Race race);
	
	List<Race> listAll();
	
	Race update (Race race);
	
	void delete(Integer id);
	
	List<Race> findByDate(String date);
	
	List<Race> findByDateBetween(String dateIn, String dateFin);
	
	List<Race> findByChampionshipOrderByDate(Championship championship);
	
	List<Race> findBySpeedwayOrderByDate(Speedway speedway);

}
