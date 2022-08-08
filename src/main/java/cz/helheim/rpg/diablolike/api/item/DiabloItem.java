package cz.helheim.rpg.diablolike.api.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.diablolike.util.Pair;
import org.bukkit.enchantments.Enchantment;

import java.util.Collection;
import java.util.Map;

/**
 * The interface for all items regarding DiabloLike items that have attributes (such as armour, weapons, necklaces, ...)
 *
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem extends Identifiable {
	void addCustomEnchantment(CustomEnchantment enchantment, int level);

	void addEnchantment(Enchantment enchantment, int level);

	Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments();

	Collection<Pair<Enchantment, Integer>> getEnchantments();

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

		public static int UNKNOWN_TIER = Integer.MAX_VALUE;

		public String toString() {
			return name().toLowerCase();
		}
	}

}
