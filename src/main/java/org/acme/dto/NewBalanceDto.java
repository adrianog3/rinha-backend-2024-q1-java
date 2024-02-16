package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewBalanceDto(
	@JsonProperty("limite")
	Integer limit,
	@JsonProperty("saldo")
	Integer balance
) {
}
