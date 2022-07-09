package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.item.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public class DefaultDungeonDrop implements DungeonDrop {

	private final List<Drop> drops = new ArrayList<>();

	public DefaultDungeonDrop(final DiabloLike plugin, ConfigurationSection repositoriesSection) {
		for (final String repoId : repositoriesSection.getKeys(false)) {
			final ItemRepository repo = plugin.getRepository(repoId);
			if (repo == null) {
				plugin.getLogger()
				      .log(Level.WARNING, String.format("Item repository '%s' not found!", repoId));
				continue;
			}
			addDrop(plugin, repo, repositoriesSection.getConfigurationSection(repoId));
		}
	}

	private void addDrop(DiabloLike plugin, ItemRepository repo, ConfigurationSection dungeonSection) {
		final ItemLoader loader = ItemLoader.newInstance(plugin, dungeonSection);
		for (final BaseItem item : repo) {
			if (!(item instanceof DiabloItem)) {
				continue;
			}
			final DiabloItem diabloItem = (DiabloItem) item;
			drops.add(Drop.newDrop(diabloItem, loader.getAmount(), loader.getDropChance(), loader.getRarityChances()));
		}
	}

	@Override
	public List<Drop> getAvailableDrops() {
		return drops;
	}
}
