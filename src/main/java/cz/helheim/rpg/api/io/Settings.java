package cz.helheim.rpg.api.io;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public abstract class Settings {
	protected final FileConfiguration config;
	protected final Logger logger;

	public Settings(HelheimPlugin plugin) {
		Validate.notNull(plugin);
		this.config = plugin.getConfig();
		plugin.saveDefaultConfig();
		this.logger = plugin.getLogger();
	}

	public abstract void reload();
}
