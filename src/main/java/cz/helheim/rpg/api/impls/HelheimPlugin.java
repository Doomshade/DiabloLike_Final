package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.IHelheimPlugin;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.api.io.PluginLogHandler;
import cz.helheim.rpg.api.serialize.SerializeManager;
import cz.helheim.rpg.command.DiabloLikeCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HelheimPlugin extends JavaPlugin implements IHelheimPlugin {

	private IOManager io;
	private SerializeManager serializeManager;

	private ICommandHandler commandHandler;
	private boolean initialized = false;

	@Override
	public void onDisable() {
		save();
		initialized = false;
	}

	@Override
	public void onEnable() {
		register();
		load();
		initialized = true;
	}

	private void save() {
		saveCommands();
	}

	public void reload() {
		save();
		load();
	}

	private void saveCommands() {
		this.commandHandler.serialize(io.getCommandsFileConfiguration());
	}

	private void register() {
		io = new IOManager();
		this.commandHandler = new DiabloLikeCommandHandler(this);
		this.commandHandler.register();
		this.commandHandler.registerSubCommands();

		this.serializeManager = new SerializeManager(this);
	}

	public void load() {
		loadIO();
		loadLogger();
		loadCommands();
	}

	private void loadCommands() {
		try {
			this.serializeManager.deserialize(this.commandHandler, io.getCommandsFileConfiguration());
		} catch (SerializationException e) {
			getLogger().log(Level.SEVERE, "Failed to load commands!", e);
		}
	}

	private void loadIO() {
		try {
			io.init(this);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void loadLogger() {
		final Logger logger = getLogger();
		try {
			logger.addHandler(new PluginLogHandler(io));
		} catch (IOException e) {
			logger.log(Level.SEVERE,
			           "Failed to initialize the log handler to " + io.getLogFile()
			                                                          .getAbsolutePath(),
			           e);
		}
	}

	public IOManager getIOManager() {
		ensureInitialized();
		return io;
	}

	private void ensureInitialized() {
		if (!initialized) {
			throw new IllegalStateException("Plugin is not yet initialized!");
		}
	}
}
