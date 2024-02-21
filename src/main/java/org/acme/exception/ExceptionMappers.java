package org.acme.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class ExceptionMappers {

	private static final int UNPROCESSABLE_ENTITY = 422;

	@ServerExceptionMapper
	public Response handleNotFound(EntityNotFoundException e) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@ServerExceptionMapper
	public Response handleConstraintViolation(ConstraintViolationException e) {
		return Response.status(UNPROCESSABLE_ENTITY).build();
	}

	@ServerExceptionMapper
	public Response handleBalanceException(BalanceException e) {
		return Response.status(UNPROCESSABLE_ENTITY).build();
	}

	@ServerExceptionMapper
	public Response handleMismatchedInput(MismatchedInputException e) {
		return Response.status(UNPROCESSABLE_ENTITY).build();
	}

}
