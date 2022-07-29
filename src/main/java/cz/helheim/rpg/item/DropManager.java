package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 02.07.2022
 */
public interface DropManager {
	/**
	 * <p>The loot is rolled as follows:</p>
	 * <p>A number between 0 and 100 is rolled for the drop, if the number is &gt; {@link Drop#getDropChance()}, then the result is an
	 * empty collection. Otherwise it continues further:</p>
	 * <p>A number between 0 and 100 is rolled for <strong>each</strong> item in the drop, if the number is &lt;=
	 * {@link BaseItem#getDropChance()}, the item is added to a pool of items, or if the item is guaranteed to drop (aka it has ~100% drop
	 * chance) it's added to the loot. Items with guaranteed chance to drop <strong>are not</strong> included in the drop amount.</p>
	 * <p>Once the rolls are done (pun intended) the drop is sorted in ascending order based on the drop chance (i.e. the least probable
	 * item to drop is the first in the queue). The rarest items are added to the loot until the amount {@link Drop#getAmount()} is reached
	 * .</p><br>
	 * <p>The items in the pool then roll for rarities as such:</p>
	 * <p>A number between 0 and 100 is rolled. Each tier in descending order (i.e. the rarest is the first, the most common is the
	 * last) subtracts its rarity chance from the rolled number. Once the number is &lt;= 0, the last tier that subtracted its rarity
	 * chance
	 * is chosen. This way an item with 100% chance to be rare will always <strong>at least</strong> be rare, and it still maintains a
	 * chance to roll a greater rarity.</p>
	 *
	 * @param drop the drop
	 *
	 * @return the loot from the drop pool
	 */
	Collection<ItemStack> getRandomLoot(final Drop drop);

	List<String> selectAttributes(Identifiable identifiable);

	/**
	 * @param level the level
	 *
	 * @return the available drops for the level
	 */
	Drop getAvailableDropForLevel(int level);
}
