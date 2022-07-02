package cz.helheim.rpg.listener;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public class MythicMobListener extends MobListener {

	@Override
	public void onMobDeath(final EntityDeathEvent e) {
		// don't do anything
	}

	@EventHandler
	public void onMythicMobDeath(final MythicMobDeathEvent e) {
		if (!(e.getKiller() instanceof Player)) {
			return;
		}
		// first check if the mob is present in some custom dungeon drop
	}
}
