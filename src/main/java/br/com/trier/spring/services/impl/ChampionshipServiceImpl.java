package br.com.trier.spring.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.repositories.ChampionshipRepository;
import br.com.trier.spring.services.ChampionshipService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class ChampionshipServiceImpl implements ChampionshipService {

	@Autowired
	private ChampionshipRepository repository;

	private void validYear(Integer year) {
		if (year == null) {
			throw new IntegrityViolation("O ano não pode ser nulo");
		}
		if (year < 1990 || year > LocalDateTime.now().getYear()) {
			throw new IntegrityViolation("O campeonato deve estar ente 1990 e %s".formatted(LocalDate.now().plusYears(1).getYear()));
		}
	}

	@Override
	public Championship findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjectNotFound("O campeonato %s não existe".formatted(id)));
	}

	@Override
	public Championship insert(Championship championship) {
		validYear(championship.getYear());
		return repository.save(championship);
	}

	@Override
	public List<Championship> listAll() {
		List<Championship> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum campeonato cadastrado");
		}
		return lista;
	}

	@Override
	public Championship update(Championship championship) {
		findById(championship.getId());
		validYear(championship.getYear());
		return repository.save(championship);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));
	}

	@Override
	public List<Championship> findByYear(Integer year) {
		List<Championship> lista = repository.findByYear(year);
		validYear(year);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum campeonato em %s".formatted(year));
		}
		return repository.findByYear(year);
	}

	@Override
	public List<Championship> findByDescriptionContaining(String description) {
		List<Championship> lista = repository.findByDescriptionContainingIgnoreCase(description);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum campeonato contem %s".formatted(description));
		}
		return lista;
	}

	@Override
	public List<Championship> findByYearBetween(Integer yearIn, Integer yearFin) {
		List<Championship> lista = repository.findByYearBetween(yearIn, yearFin);
		validYear(yearIn);
		validYear(yearFin);
		if (lista.isEmpty()) {
			throw new ObjectNotFound(
					"Nenhum campeonato entre %s e %s".formatted(yearIn, yearFin));
		}
		return lista;
	}
}
