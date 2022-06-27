package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class DefaultDiabloItem implements DiabloItem {

	private final Collection<Pair<CustomEnchantment, Integer>> customEnchantments = new ArrayList<>();
	private final Collection<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();

	private final ItemStack itemStack;
	private final int level;

	public DefaultDiabloItem(final ItemStack itemStack, final int level) {
		if (itemStack == null) {
			throw new IllegalArgumentException("item cannot be null");
		}
		if (!itemStack.hasItemMeta() || itemStack.getItemMeta()
		                                         .hasEnchants()) {
			throw new IllegalArgumentException(String.format("The item '%s' must have a meta and no enchants!", itemStack));
		}
		if (level < 0) {
			throw new IllegalArgumentException(String.format("Invalid level %d for %s",
			                                                 level,
			                                                 itemStack.getItemMeta()
			                                                          .getDisplayName()));
		}
		this.itemStack = itemStack;
		this.level = level;
	}

	@Override
	public void addCustomEnchantment(CustomEnchantment enchantment, int level) {
		this.customEnchantments.add(new Pair<>(enchantment, level));
	}

	@Override
	public void addEnchantment(Enchantment enchantment, int level) {
		this.enchantments.add(new Pair<>(enchantment, level));
	}

	@Override
	public Collection<Pair<CustomEnchantment, Integer>> getCustomEnchantments() {
		return Collections.unmodifiableCollection(customEnchantments);
	}

	@Override
	public Collection<Pair<Enchantment, Integer>> getEnchantments() {
		return Collections.unmodifiableCollection(enchantments);
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public int getLevel() {
		return level;
	}
}
