package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.BaseItem;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.DungeonDrop;
import cz.helheim.rpg.item.ItemRepository;
import cz.helheim.rpg.util.Pair;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public class DefaultDungeonDrop implements DungeonDrop {

	private final Collection<Pair<DiabloItem, Range>> drops = new LinkedHashSet<>();

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
		if (!isSet(plugin, repo, dungeonSection, "amount")) {
			return;
		}
		if (!isSet(plugin, repo, dungeonSection, "chance")) {
			return;
		}

		final Range amount = new Range(dungeonSection.getString("amount"));
		final double chance = dungeonSection.getDouble("chance");
		final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>();
		final DiabloLikeSettings settings = plugin.getSettings();
		for (final DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			rarityChances.put(tier, dungeonSection.getDouble(tier.toString(), settings.getRarityChance(tier)));
		}
		for (final BaseItem item : repo) {
			if (!(item instanceof DiabloItem)) {
				continue;
			}
			final DiabloItem diabloItem = (DiabloItem) item;
			diabloItem.setDropChance(chance);
			diabloItem.setRarityChances(rarityChances);
			drops.add(new Pair<>(diabloItem, amount));
		}
	}

	private boolean isSet(final DiabloLike plugin, final ItemRepository repo, final ConfigurationSection dungeonSection,
	                      final String sectionName) {
		if (!dungeonSection.isSet(sectionName)) {
			plugin.getLogger()
			      .log(Level.WARNING, String.format("Missing '%s' for '%s' in file '%s'", sectionName, repo.getId(),
			                                        repo.getRepository()));
			return false;
		}
		return true;
	}

	@Override
	public Collection<Pair<DiabloItem, Range>> getAvailableDropsForMob() {
		return drops;
	}
}
