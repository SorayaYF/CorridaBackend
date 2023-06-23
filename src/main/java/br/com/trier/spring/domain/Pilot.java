package br.com.trier.spring.domain;

import br.com.trier.spring.domain.dto.PilotDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "piloto")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pilot {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_piloto")
	private Integer id;

	@Column(name = "nome_piloto")
	private String name;

	@ManyToOne
	private Country country;

	@ManyToOne
	private Team team;

	public Pilot(PilotDTO dto, Country country, Team team) {
		this(dto.getId(), dto.getName(), country, team);
	}

	public PilotDTO toDTO() {
		return new PilotDTO(id, name, country.getId(), country.getName(), team.getId(), team.getName());
	}

}
