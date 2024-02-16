package org.acme.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.acme.dto.AccountStatementDto;
import org.acme.dto.NewBalanceDto;
import org.acme.dto.NewTransactionDto;
import org.acme.service.AccountService;

@Path("/clientes")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@POST
	@Path("{clientId}/transacoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response performTransaction(@PathParam("clientId") int clientId, @Valid NewTransactionDto newTransactionDto) {
		NewBalanceDto newBalanceDto = accountService.performTransaction(clientId, newTransactionDto);
		return Response.ok(newBalanceDto).build();
	}

	@GET
	@Path("{clientId}/extrato")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatement(@PathParam("clientId") int clientId) {
		AccountStatementDto accountStatementDto = accountService.getStatement(clientId);
		return Response.ok(accountStatementDto).build();
	}

}
