package cz.helheim.rpg.diablolike.api.item;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

/**
 * An item repository, basically representing a collection of {@link BaseItem}s with additional metadata and helper functions
 *
 * @author Doomshade
 * @version 1.0
 * @since 28.06.2022
 */
public interface ItemRepository extends Iterable<BaseItem> {
	/**
	 * @return the file containing the items
	 */
	File getRepository();

	/**
	 * @return the root file configuration
	 */
	FileConfiguration getRoot();

	/**
	 * @return the ID of this repository, normally it's denoted by the file name
	 */
	String getId();

	/**
	 * @return the available drops of the repository
	 */
	Collection<BaseItem> getItems();

	/**
	 * @param id  the ID of the item
	 * @param <T> the base item
	 *
	 * @return a base item with the given ID
	 *
	 * @throws ClassCastException
	 */
	<T extends BaseItem> Optional<T> getItem(String id) throws ClassCastException;

	/**
	 * @param item the raw item stack
	 * @param <T>  the base item
	 *
	 * @return a base item from the raw item
	 *
	 * @throws ClassCastException
	 */
	<T extends BaseItem> Optional<T> getItem(ItemStack item) throws ClassCastException;

	/**
	 * Adds an item to the repository
	 *
	 * @param diabloItem the item
	 * @param id         the ID of the item
	 */
	void addItem(BaseItem diabloItem, String id);
}
