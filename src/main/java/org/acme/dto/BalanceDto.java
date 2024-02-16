package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record BalanceDto(
	@JsonProperty("total")
	Integer balance,
	@JsonProperty("data_extrato")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
	LocalDateTime generatedAt,
	@JsonProperty("limite")
	Integer limit
) {
}
