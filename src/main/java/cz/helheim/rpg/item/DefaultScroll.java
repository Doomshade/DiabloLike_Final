package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.util.Pair;
import cz.helheim.rpg.util.Range;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.helheim.rpg.item.LoreParser.LA_PATTERN;
import static cz.helheim.rpg.item.LoreParser.SAPI_PATTERN;
import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DefaultScroll extends AbstractBaseItem implements Scroll {
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
		final DiabloLikeSettings settings = DiabloLike.getInstance()
		                                              .getSettings();
		final Pattern identifyPattern = Pattern.compile(settings.getUnidentifiedItemLore());
		for (Iterator<String> it = lore.iterator(); it.hasNext(); i++) {
			String s = it.next();
			Matcher m = identifyPattern.matcher(s);
			if (m.find()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Result identify(final Identifiable identifiable, final InventoryHolder inventoryHolder, final boolean consumeScroll) {
		if (identifiable == null) {
			return Result.FAILURE_INVALID_ITEM;
		}

		// the index in list of the "identify" pattern
		if (identifiable.isIdentified()) {
			return Result.FAILURE_ITEM_IDENTIFIED;
		}

		// the scroll is not in a valid range
		if (range != null && !range.isInRange(identifiable.getLevel(), true, true)) {
			return Result.FAILURE_SCROLL_LEVEL_LOW;
		}

		// everything is ok, we should identify
		final List<String> selectedAttributes = DiabloLike.getInstance()
		                                                  .getItemDropManager()
		                                                  .selectAttributes(identifiable);
		for (final String s : selectedAttributes) {
			Matcher m = LA_PATTERN.matcher(s);
			if (!m.find()) {
				m = SAPI_PATTERN.matcher(s);
			}
			final boolean find = m.find();
			assert find : "Selected attributes that do not satisfy certain patterns";
			final int lower = Integer.parseInt(m.group("lower"));
			int upper = lower;
			if (m.groupCount() == 4) {
				upper = Integer.parseInt(m.group("upper"));
			}
			final int roll = new Range(lower, upper).randomValue();
			final String replace = s.replace(m.group("range"), String.valueOf(roll));
			selectedAttributes.set(selectedAttributes.indexOf(s), replace);
		}

		// add enchantments if the identifiable is a diablo item
		if (identifiable instanceof DiabloItem) {
			final DiabloItem diabloItem = (DiabloItem) identifiable;
			for (final Pair<Enchantment, Integer> pair : diabloItem.getEnchantments()) {
				diabloItem.addEnchantment(pair.getA(), pair.getB());
			}

			// add custom enchantments
			for (final Pair<CustomEnchantment, Integer> pair : diabloItem.getCustomEnchantments()) {
				diabloItem.addCustomEnchantment(pair.getA(), pair.getB());
			}
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
