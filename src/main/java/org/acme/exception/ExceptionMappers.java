package org.acme.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExceptionMappers {

	private static final int UNPROCESSABLE_ENTITY = 422;

	@ServerExceptionMapper(value = {NotFoundException.class, EntityNotFoundException.class})
	public Response handleNotFound(Exception e) {
		return buildResponse(e.getMessage(), Response.Status.NOT_FOUND);
	}

	@ServerExceptionMapper
	public Response handleBalanceException(BalanceException e) {
		return buildResponse(e.getMessage(), UNPROCESSABLE_ENTITY);
	}

	@ServerExceptionMapper
	public Response handleMismatchedInputException(MismatchedInputException e) {
		String fieldPath = e.getPath().stream()
			.map(JsonMappingException.Reference::getFieldName)
			.collect(Collectors.joining("."));

		String message = String.format(
			"Não foi possível converter o valor do campo '%s' para %s",
			fieldPath, e.getTargetType().getSimpleName()
		);

		return buildResponse(message, UNPROCESSABLE_ENTITY);
	}

	public Response buildResponse(String message, Response.Status status) {
		return buildResponse(message, status.getStatusCode());
	}

	public Response buildResponse(String message, int status) {
		Map<String, Object> payload = Map.of(
			"message", message,
			"timestamp", LocalDateTime.now()
		);

		return Response.status(status).entity(payload).build();
	}

}
