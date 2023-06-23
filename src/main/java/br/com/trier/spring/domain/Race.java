package br.com.trier.spring.domain;

import java.time.ZonedDateTime;

import br.com.trier.spring.domain.dto.RaceDTO;
import br.com.trier.spring.utils.DateUtils;
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

@Entity(name = "corrida")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Race {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_corrida")
	private Integer id;

	@Column(name = "data_corrida")
	private ZonedDateTime date;

	@ManyToOne
	private Speedway speedway;

	@ManyToOne
	private Championship championship;

	public Race(RaceDTO dto, Championship championship, Speedway speedway) {
		this(dto.getId(), DateUtils.strToZonedDateTime(dto.getDate()), speedway, championship);
	}

	public RaceDTO toDTO() {
		return new RaceDTO(id, DateUtils.zonedDateTimeToStr(date), speedway.getId(), speedway.getName(),
				championship.getId(), championship.getDescription());
	}

}
