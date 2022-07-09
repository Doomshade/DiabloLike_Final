package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.Validate.*;

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

	private final List<String> originalLore = new ArrayList<>();
	private final String id;
	private int price = 0;
	private Tier tier = Tier.COMMON;

	public DefaultDiabloItem(final String id, final ItemStack itemStack, final int level, final List<String> originalLore) {
		notNull(itemStack);
		notNull(originalLore);
		notNull(id);
		notEmpty(id);

		noNullElements(originalLore);
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

		this.id = id;
		this.itemStack = itemStack;
		this.level = level;
		this.originalLore.addAll(originalLore);
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
	public List<String> getOriginalLore() {
		return Collections.unmodifiableList(originalLore);
	}

	@Override
	public List<String> getAttributes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getRequirements() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public void setPrice(final int price) {
		this.price = price;
	}
}
