package org.acme.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.acme.dto.NewTransactionDto;
import org.acme.dto.NewBalanceDto;
import org.acme.service.AccountTransactionService;

@ApplicationScoped
@Path("/clientes")
@RequiredArgsConstructor
public class AccountTransactionController {

	private final AccountTransactionService accountTransactionService;

	@POST
	@Path("{clientId}/transacoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response performTransaction(@PathParam("clientId") int clientId, @Valid NewTransactionDto newTransactionDto) {
		NewBalanceDto newBalanceDto = accountTransactionService.performTransaction(clientId, newTransactionDto);
		return Response.ok(newBalanceDto).build();
	}

}
