package cz.helheim.rpg.util;

import cz.helheim.rpg.api.IHelheimPlugin;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 18.07.2022
 */
public final class ExceptionUtils {
	private ExceptionUtils() {
	}

	public static String internalError(IHelheimPlugin plugin, String errorMessage) {
		return errorMessage.concat(
				" This is a plugin error, please see the newest log file in the logs folder, and report the error to the plugin " +
				"authors: " + plugin.getDescription()
				                    .getAuthors());
	}
}
