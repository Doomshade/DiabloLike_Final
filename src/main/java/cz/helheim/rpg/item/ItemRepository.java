package cz.helheim.rpg.item;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Doomshade
 * @version 1.0
 * @since 28.06.2022
 */
public interface ItemRepository extends Iterable<BaseItem> {

	static ItemRepository newItemRepository(final File repository, final ItemRepositoryLoader itemRepositoryLoader) {
		return new DefaultItemRepository(repository, itemRepositoryLoader);
	}

	/**
	 * @return the file containing the items
	 */
	File getRepository();

	FileConfiguration getRoot();

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

	void addItem(BaseItem diabloItem, String id);
}
