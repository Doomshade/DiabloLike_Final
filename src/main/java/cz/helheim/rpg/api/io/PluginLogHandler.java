package cz.helheim.rpg.api.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Handler to write log messages to a custom file in {@code logs/} folder.
 *
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class PluginLogHandler extends Handler {
	public static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
	private final Writer out;

	public PluginLogHandler(final IOManager io) throws IOException {
		this.out = new BufferedWriter(new FileWriter(io.getLogFile()));
	}

	@Override
	public void publish(final LogRecord record) {
		try {
			final Throwable thrown = record.getThrown();
			out.append(String.format("[%s] %s%s",
			                         LocalDateTime.now()
			                                      .format(DATE_TIME_PATTERN),
			                         record.getMessage(),
			                         thrown == null ? "" : " THROWN: " + thrown.getLocalizedMessage()));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void flush() {
		try {
			out.flush();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void close() throws SecurityException {
		try {
			out.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
