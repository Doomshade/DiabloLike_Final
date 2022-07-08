package cz.helheim.rpg.api.event;

import cz.helheim.rpg.api.IHelheimPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class ReloadEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();
	private final IHelheimPlugin plugin;

	public ReloadEvent(final IHelheimPlugin plugin) {
		this.plugin = plugin;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public IHelheimPlugin getPlugin() {
		return plugin;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
