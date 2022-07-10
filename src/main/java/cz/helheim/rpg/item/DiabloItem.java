package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem extends BaseItem {

	static DiabloItem newInstance(final String id, final ItemStack itemStack, final int level, final List<String> originalLore,
	                              final double dropChance, final Map<Tier, Double> customRarities, final boolean hasDefaultProperties) {
		return new DefaultDiabloItem(id, itemStack, level, originalLore, dropChance, customRarities, hasDefaultProperties);
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

		public String toString() {
			return name().toLowerCase();
		}
	}

}
