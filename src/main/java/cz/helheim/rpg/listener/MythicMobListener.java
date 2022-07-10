package cz.helheim.rpg.listener;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.item.Drop;
import cz.helheim.rpg.item.DropManager;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public class MythicMobListener implements Listener {

	private final DiabloLike plugin;

	public MythicMobListener(DiabloLike plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onMythicMobDeath(final MythicMobDeathEvent e) {
		if (!(e.getKiller() instanceof Player)) {
			return;
		}
		final Drop availableDrop = plugin.getAvailableDrop(e.getEntity());
		final DropManager dropManager = plugin.getItemDropManager();
		final Collection<ItemStack> randomLoot = dropManager.getRandomLoot(availableDrop);
		// first check if the mob is present in some custom dungeon drop
	}
}
