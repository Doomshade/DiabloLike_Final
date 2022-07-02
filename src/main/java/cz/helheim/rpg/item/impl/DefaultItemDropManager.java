package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.ItemDropManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public class DefaultItemDropManager implements ItemDropManager {

	private final HelheimPlugin plugin;

	public DefaultItemDropManager(final HelheimPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Collection<DiabloItem> getRandomLoot(final Collection<DiabloItem> items, int limit) {
		final Collection<DiabloItem> loot = new ArrayList<>();
		final Random random = new Random();

		for (final DiabloItem item : items) {
			final double chance = random.nextDouble() * 100;
			if (chance < item.getDropChance()) {
				loot.add(item);
				limit--;
			}
			if (limit <= 0) {
				break;
			}
		}

		return loot;
	}

	@Override
	public List<String> selectAttributes(final DiabloItem diabloItem) {
		final DiabloLikeSettings settings = plugin.getSettings();
		final List<String> attributes = diabloItem.getAttributes();
		final double dividedBy = settings.getPocetDiv(diabloItem.getTier());

		int count = (int) Math.ceil(attributes.size() / dividedBy);
		final Random random = new Random();

		// invert it to the count that needs to be removed from the pool
		// e.g. attributes size = 4, count = 3 -> count = 1 (aka we need to remove 1 from the attributes to match the count)
		count = attributes.size() - count;
		while (count-- > 0) {
			attributes.remove(random.nextInt(attributes.size()));
		}
		return attributes;
	}
}
