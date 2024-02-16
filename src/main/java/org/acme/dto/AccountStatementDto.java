package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AccountStatementDto(
	@JsonProperty("saldo")
	BalanceDto balance,
	@JsonProperty("ultimas_transacoes")
	List<AccountTransactionDto> lastTransactions
) {
}
