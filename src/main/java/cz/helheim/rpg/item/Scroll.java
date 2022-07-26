package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface Scroll extends BaseItem {
	/**
	 * Identifies a diablo item
	 *
	 * @param diabloItem      the diablo item to identify
	 * @param inventoryHolder the holder of the scroll
	 * @param consumeScroll   whether to consume the item
	 *
	 * @return the result of the identification
	 */
	Scroll.Result identify(DiabloItem diabloItem, final InventoryHolder inventoryHolder, final boolean consumeScroll);

	/**
	 * @return the level range this scroll is able to identify
	 */
	Range getIdentifyRange();

	@Override
	default String getType() {
		return "scroll";
	}

	enum Result {
		/**
		 * When the item is successfully identified
		 */
		SUCCESS_IDENTIFY,
		/**
		 * When the scroll identify level is too low
		 */
		FAILURE_SCROLL_LEVEL_LOW,
		/**
		 * When the target item is already identified
		 */
		FAILURE_ITEM_IDENTIFIED,
		/**
		 * When the target item is not a valid item
		 */
		FAILURE_INVALID_ITEM
	}
}
