package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The interface for all items regarding DiabloLike items that have attributes (such as armour, weapons, necklaces, ...)
 *
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem extends BaseItem {

	static DiabloItem newInstance(final String id, final ItemStack itemStack, final int level, final List<String> originalLore,
	                              final double dropChance, final Map<Tier, Double> customRarities, final boolean hasDefaultProperties) {
		return new DefaultDiabloItem(id, itemStack, level, originalLore, dropChance, customRarities, hasDefaultProperties, false);
	}

	void addCustomEnchantment(CustomEnchantment enchantment, int level);

	void addEnchantment(Enchantment enchantment, int level);

	Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments();

	Collection<Pair<Enchantment, Integer>> getEnchantments();

	List<String> getOriginalLore();

	List<String> getAttributes();

	List<String> getRequirements();

	Map<Tier, Double> getRarities();

	void setRarities(Map<Tier, Double> rarities);

	@Override
	default String getType() {
		return "diabloitem";
	}

	int getLevel();

	enum Tier {
		COMMON,
		UNCOMMON,
		RARE,
		LEGENDARY,
		MYTHIC;

		static int UNKNOWN_TIER = Integer.MAX_VALUE;

		public String toString() {
			return name().toLowerCase();
		}
	}

}
