package br.com.trier.spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RaceDTO {

	private Integer id;
	private String date;
	private Integer speedwayId;
	private String speedwayName;
	private Integer championshipId;
	private String championshipName;

}
