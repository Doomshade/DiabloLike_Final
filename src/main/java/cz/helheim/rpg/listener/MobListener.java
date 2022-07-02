package cz.helheim.rpg.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public class MobListener implements Listener {

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
	}

}
