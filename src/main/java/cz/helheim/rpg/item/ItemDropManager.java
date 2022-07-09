package cz.helheim.rpg.item;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.item.impl.DefaultItemDropManager;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public interface ItemDropManager {

	static ItemDropManager newInstance(final HelheimPlugin plugin, final ItemRepository repository) {
		return new DefaultItemDropManager(plugin, repository);
	}

	Collection<ItemStack> getRandomLoot(List<Drop> items, final int limit);

	List<String> selectAttributes(DiabloItem diabloItem);

	List<Drop> getDropForLevel(int level);

	DiabloItem.Tier getTier(DiabloItem diabloItem) throws IllegalArgumentException;
}
