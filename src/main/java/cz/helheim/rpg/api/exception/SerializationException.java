package cz.helheim.rpg.api.exception;

import java.io.IOException;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public class SerializationException extends IOException {
	public SerializationException() {
	}

	public SerializationException(final String message) {
		super(message);
	}

	public SerializationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SerializationException(final Throwable cause) {
		super(cause);
	}
}
