package cz.helheim.rpg.listener;

import cz.helheim.rpg.DiabloLike;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cz.helheim.rpg.listener.MobListener.dropItems;

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
		final Entity mob = e.getEntity();
		final LivingEntity killer = e.getKiller();

		dropItems(plugin, mob, killer);
	}

}
