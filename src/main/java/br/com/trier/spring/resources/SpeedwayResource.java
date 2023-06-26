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

import br.com.trier.spring.domain.Speedway;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.SpeedwayService;

@RestController
@RequestMapping("/speedways")
public class SpeedwayResource {

	@Autowired
	private SpeedwayService service;

	@Autowired
	private CountryService countryService;

	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<Speedway> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<Speedway> insert(@RequestBody Speedway speedway) {
		countryService.findById(speedway.getCountry().getId());
		return ResponseEntity.ok(service.insert(speedway));
	}

	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<Speedway>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@Secured({"ROLE_ADMIN"})
	@PostMapping("/{id}")
	public ResponseEntity<Speedway> update(@RequestBody Speedway speedway, @PathVariable Integer id) {
		countryService.findById(speedway.getCountry().getId());
		speedway.setId(id);
		return ResponseEntity.ok(service.update(speedway));

	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Speedway>> findByNameStartsWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartsWithIgnoreCase(name));

	}

	@Secured({"ROLE_USER"})
	@GetMapping("/size/{sizeIn}/{sizeFin}")
	public ResponseEntity<List<Speedway>> findBySizeBetween(@PathVariable Integer sizeIn,
			@PathVariable Integer sizeFin) {
		return ResponseEntity.ok(service.findBySizeBetween(sizeIn, sizeFin));
	}

	@Secured({"ROLE_USER"})
	@GetMapping("/country/{id}")
	public ResponseEntity<List<Speedway>> findByCountryOrderBySizeDesc(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findByCountryOrderBySizeDesc(countryService.findById(id)));
	}

}
