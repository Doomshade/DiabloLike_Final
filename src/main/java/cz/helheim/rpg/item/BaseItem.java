package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 03.07.2022
 */
public interface BaseItem extends Sellable, Comparable<BaseItem> {
	ItemStack getItemStack();

	String getId();

	String getType();

	double getDropChance();

	void setDropChance(double dropChance);

	boolean hasDefaultProperties();

	@Override
	default int compareTo(BaseItem o) {
		return Double.compare(getDropChance(), o.getDropChance());
	}
}
