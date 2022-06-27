package cz.helheim.rpg.api;

import cz.helheim.rpg.api.io.IOManager;
import org.bukkit.plugin.Plugin;

public interface IHelheimPlugin extends Plugin {
	IOManager getIOManager();

	void reload();
}
