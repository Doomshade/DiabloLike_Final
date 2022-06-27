package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public interface DiabloItem {
	void addCustomEnchantment(CustomEnchantment enchantment, int level);

	void addEnchantment(Enchantment enchantment, int level);

	Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments();

	Collection<Pair<Enchantment, Integer>> getEnchantments();

	ItemStack getItemStack();

	int getLevel();

}
