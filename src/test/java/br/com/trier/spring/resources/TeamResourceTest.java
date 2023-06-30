package br.com.trier.spring.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.spring.Application;
import br.com.trier.spring.config.jwt.LoginDTO;
import br.com.trier.spring.domain.Team;
import br.com.trier.spring.domain.dto.UserDTO;

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private HttpHeaders getHeaders(String email, String password) {
		LoginDTO loginDTO = new LoginDTO(email, password);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
		ResponseEntity<String> responseEntity = rest.exchange("/auth/token", HttpMethod.POST, requestEntity,
				String.class);
		String token = responseEntity.getBody();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		return headers;
	}

	private ResponseEntity<Team> getTeam(String url) {
		return rest.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders("email1", "senha1")), Team.class);
	}

	private ResponseEntity<List<Team>> getTeams(String url) {
		return rest.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders("email1", "senha1")),
				new ParameterizedTypeReference<List<Team>>() {
				});
	}

	@Test
	@DisplayName("Cadastrar equipe")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	public void testInsertTeam() {
		Team team = new Team(null, "Equipe 1");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Team> requestEntity = new HttpEntity<>(team, headers);

		ResponseEntity<Team> responseEntity = rest.exchange("/teams", HttpMethod.POST, requestEntity, Team.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Team createdTeam = responseEntity.getBody();
		assertEquals("Equipe 1", createdTeam.getName());
	}

	@Test
	@DisplayName("Buscar equipe por ID")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	public void testFindTeamById() {
		ResponseEntity<Team> response = getTeam("/users/1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Team team = response.getBody();
		assertEquals("Ferrari", team.getName());
	}

	@Test
	@DisplayName("Listar todas as equipes")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	public void testListAllTeams() {
		ResponseEntity<List<Team>> response = rest.exchange("/teams", HttpMethod.GET,
				new HttpEntity<>(getHeaders("email1", "senha1")), new ParameterizedTypeReference<List<Team>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Team> teams = response.getBody();
		assertEquals(2, teams.size());
	}

	@Test
	@DisplayName("Atualizar equipe")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	public void testUpdateTeam() {
		Team updatedTeam = new Team(1, "Equipe Atualizada");
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Team> requestEntity = new HttpEntity<>(updatedTeam, headers);

		ResponseEntity<Team> responseEntity = rest.exchange("/teams/1", HttpMethod.PUT, requestEntity, Team.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Team team = responseEntity.getBody();
		assertEquals("Equipe Atualizada", team.getName());
	}

	@Test
	@DisplayName("Excluir equipe")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	public void testDeleteTeam() {
		HttpHeaders headers = getHeaders("email1", "senha1");
		HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<Void> responseEntity = rest.exchange("/teams/2", HttpMethod.DELETE, requestEntity, Void.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	@DisplayName("Buscar equipe por nome")
	@Sql({ "classpath:/resources/sqls/limpa_tabelas.sql" })
	@Sql({ "classpath:/resources/sqls/equipe.sql" })
	public void testFindTeamByNameStartsWithIgnoreCase() {
		ResponseEntity<List<Team>> response = rest.exchange("/teams/name/equipe", HttpMethod.GET,
				new HttpEntity<>(getHeaders("email1", "senha1")), new ParameterizedTypeReference<List<Team>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Team> teams = response.getBody();
		assertEquals(2, teams.size());
	}
}
