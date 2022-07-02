package cz.helheim.rpg.item;

import java.util.Collection;
import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public interface ItemDropManager {
	Collection<DiabloItem> getRandomLoot(Collection<DiabloItem> items, final int limit);

	List<String> selectAttributes(DiabloItem diabloItem);
}
