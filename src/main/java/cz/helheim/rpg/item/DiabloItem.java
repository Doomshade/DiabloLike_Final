package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.item.impl.DefaultDiabloItem;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem extends BaseItem {

	static DiabloItem newInstance(final String id, final ItemStack itemStack, final int level, final List<String> originalLore) {
		return new DefaultDiabloItem(id, itemStack, level, originalLore);
	}

	void addCustomEnchantment(CustomEnchantment enchantment, int level);

	void addEnchantment(Enchantment enchantment, int level);

	Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments();

	Collection<Pair<Enchantment, Integer>> getEnchantments();

	List<String> getOriginalLore();

	List<String> getAttributes();

	List<String> getRequirements();

	int getLevel();

	enum Tier {
		COMMON,
		UNCOMMON,
		RARE,
		LEGENDARY,
		MYTHIC;

		public String toString() {
			return name().toLowerCase();
		}
	}

}
