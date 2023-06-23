package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

	Country findByName(String name);

	List<Country> findByNameStartsWithIgnoreCase(String name);

}
