package cz.helheim.rpg.api.io;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class IOManager {

	private File logFile;
	private File commandsFile;
	private FileConfiguration commandsFileConfiguration;

	private boolean initialized = false;
	private boolean firstEverInitialization = false;

	public boolean isFirstEverInitialization() {
		return firstEverInitialization;
	}

	public void load(HelheimPlugin plugin) throws IOException, InvalidConfigurationException {
		initDataFolder(plugin);
		initLogFile(plugin.getDataFolder());
		initCommandsFileConfiguration(plugin.getDataFolder());
	}

	private void initDataFolder(final HelheimPlugin plugin) throws IOException {
		final File dataFolder = plugin.getDataFolder();
		if (!dataFolder.isDirectory()) {
			if (!dataFolder.mkdirs()) {
				throw new IOException("Failed to create the data folder for plugin " + plugin.getName());
			}
			firstEverInitialization = true;
		}
	}

	public void loadConfig(final HelheimPlugin plugin) {
		plugin.saveDefaultConfig();
	}

	private void initLogFile(final File dataFolder) throws IOException {
		File logFolder = folder(dataFolder, "logs");
		this.logFile = file(logFolder,
		                    String.format("log-%s.log",
		                                  LocalDateTime.now()
		                                               .format(PluginLogHandler.DATE_TIME_PATTERN)));
	}

	private void initCommandsFileConfiguration(File dataFolder) throws IOException, InvalidConfigurationException {
		this.commandsFile = file(dataFolder, "commands.yml");
		this.commandsFileConfiguration = new YamlConfiguration();
		this.commandsFileConfiguration.load(commandsFile);
		this.commandsFileConfiguration.options()
		                              .copyDefaults(true)
		                              .header("You may edit any values (not keys!). If there's an error an appropriate message will be " +
		                                      "shown in the" + " exception.")
		                              .copyHeader(true);
	}

	private File folder(File parent, String name) throws IOException {
		File folder = new File(parent, name);
		if (!folder.isDirectory() && !folder.mkdirs()) {
			throw new IOException("Failed to create " + folder.getAbsolutePath());
		}
		return folder;
	}

	private File file(File folder, String name) throws IOException {
		final File file = new File(folder, name);
		if (!file.exists() && !file.createNewFile()) {
			throw new IOException("Failed to create " + file.getAbsolutePath());
		}
		return file;
	}

	private File folder(String name) throws IOException {
		final File folder = new File(name);
		if (!folder.isDirectory() && !folder.mkdirs()) {
			throw new IOException("Failed to create " + folder.getAbsolutePath());
		}
		return folder;
	}

	public File getLogFile() {
		ensureInitialized();
		return logFile;
	}

	private void ensureInitialized() {
		if (!initialized) {
			throw new IllegalStateException("IO manager is not yet initialized");
		}
	}

	public FileConfiguration getCommandsFileConfiguration() {
		ensureInitialized();
		return commandsFileConfiguration;
	}

	public File getCommandsFile() {
		return commandsFile;
	}
}
