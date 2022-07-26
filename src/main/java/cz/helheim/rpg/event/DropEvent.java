package cz.helheim.rpg.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 18.07.2022
 */
public class DropEvent extends Event implements Cancellable {
	private static final HandlerList handlerList = new HandlerList();
	private final Entity mob;
	private final LivingEntity killer;
	private final Collection<ItemStack> loot = new ArrayList<>();
	private boolean cancelled = false;

	public DropEvent(final Entity mob, final LivingEntity killer, final Collection<ItemStack> loot) {
		this.mob = mob;
		this.killer = killer;
		this.loot.addAll(loot);
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public Collection<ItemStack> getLoot() {
		return Collections.unmodifiableCollection(loot);
	}

	public LivingEntity getKiller() {
		return killer;
	}

	public Entity getMob() {
		return mob;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

}
