package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import cz.helheim.rpg.util.Range;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DefaultScroll extends AbstractBaseItem implements Scroll {
	// TODO config
	private static final Pattern IDENTIFY_PATTERN = Pattern.compile("Neidentifikovaný předmět");

	private final Range range;


	DefaultScroll(final String id, final ItemStack item, final Range range, final double dropChance, final boolean hasDefaultProperties,
	              final boolean dropsRepeatedly, boolean validateParams) {
		super(item, id, dropChance, hasDefaultProperties, dropsRepeatedly, validateParams);
		if (validateParams) {
			notNull(range);
		}
		this.range = range;
	}

	DefaultScroll(final String id, final ItemStack item, final Range range, final double dropChance, final boolean hasDefaultProperties,
	              final boolean dropsRepeatedly) {
		this(id, item, range, dropChance, hasDefaultProperties, dropsRepeatedly, true);
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
	public Result identify(final DiabloItem diabloItem, final InventoryHolder inventoryHolder, final boolean consumeScroll) {
		final ItemStack item;
		if (diabloItem == null || (item = diabloItem.getItemStack()) == null || !item.hasItemMeta()) {
			return Result.FAILURE_INVALID_ITEM;
		}

		// the index in list of the "identify" pattern
		final int identifyIndex = getIdentifyPatternIndex(diabloItem);
		if (identifyIndex < 0) {
			return Result.FAILURE_ITEM_IDENTIFIED;
		}

		// the scroll is not in a valid range
		if (range != null && !range.isInRange(diabloItem.getLevel(), true, true)) {
			return Result.FAILURE_SCROLL_LEVEL_LOW;
		}

		// everything is ok, we should identify
		final ItemMeta meta = item.getItemMeta();
		final List<String> lore = meta.getLore();

		// remove the "unidentified" item in lore
		lore.remove(identifyIndex);

		// add random properties
		// TODO
		final List<String> originalLore = diabloItem.getOriginalLore();
		for (final ListIterator<String> it = lore.listIterator(identifyIndex); it.hasNext(); ) {
			final String s = it.next();
		}

		// add enchantments
		for (final Pair<Enchantment, Integer> pair : diabloItem.getEnchantments()) {
			diabloItem.getItemStack()
			          .addUnsafeEnchantment(pair.getA(), pair.getB());
		}

		// add custom enchantments
		for (final Pair<CustomEnchantment, Integer> pair : diabloItem.getCustomEnchantments()) {
			pair.getA()
			    .addToItem(diabloItem.getItemStack(), pair.getB());
		}
		if (consumeScroll) {
			consumeItem(inventoryHolder);
		}
		return Result.SUCCESS_IDENTIFY;
	}

	@Override
	public Range getIdentifyRange() {
		return range;
	}

	private void consumeItem(final InventoryHolder inventoryHolder) {
		final ItemStack item = getItemStack();
		if (item != null) {
			inventoryHolder.getInventory()
			               .remove(item);
		}
	}

}
