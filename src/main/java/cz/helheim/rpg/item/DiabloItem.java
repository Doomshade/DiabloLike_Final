package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem extends BaseItem {
	void addCustomEnchantment(CustomEnchantment enchantment, int level);

	void addEnchantment(Enchantment enchantment, int level);

	Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments();

	Map<Tier, Double> getRarityChances();

	Collection<Pair<Enchantment, Integer>> getEnchantments();

	List<String> getOriginalLore();

	double getDropChance();

	List<String> getAttributes();

	List<String> getRequirements();

	int getLevel();

	Tier getTier();

	void setTier(Tier tier);

	enum Tier {
		COMMON,
		UNCOMMON,
		RARE,
		LEGENDARY,
		MYTHIC
	}

}
