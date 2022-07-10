package cz.helheim.rpg.item;

import cz.helheim.rpg.DiabloLike;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public interface DropManager {

	static DropManager newInstance(final DiabloLike plugin) {
		return new DefaultDropManager(plugin);
	}

	/**
	 * <p>The loot is rolled as follows:</p>
	 * <p>A number between 0 and 100 is rolled for <strong>each</strong> drop, if the roll is good, the drop is added to a pool of
	 * items.</p>
	 * <p>After all the rolls the drop is sorted in ascending order based on the drop chance.</p>
	 * <p>Items with 100% drop chance and the rarest items get added to the loot. The items with 100% drop chance <strong>are
	 * not</strong></p>
	 * <p>included in the drop amount, they <strong>always</strong> drop</p><br>
	 * <p>The items in the pool then roll for rarities as follows:</p>
	 * <p>A number between 0 and 100 is rolled. Each tier in descending order (i.e. the rarest is the first, the most common is the
	 * last)</p>
	 * <p>subtracts its rarity chance from the number. Once the number is <= 0 the last tier that subtracted its rarity chance is
	 * chosen.</p>
	 * <p>This way an item with 100% chance to be rare will always <strong>at least</strong> be rare, and still has a chance to roll
	 * higher.</p>
	 *
	 * @param drop
	 *
	 * @return the loot from the drop pool
	 */
	Collection<ItemStack> getRandomLoot(final Drop drop);

	List<String> selectAttributes(DiabloItem diabloItem);

	Drop getAvailableDropsForLevel(final ItemRepository repository, int level);
}
