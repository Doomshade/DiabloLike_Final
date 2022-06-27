package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface Scroll {
	Scroll.Result identify(DiabloItem diabloItem);

	Range getLevelRange();

	enum Result {
		SUCCESS_IDENTIFY,
		FAILURE_SCROLL_LEVEL_LOW,
		FAILURE_ITEM_IDENTIFIED,
		FAILURE_INVALID_ITEM
	}
}
