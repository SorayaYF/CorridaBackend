package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.domain.Pilot;
import br.com.trier.spring.domain.PilotRace;
import br.com.trier.spring.domain.Race;
import br.com.trier.spring.repositories.PilotRaceRepository;
import br.com.trier.spring.services.PilotRaceService;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class PilotRaceServiceImpl implements PilotRaceService {
	
	@Autowired
	private PilotRaceRepository repository;
	
	@Override
	public PilotRace findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("%s não existe".formatted(id)));
	}
	
	@Override
	public PilotRace insert(PilotRace pilotRace) {
		return repository.save(pilotRace);
	}
	
	@Override
	public List<PilotRace> listAll() {
		List<PilotRace> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw  new ObjectNotFound("Nenhum cadastro encontrado");
		}
		return lista;
	}
	
	@Override
	public PilotRace update(PilotRace pilotRace) {
		findById(pilotRace.getId());
		return repository.save(pilotRace);
	}
	
	@Override
	public void delete(Integer id) {
		PilotRace pilotRace = findById(id);
		repository.delete(pilotRace);
	}

	@Override
	public List<PilotRace> findByPilot(Pilot pilot) {
		List<PilotRace> lista = repository.findByPilot(pilot);
		if (lista.isEmpty()) {
			throw  new ObjectNotFound("Nenhum cadastro encontrado");
		}
		return lista;
	}

	@Override
	public List<PilotRace> findByRace(Race race) {
		List<PilotRace> lista = repository.findByRace(race);
		if (lista.isEmpty()) {
			throw  new ObjectNotFound("Nenhum cadastro encontrado");
		}
		return lista;
	}

}
