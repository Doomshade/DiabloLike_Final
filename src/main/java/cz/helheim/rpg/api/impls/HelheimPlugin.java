package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.IHelheimPlugin;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.event.ReloadEvent;
import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.api.io.PluginLogHandler;
import cz.helheim.rpg.api.io.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HelheimPlugin extends JavaPlugin implements IHelheimPlugin, Listener {

	private IOManager io;
	private ICommandHandler commandHandler;

	private Settings settings;

	public HelheimPlugin() {
	}

	@Override
	public void onDisable() {
		save();
		closeFiles();
		for (Handler h : getLogger().getHandlers()) {
			h.close();
		}
	}

	@Override
	public void onEnable() {
		register();
		load();
		save();
	}

	private void closeFiles() {
		io.closeFiles();
	}

	protected abstract ICommandHandler getCommandHandler();

	private void register() {
		ConfigurationSerialization.registerClass(AbstractSubCommand.class);
		this.io = new IOManager();
		this.commandHandler = getCommandHandler();
		this.commandHandler.register();
		this.commandHandler.registerSubCommands();
		registerListener(this);
	}

	protected void load() {
		loadIO();
		loadLogger();
		loadConfig();
		loadCommands();
	}

	private void loadConfig() {
		io.loadConfig(this);
	}

	private void loadCommands() {
		this.commandHandler.loadCommands(io.getCommandsFileConfiguration());
	}

	private void loadIO() {
		try {
			io.load(this);
		} catch (IOException | InvalidConfigurationException e) {
			getLogger().log(Level.SEVERE, "An IO exception occurred when loading the plugin, stopping...", e);
			Bukkit.getPluginManager()
			      .disablePlugin(this);
		}
	}

	private void loadLogger() {
		final Logger logger = getLogger();
		final String logFilePath = io.getLogFile()
		                             .getAbsolutePath();
		try {
			logger.addHandler(new PluginLogHandler(logFilePath));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to initialize the log handler for " + logFilePath, e);
		}
	}

	private void save() {
		saveCommands();
	}

	private void saveCommands() {
		try {
			final FileConfiguration commandsFileConfiguration = this.io.getCommandsFileConfiguration();
			// TODO
			this.commandHandler.saveCommands(commandsFileConfiguration);
			// commandsFileConfiguration.addDefaults(this.serializeManager.serialize(this.commandHandler));
			commandsFileConfiguration.save(this.io.getCommandsFile());
		} catch (IOException e) {
			getLogger().log(Level.WARNING, "Failed to save commands!", e);
		}
	}

	protected void registerListener(Listener listener) {
		Bukkit.getPluginManager()
		      .registerEvents(listener, this);
	}

	@Override
	public IOManager getIOManager() {
		ensureEnabled();
		return io;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Settings> T getSettings() {
		return (T) settings;
	}

	@Override
	public void setSettings(final Settings settings) {
		this.settings = settings;
	}

	@Override
	public void reload() {
		load();
		Bukkit.getPluginManager()
		      .callEvent(new ReloadEvent(this));
	}


	private void ensureEnabled() {
		if (!isEnabled()) {
			throw new IllegalStateException("Plugin is not yet enabled!");
		}
	}
}
