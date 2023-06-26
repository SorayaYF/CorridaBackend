package br.com.trier.spring.domain;

import br.com.trier.spring.domain.dto.PilotRaceDTO;
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

@Entity(name = "piloto_corrida")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PilotRace {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_piloto_corrida")
	private Integer id;

	@Column(name = "colocacao")
	private Integer placing;

	@ManyToOne
	private Pilot pilot;

	@ManyToOne
	private Race race;

	public PilotRace(PilotRaceDTO dto, Race race, Pilot pilot) {
		this(dto.getId(), dto.getPlacing(), pilot, race);
	}

	public PilotRaceDTO toDTO() {
		return new PilotRaceDTO(id, placing, race.getId(), pilot.getId(), pilot.getName());
	}
}
