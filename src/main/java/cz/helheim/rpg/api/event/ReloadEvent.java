package cz.helheim.rpg.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class ReloadEvent extends Event {

	private static final HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
