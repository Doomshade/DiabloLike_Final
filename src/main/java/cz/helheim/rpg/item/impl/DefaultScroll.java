package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.Scroll;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

import static java.util.Optional.empty;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class DefaultScroll implements Scroll {

	private final Range range;

	private DefaultScroll(final Range range) {
		this.range = range;
	}

	static Optional<Scroll> getScroll(ConfigurationSection section) {
		return Optional.empty();
	}

	static Optional<Scroll> getScroll(ItemStack item) {
		if (item == null) {
			return empty();
		}
		if (!item.hasItemMeta()) {
			return empty();
		}
		final ItemMeta itemMeta = item.getItemMeta();
		if (!itemMeta.hasLore()) {
			return empty();
		}
		for (String s : itemMeta.getLore()) {
			// TODO add from config

		}
		return null;
	}

	@Override
	public Result identify(final DiabloItem diabloItem) {
		if (diabloItem == null) {
			return Result.FAILURE_INVALID_ITEM;
		}
		if (isIdentified(diabloItem)) {
			return Result.FAILURE_ITEM_IDENTIFIED;
		}
		return null;
	}

	@Override
	public Range getLevelRange() {
		return range;
	}

	private boolean isIdentified(final DiabloItem diabloItem) {
		return false;
	}
}
