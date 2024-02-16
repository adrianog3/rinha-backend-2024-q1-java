package org.acme.exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.LocalDateTime;
import java.util.Map;

@ApplicationScoped
public class ExceptionMappers {

	@ServerExceptionMapper(value = {NotFoundException.class, EntityNotFoundException.class})
	public Response handleNotFound(Exception e) {
		Map<String, Object> message = Map.of(
			"message", e.getMessage(),
			"timestamp", LocalDateTime.now()
		);

		return Response.status(Response.Status.NOT_FOUND).entity(message).build();
	}

	@ServerExceptionMapper(value = {BalanceException.class})
	public Response handleBalanceException(Exception e) {
		Map<String, Object> message = Map.of(
			"message", e.getMessage(),
			"timestamp", LocalDateTime.now()
		);

		return Response.status(422).entity(message).build();
	}

}
