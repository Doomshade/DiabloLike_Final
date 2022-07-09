package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 03.07.2022
 */
public interface BaseItem extends Sellable {
	ItemStack getItemStack();

	String getId();

}
