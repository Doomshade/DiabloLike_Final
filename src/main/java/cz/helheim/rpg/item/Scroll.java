package cz.helheim.rpg.item;

import cz.helheim.rpg.item.impl.DefaultScroll;
import cz.helheim.rpg.util.Range;
import org.bukkit.inventory.ItemStack;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface Scroll extends BaseItem {

	static Scroll newInstance(final String id, final ItemStack item, final Range range) {
		return new DefaultScroll(id, item, range);
	}

	Scroll.Result identify(DiabloItem diabloItem);

	Range getIdentifyRange();

	enum Result {
		SUCCESS_IDENTIFY,
		FAILURE_SCROLL_LEVEL_LOW,
		FAILURE_ITEM_IDENTIFIED,
		FAILURE_INVALID_ITEM
	}
}
