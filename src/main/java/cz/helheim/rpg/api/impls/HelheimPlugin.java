package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.IHelheimPlugin;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.event.ReloadEvent;
import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.api.io.PluginLogHandler;
import cz.helheim.rpg.command.DiabloLikeCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HelheimPlugin extends JavaPlugin implements IHelheimPlugin {

	private IOManager io;
	private ICommandHandler commandHandler;

	@Override
	public void onDisable() {
		save();
	}

	@Override
	public void onEnable() {
		register();
		load();
	}

	private void register() {
		this.io = new IOManager();
		this.commandHandler = new DiabloLikeCommandHandler(this);
		this.commandHandler.register();
		this.commandHandler.registerSubCommands();

	}

	private void load() {
		loadIO();
		loadLogger();
		loadConfig();
		loadCommands();
	}

	private void loadConfig() {
		io.loadConfig(this);
	}

	private void loadCommands() {
		// TODO
	}

	private void loadIO() {
		try {
			io.load(this);
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().log(Level.SEVERE, "An IO exception occurred, stopping plugin...", e);
			Bukkit.getPluginManager()
			      .disablePlugin(this);
		}
	}

	private void loadLogger() {
		final Logger logger = getLogger();
		try {
			logger.addHandler(new PluginLogHandler(io));
		} catch (IOException e) {
			logger.log(Level.SEVERE,
			           "Failed to initialize the log handler for " + io.getLogFile()
			                                                           .getAbsolutePath(),
			           e);
		}
	}

	private void save() {
		saveCommands();
	}

	private void saveCommands() {
		try {
			final FileConfiguration commandsFileConfiguration = this.io.getCommandsFileConfiguration();
			// TODO
			// commandsFileConfiguration.addDefaults(this.serializeManager.serialize(this.commandHandler));
			commandsFileConfiguration.save(this.io.getCommandsFile());
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Failed to save commands!", e);
		}
	}

	@Override
	public IOManager getIOManager() {
		ensureEnabled();
		return io;
	}

	@Override
	public void reload() {
		save();
		load();
		Bukkit.getPluginManager()
		      .callEvent(new ReloadEvent());
	}


	private void ensureEnabled() {
		if (!isEnabled()) {
			throw new IllegalStateException("Plugin is not yet enabled!");
		}
	}
}
