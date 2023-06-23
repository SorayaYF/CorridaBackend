package br.com.trier.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.spring.domain.Country;
import br.com.trier.spring.domain.Speedway;

@Repository
public interface SpeedwayRepository extends JpaRepository<Speedway, Integer> {

	List<Speedway> findByNameStartsWithIgnoreCase(String name);

	List<Speedway> findBySizeBetween(Integer sizeIn, Integer sizeFin);

	List<Speedway> findByCountryOrderBySizeDesc(Country country);

}
