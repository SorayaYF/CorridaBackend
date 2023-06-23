package br.com.trier.spring.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PilotRaceDTO {

	private Integer id;
	private Integer placing;
	private Integer runId;
	private Integer pilotId;
	private String pilotName;

}
