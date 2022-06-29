package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.Scroll;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class DefaultScroll implements Scroll {
	private static final Pattern IDENTIFY_PATTERN = Pattern.compile("Neidentifikovaný předmět");

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
		return empty();
	}

	private static int getIdentifyPatternIndex(final DiabloItem diabloItem) {
		final List<String> lore = diabloItem.getItemStack()
		                                    .getItemMeta()
		                                    .getLore();
		int i = 0;
		for (Iterator<String> it = lore.iterator(); it.hasNext(); i++) {
			String s = it.next();
			Matcher m = IDENTIFY_PATTERN.matcher(s);
			if (m.find()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Result identify(final DiabloItem diabloItem) {
		if (diabloItem == null) {
			return Result.FAILURE_INVALID_ITEM;
		}
		final int identifyIndex = getIdentifyPatternIndex(diabloItem);
		if (identifyIndex < 0) {
			return Result.FAILURE_ITEM_IDENTIFIED;
		}
		if (!range.isInRange(diabloItem.getLevel(), true, true)) {
			return Result.FAILURE_SCROLL_LEVEL_LOW;
		}
		final ItemMeta meta = diabloItem.getItemStack()
		                                .getItemMeta();
		final List<String> lore = meta.getLore();
		lore.remove(identifyIndex);
		final List<String> originalLore = diabloItem.getOriginalLore();
		for (ListIterator<String> it = lore.listIterator(identifyIndex); it.hasNext(); ) {

		}

		return Result.SUCCESS_IDENTIFY;
	}

	@Override
	public Range getIdentifyRange() {
		return range;
	}
}
