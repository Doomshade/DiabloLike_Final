package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

/**
 * The interface for all items regarding DiabloLike items
 *
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public interface BaseItem extends Sellable, Comparable<BaseItem> {
	/**
	 * @return the copy of the item
	 */
	ItemStack getItemStack();

	/**
	 * @return the config ID of this item
	 */
	String getId();

	/**
	 * @return the type of this item
	 */
	String getType();

	/**
	 * @return the drop chance (0.0-100.0) of this item
	 */
	double getDropChance();

	/**
	 * @param dropChance the drop chance of this item
	 */
	void setDropChance(double dropChance);

	/**
	 * @return whether the properties (such as drop chance or rarity) are config-default
	 */
	boolean hasDefaultProperties();

	/**
	 * An item can drop repeatedly, i.e. when the drop is rolled it is not removed from the pool
	 *
	 * @return whether this item can drop repeatedly
	 */
	boolean canDropRepeatedly();

	@Override
	default int compareTo(BaseItem o) {
		return Double.compare(getDropChance(), o.getDropChance());
	}
}
