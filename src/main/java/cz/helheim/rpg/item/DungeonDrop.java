package cz.helheim.rpg.item;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.item.impl.DefaultDungeonDrop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public interface DungeonDrop {
	static DungeonDrop newDungeonSpecificDrop(final DiabloLike plugin, ConfigurationSection repositoriesSection) {
		return new DefaultDungeonDrop(plugin, repositoriesSection);
	}

	List<Drop> getAvailableDrops();
}
