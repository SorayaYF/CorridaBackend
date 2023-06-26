package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.domain.Race;
import br.com.trier.spring.domain.dto.RaceDTO;
import br.com.trier.spring.services.ChampionshipService;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.SpeedwayService;

@RestController
@RequestMapping("/races")
public class RaceResource {

	@Autowired
	private RaceService service;

	@Autowired
	private ChampionshipService championshipService;

	@Autowired
	private SpeedwayService speedwayService;

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<RaceDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<RaceDTO> insert(@RequestBody RaceDTO raceDTO) {
		return ResponseEntity
				.ok(service.insert(new Race(raceDTO, championshipService.findById(raceDTO.getChampionshipId()),
						speedwayService.findById(raceDTO.getSpeedwayId()))).toDTO());
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<RaceDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(race -> race.toDTO()).toList());
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping("/{id}")
	public ResponseEntity<RaceDTO> update(@RequestBody RaceDTO raceDTO, @PathVariable Integer id) {
		Race race = new Race(raceDTO, championshipService.findById(raceDTO.getChampionshipId()),
				speedwayService.findById(raceDTO.getSpeedwayId()));
		race.setId(id);
		return ResponseEntity.ok(service.update(race).toDTO());

	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/date/{date}")
	public ResponseEntity<List<RaceDTO>> findByDate(@PathVariable String date) {
		return ResponseEntity.ok(service.findByDate(date).stream().map(race -> race.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/date/{dateIn}/{dateFin}")
	public ResponseEntity<List<RaceDTO>> findByDateBetween(@PathVariable String dateIn, @PathVariable String dateFin) {
		return ResponseEntity.ok(service.findByDateBetween(dateIn, dateFin).stream().map(race -> race.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/speedway/{id}")
	public ResponseEntity<List<RaceDTO>> findBySpeedwayOrderByName(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findBySpeedwayOrderByDate(speedwayService.findById(id)).stream()
				.map(race -> race.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/championship/{id}")
	public ResponseEntity<List<RaceDTO>> findByChampionshipOrderByName(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByChampionshipOrderByDate(championshipService.findById(id)).stream()
				.map(race -> race.toDTO()).toList());
	}
}
