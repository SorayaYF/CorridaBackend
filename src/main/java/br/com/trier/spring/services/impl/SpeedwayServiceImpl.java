package br.com.trier.spring.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.spring.domain.Country;
import br.com.trier.spring.domain.Speedway;
import br.com.trier.spring.repositories.SpeedwayRepository;
import br.com.trier.spring.services.SpeedwayService;
import br.com.trier.spring.services.exceptions.IntegrityViolation;
import br.com.trier.spring.services.exceptions.ObjectNotFound;

@Service
public class SpeedwayServiceImpl implements SpeedwayService {

	@Autowired
	private SpeedwayRepository repository;

	private void validateSpeedway(Speedway speedway) {
		if (speedway.getSize() == null || speedway.getSize() <= 0) {
			throw new IntegrityViolation("Tamanho da pista inválido");
		}
	}

	@Override
	public Speedway findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A pista %s não existe".formatted(id)));
	}

	@Override
	public Speedway insert(Speedway speedway) {
		validateSpeedway(speedway);
		return repository.save(speedway);
	}

	@Override
	public List<Speedway> listAll() {
		List<Speedway> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada");
		}
		return lista;
	}

	@Override
	public Speedway update(Speedway speedway) {
		findById(speedway.getId());
		validateSpeedway(speedway);
		return repository.save(speedway);
	}

	@Override
	public void delete(Integer id) {
		repository.delete(findById(id));

	}

	@Override
	public List<Speedway> findByNameStartsWithIgnoreCase(String name) {
		List<Speedway> lista = repository.findByNameStartsWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com nome: %s".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin) {
		List<Speedway> lista = repository.findBySizeBetween(sizeIn, sizeFin);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com tamanho entre %s e %s".formatted(sizeIn, sizeFin));
		}
		return lista;
	}

	@Override
	public List<Speedway> findByCountryOrderBySizeDesc(Country country) {
		List<Speedway> lista = repository.findByCountryOrderBySizeDesc(country);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com o país: %s".formatted(country.getName()));
		}
		return lista;
	}

}
