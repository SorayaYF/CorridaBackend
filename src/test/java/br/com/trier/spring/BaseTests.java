package br.com.trier.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import br.com.trier.spring.services.ChampionshipService;
import br.com.trier.spring.services.CountryService;
import br.com.trier.spring.services.PilotRaceService;
import br.com.trier.spring.services.PilotService;
import br.com.trier.spring.services.RaceService;
import br.com.trier.spring.services.SpeedwayService;
import br.com.trier.spring.services.TeamService;
import br.com.trier.spring.services.UserService;
import br.com.trier.spring.services.impl.ChampionshipServiceImpl;
import br.com.trier.spring.services.impl.CountryServiceImpl;
import br.com.trier.spring.services.impl.PilotRaceServiceImpl;
import br.com.trier.spring.services.impl.PilotServiceImpl;
import br.com.trier.spring.services.impl.RaceServiceImpl;
import br.com.trier.spring.services.impl.SpeedwayServiceImpl;
import br.com.trier.spring.services.impl.TeamServiceImpl;
import br.com.trier.spring.services.impl.UserServiceImpl;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class BaseTests {

	@Bean
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	public CountryService countryService() {
		return new CountryServiceImpl();
	}

	@Bean
	public TeamService teamService() {
		return new TeamServiceImpl();
	}

	@Bean
	public ChampionshipService championshipService() {
		return new ChampionshipServiceImpl();
	}

	@Bean
	public SpeedwayService speedwayService() {
		return new SpeedwayServiceImpl();
	}

	@Bean
	public RaceService runService() {
		return new RaceServiceImpl();
	}

	@Bean
	public PilotService pilotService() {
		return new PilotServiceImpl();
	}

	@Bean
	public PilotRaceService pilotRunService() {
		return new PilotRaceServiceImpl();
	}

}