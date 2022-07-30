package cz.helheim.rpg.api.io;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Handler to write log messages to a custom file in {@code logs/} folder.
 *
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class PluginLogHandler extends FileHandler {
	public static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm-ss");
	private static final Function<Level, String> LOG_COLOUR = level -> {
		final int val = level.intValue();
		if (val >= Level.SEVERE.intValue()) {
			return "\033[0;31m";
		}
		if (val >= Level.WARNING.intValue()) {
			return "\033[0;31m";
		}
		if (val <= Level.CONFIG.intValue()) {
			return "\033[0;34m";
		}
		return "";
	};

	{
		setFormatter(new SimpleFormatter());
	}

	public PluginLogHandler() throws IOException, SecurityException {
	}

	public PluginLogHandler(final String pattern) throws IOException, SecurityException {
		super(pattern);
	}

	public PluginLogHandler(final String pattern, final boolean append) throws IOException, SecurityException {
		super(pattern, append);
	}

	public PluginLogHandler(final String pattern, final int limit, final int count) throws IOException, SecurityException {
		super(pattern, limit, count);
	}

	public PluginLogHandler(final String pattern, final int limit, final int count, final boolean append)
			throws IOException, SecurityException {
		super(pattern, limit, count, append);
	}

	@Override
	public void publish(final LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}
		final Throwable thrown = record.getThrown();
		final String msg = String.format("%s%s%s%s",
		                                 LOG_COLOUR.apply(record.getLevel()),
		                                 record.getMessage(),
		                                 thrown == null ? "" : " THROWN: " + thrown.getLocalizedMessage(),
		                                 "\033[0m");
		record.setMessage(msg);
		super.publish(record);
	}
}
