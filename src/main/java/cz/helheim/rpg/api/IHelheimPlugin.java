package cz.helheim.rpg.api;

import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.api.io.Settings;
import org.bukkit.plugin.Plugin;

public interface IHelheimPlugin extends Plugin {
	IOManager getIOManager();

	<T extends Settings> T getSettings();

	void setSettings(Settings settings);

	void reload();
}
