package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewTransactionDto(
	@NotNull
	@Min(value = 1, message = "O valor da transação deve ser um número inteiro maior que zero")
	@JsonProperty("valor")
	Integer amount,
	@NotNull
	@Pattern(regexp = "[cd]", message = "O tipo da transação deve conter apenas 'c' para crédito ou 'd' para débito")
	@JsonProperty("tipo")
	String transactionType,
	@NotNull
	@Size(min = 1, max = 10, message = "A descrição da transação deve conter de 1 à 10 caracteres")
	@JsonProperty("descricao")
	String description
) {
}
