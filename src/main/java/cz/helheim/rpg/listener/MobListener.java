package cz.helheim.rpg.listener;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.event.DropEvent;
import cz.helheim.rpg.item.Drop;
import cz.helheim.rpg.item.DropManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public class MobListener implements Listener {

	private final DiabloLike plugin;

	public MobListener(final DiabloLike plugin) {
		this.plugin = plugin;
	}

	static void dropItems(final DiabloLike plugin, final Entity mob, final LivingEntity killer) {
		// get the available drop for the mob
		// this will check whether it's a Lv. X mob, a dungeon mob etc.
		final Drop availableDrop = plugin.getAvailableDrop(mob);

		// the mob has no drop available
		if (availableDrop.isEmpty()) {
			return;
		}

		// there's a drop, get a random loot. if the loot is empty (meaning it didn't drop), don't do anything
		final DropManager dropManager = plugin.getItemDropManager();
		final Collection<ItemStack> loot = dropManager.getRandomLoot(availableDrop);
		if (loot.isEmpty()) {
			return;
		}
		final DropEvent dropEvent = new DropEvent(mob, killer, loot);
		Bukkit.getPluginManager()
		      .callEvent(dropEvent);

		// the drop was cancelled due to some reason
		if (dropEvent.isCancelled()) {
			return;
		}

		// drop the items at the mobs location
		final Location location = mob.getLocation();
		for (final ItemStack item : loot) {
			location.getWorld()
			        .dropItemNaturally(location, item);
		}
	}


	@EventHandler
	public void onMobDeath(final EntityDeathEvent e) {
		final LivingEntity killedEntity = e.getEntity();

		// ignore entities that:
		// got killed by another entity or fall damage
		// were not a MythicMob entity (very likely)
		// that was a player
		if (killedEntity.getKiller() == null || killedEntity.getCustomName() == null || killedEntity instanceof Player) {
			return;
		}

		dropItems(plugin, killedEntity, killedEntity.getKiller());
	}

}
