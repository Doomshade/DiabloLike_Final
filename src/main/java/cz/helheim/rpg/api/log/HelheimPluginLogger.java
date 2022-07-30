package cz.helheim.rpg.api.log;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 29.07.2022
 */
public class HelheimPluginLogger extends PluginLogger {
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

	/**
	 * Creates a new PluginLogger that extracts the name from a plugin.
	 *
	 * @param context A reference to the plugin
	 */
	public HelheimPluginLogger(final Plugin context) {
		super(context);
	}

	/**
	 * @param logRecord the LogRecord to be published
	 */
	@Override
	public void log(final LogRecord logRecord) {
		logRecord.setMessage(LOG_COLOUR.apply(logRecord.getLevel()) + logRecord.getMessage());
		super.log(logRecord);
	}
}
