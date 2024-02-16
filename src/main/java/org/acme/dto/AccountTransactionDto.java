package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AccountTransactionDto(
	@JsonProperty("valor")
	Integer amount,
	@JsonProperty("tipo")
	String transactionType,
	@JsonProperty("descricao")
	String description,
	@JsonProperty("realizada_em")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
	LocalDateTime ocurredAt
) {
}
