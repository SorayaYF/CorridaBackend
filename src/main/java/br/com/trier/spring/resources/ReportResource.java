package br.com.trier.spring.resources;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.domain.Country;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.domain.dto.RaceCountryYearDTO;
import br.com.trier.spring.domain.dto.RaceDTO;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.SpeedwayService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@RestController
@RequestMapping("/report")
public class ReportResource {
	
	@Autowired
	private RaceService raceService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private SpeedwayService speedwayService;
	
	@GetMapping("race-by-country-year/{countryId}/{year}")
	public ResponseEntity<RaceCountryYearDTO> findRaceByCountryAndYear(@PathVariable Integer countryId, @PathVariable Integer year){
		Country country = countryService.findById(countryId);
		
		List<RaceDTO> raceDTOS = speedwayService.findByCountryOrderBySizeDesc(country).stream().flatMap(speedway -> {
			try {
				return raceService.findBySpeedwayOrderByDate(speedway).stream();
			} catch (ObjectNotFound e) {
				return Stream.empty();
			}
		}).filter(race -> race.getDate().getYear() == year).map(Race::toDTO).toList();
		return ResponseEntity.ok(new RaceCountryYearDTO(year, country.getName(), raceDTOS.size(), raceDTOS));
	}

}
