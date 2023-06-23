package br.com.trier.spring.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.services.ChampionshipService;

@RestController
@RequestMapping("/championships")
public class ChampionshipResource {

	@Autowired
	private ChampionshipService service;

	@PostMapping
	public ResponseEntity<Championship> insert(@RequestBody Championship championship) {
		return ResponseEntity.ok(service.insert(championship));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Championship> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping
	public ResponseEntity<List<Championship>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Championship> update(@PathVariable Integer id, @RequestBody Championship championship) {
		championship.setId(id);
		return ResponseEntity.ok(service.update(championship));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/year/{year}")
	public ResponseEntity<List<Championship>> findByYear(@PathVariable Integer year) {
		return ResponseEntity.ok(service.findByYear(year));
	}

	@GetMapping("/year/{yearIn}/{yearFin}")
	public ResponseEntity<List<Championship>> findByYearBetween(@PathVariable Integer yearIn,
			@PathVariable Integer yearFin) {
		return ResponseEntity.ok(service.findByYearBetween(yearIn, yearFin));
	}

	@GetMapping("/description/{description}")
	public ResponseEntity<List<Championship>> findByDescriptionContaining(@PathVariable String description) {
		return ResponseEntity.ok(service.findByDescriptionContaining(description));
	}

}
