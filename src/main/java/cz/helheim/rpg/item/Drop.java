package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

import java.util.Map;

/**
 * A collection of items with additional metadata, such as the drop chance
 *
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public interface Drop extends Comparable<Drop>, Iterable<BaseItem> {

	/**
	 * @return whether the drop is empty
	 */
	boolean isEmpty();

	/**
	 * Adds an item to the drop
	 *
	 * @param item the item to add
	 *
	 * @throws NullPointerException if the item is {@code null}
	 */
	void addItem(BaseItem item) throws NullPointerException;

	/**
	 * @return the amount of items that can drop from this
	 */
	Range getAmount();

	/**
	 * @return the drop chance of this drop
	 */
	double getDropChance();

	/**
	 * @return the default global rarity chances of this drop
	 */
	Map<DiabloItem.Tier, Double> getRarityChances();
}
