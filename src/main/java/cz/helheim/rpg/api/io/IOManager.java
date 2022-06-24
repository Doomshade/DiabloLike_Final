package cz.helheim.rpg.api.io;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public class IOManager {

	private File logFile;
	private FileConfiguration commandsFileConfiguration;

	private boolean initialized = false;

	public void init(HelheimPlugin plugin) throws IOException {
		File logFolder = folder(plugin.getDataFolder(), "logs");
		this.logFile = file(logFolder,
		                    String.format("log-%s.log",
		                                  LocalDateTime.now()
		                                               .format(PluginLogHandler.DATE_TIME_PATTERN)));

		this.commandsFileConfiguration = YamlConfiguration.loadConfiguration(file(plugin.getDataFolder(), "commands.yml"));

		this.initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	private File folder(File parent, String name) throws IOException {
		File folder = new File(parent, name);
		if (!folder.isDirectory() && !folder.mkdirs()) {
			throw new IOException("Failed to create " + folder.getAbsolutePath());
		}
		return folder;
	}

	private File folder(String name) throws IOException {
		final File folder = new File(name);
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

	public File getLogFile() {
		ensureInitialized();
		return logFile;
	}

	public FileConfiguration getCommandsFileConfiguration() {
		ensureInitialized();
		return commandsFileConfiguration;
	}

	private void ensureInitialized() {
		if (!initialized) {
			throw new IllegalStateException("IO manager is not yet initialized");
		}
	}
}
