package es.alordiez.wumpus.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomHttpException extends ResponseStatusException {
	
	private static final long serialVersionUID = -397346194878137254L;
	
	public CustomHttpException(HttpStatus status) {
		super(status);
	}

	public CustomHttpException(HttpStatus status, String reason) {
		super(status, reason);
	}

	public CustomHttpException(HttpStatus status, String reason, Throwable cause) {
		super(status, reason, cause);
	}

}
