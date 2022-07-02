package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Doomshade
 * @version 1.0
 * @since 28.06.2022
 */
public interface ItemRepository {
	Collection<DiabloItem> getAvailableDrops();

	<T extends Item> Optional<T> getItem(String id) throws ClassCastException;

	<T extends Item> Optional<T> getItem(ItemStack item) throws ClassCastException;

	void addItem(Item diabloItem, String id);
}
