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

import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.dto.PilotDTO;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.PilotService;
import br.com.trier.spring.services.TeamService;

@RestController
@RequestMapping("/pilots")
public class PilotResource {

	@Autowired
	private PilotService service;

	@Autowired
	private CountryService countryService;

	@Autowired
	private TeamService teamService;

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<PilotDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<PilotDTO> insert(@RequestBody PilotDTO pilotDTO) {
		return ResponseEntity.ok(service.insert(new Pilot(pilotDTO, countryService.findById(pilotDTO.getCountryId()),
				teamService.findById(pilotDTO.getTeamId()))).toDTO());
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<PilotDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map(pilot -> pilot.toDTO()).toList());
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping("/{id}")
	public ResponseEntity<PilotDTO> update(@RequestBody PilotDTO pilotDTO, @PathVariable Integer id) {
		Pilot pilot = new Pilot(pilotDTO, countryService.findById(pilotDTO.getCountryId()),
				teamService.findById(pilotDTO.getTeamId()));
		pilot.setId(id);
		return ResponseEntity.ok(service.update(pilot).toDTO());

	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/name/{name}")
	public ResponseEntity<List<PilotDTO>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity
				.ok(service.findByNameStartsWithIgnoreCase(name).stream().map(pilot -> pilot.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/country/{id}")
	public ResponseEntity<List<PilotDTO>> findByCountryOrderByName(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByCountryOrderByName(countryService.findById(id)).stream()
				.map(pilot -> pilot.toDTO()).toList());
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/team/{id}")
	public ResponseEntity<List<PilotDTO>> findByTeamOrderByName(@PathVariable Integer id) {
		return ResponseEntity.ok(
				service.findByTeamOrderByName(teamService.findById(id)).stream().map(pilot -> pilot.toDTO()).toList());
	}

}
