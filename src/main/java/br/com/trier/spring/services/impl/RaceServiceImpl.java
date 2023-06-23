package br.com.trier.spring.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.domain.Championship;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.domain.Speedway;
import br.com.trier.spring.repositories.RaceRepository;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;
import br.com.trier.spring.utils.DateUtils;

@Service
public class RaceServiceImpl implements RaceService {

	@Autowired
	private RaceRepository repository;

	private void validateDate(Race race) {
		ZonedDateTime currentDate = ZonedDateTime.now();
		if (race.getChampionship() == null) {
			throw new IntegrityViolation("Campeonato não pode ser nulo");
		}
		if (race.getDate() == null) {
			throw new IntegrityViolation("Data da corrida inválida");
		}
		int championshipYear = race.getChampionship().getYear();
		int runYear = race.getDate().getYear();
		if (championshipYear != runYear) {
			throw new IntegrityViolation("Ano da corrida diferente do ano do campeonato");
		}
	}

	@Override
	public Race findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A corrida %s não existe".formatted(id)));
	}

	@Override
	public Race insert(Race race) {
		validateDate(race);
		return repository.save(race);
	}

	@Override
	public List<Race> listAll() {
		List<Race> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma corrida cadastrada");
		}
		return lista;
	}

	@Override
	public Race update(Race race) {
		findById(race.getId());
		validateDate(race);
		return repository.save(race);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));

	}

	@Override
	public List<Race> findByDate(String date) {
		ZonedDateTime zonedDate = DateUtils.strToZonedDateTime(date);
		List<Race> lista = repository.findByDate(zonedDate);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma corrida cadastrada com data %s".formatted(date));
		}
		return lista;
	}

	@Override
	public List<Race> findByDateBetween(String dateIn, String dateFin) {
		ZonedDateTime zonedDate1 = DateUtils.strToZonedDateTime(dateIn);
		ZonedDateTime zonedDate2 = DateUtils.strToZonedDateTime(dateFin);
		List<Race> lista = repository.findByDateBetween(zonedDate1, zonedDate2);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma corrida cadastrada com data entre %s e %s".formatted(dateIn, dateFin));
		}
		return lista;
	}

	@Override
	public List<Race> findByChampionshipOrderByDate(Championship championship) {
		List<Race> lista = repository.findByChampionshipOrderByDate(championship);
		if (lista.isEmpty()) {
			throw new ObjectNotFound(
					"Nenhuma corrida cadastrada no campeonato %s".formatted(championship.getDescription()));
		}
		return lista;
	}

	@Override
	public List<Race> findBySpeedwayOrderByDate(Speedway speedway) {
		List<Race> lista = repository.findBySpeedwayOrderByDate(speedway);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma corrida cadastrada na pista %s".formatted(speedway.getName()));
		}
		return lista;
	}
}
