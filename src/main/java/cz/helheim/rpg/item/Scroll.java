package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;
import org.bukkit.inventory.ItemStack;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface Scroll extends BaseItem {

	static Scroll newInstance(final String id, final ItemStack item, final Range range, final double dropChance,
	                          final boolean hasDefaultProperties) {
		return new DefaultScroll(id, item, range, dropChance, hasDefaultProperties);
	}

	Scroll.Result identify(DiabloItem diabloItem);

	Range getIdentifyRange();

	@Override
	default String getType() {
		return "scroll";
	}

	enum Result {
		SUCCESS_IDENTIFY,
		FAILURE_SCROLL_LEVEL_LOW,
		FAILURE_ITEM_IDENTIFIED,
		FAILURE_INVALID_ITEM
	}
}
