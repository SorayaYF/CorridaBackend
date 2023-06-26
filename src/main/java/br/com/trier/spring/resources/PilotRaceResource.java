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

import br.com.trier.spring.domain.PilotRace;
import br.com.trier.spring.domain.dto.PilotRaceDTO;
import br.com.trier.spring.services.PilotRaceService;
import br.com.trier.spring.services.PilotService;
import br.com.trier.spring.services.RaceService;

@RestController
@RequestMapping("/pilots_races")
public class PilotRaceResource {

	@Autowired
	private PilotRaceService service;

	@Autowired
	private RaceService raceService;

	@Autowired
	private PilotService pilotService;

	@Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<PilotRaceDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<PilotRaceDTO> insert(@RequestBody PilotRaceDTO pilotRaceDTO) {
		return ResponseEntity
				.ok(service.insert(new PilotRace(pilotRaceDTO, raceService.findById(pilotRaceDTO.getRaceId()),
						pilotService.findById(pilotRaceDTO.getPilotId()))).toDTO());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<PilotRaceDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(race -> race.toDTO()).toList());
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping("/{id}")
	public ResponseEntity<PilotRaceDTO> update(@RequestBody PilotRaceDTO pilotRaceDTO, @PathVariable Integer id) {
		PilotRace pilotRace = new PilotRace(pilotRaceDTO, raceService.findById(pilotRaceDTO.getRaceId()),
				pilotService.findById(pilotRaceDTO.getPilotId()));
		pilotRace.setId(id);
		return ResponseEntity.ok(service.update(pilotRace).toDTO());

	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
}
