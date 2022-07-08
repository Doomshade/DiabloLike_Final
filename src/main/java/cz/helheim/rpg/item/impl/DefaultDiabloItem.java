package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

	private final Map<Tier, Double> rarityChances = new HashMap<>();

	private final double dropChance;
	private int price = 0;

	private Tier tier = Tier.COMMON;

	public DefaultDiabloItem(final ItemStack itemStack, final int level, final List<String> originalLore, final double dropChance,
	                         final Map<Tier, Double> rarityChances) {
		if (rarityChances.size() != Tier.values().length) {
			throw new IllegalArgumentException(String.format("Rarity chances size must cover all tiers (covering %d / %d rarities)",
			                                                 rarityChances.size(),
			                                                 Tier.values().length));
		}
		if (dropChance < 0 || dropChance > 100) {
			throw new IllegalArgumentException("Invalid drop chance: " + dropChance);
		}
		if (itemStack == null) {
			throw new IllegalArgumentException("Item cannot be null");
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

		this.dropChance = dropChance;
		this.itemStack = itemStack;
		this.rarityChances.putAll(rarityChances);
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
	public Map<Tier, Double> getRarityChances() {
		return Collections.unmodifiableMap(rarityChances);
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
	public double getDropChance() {
		return dropChance;
	}

	@Override
	public List<String> getAttributes() {
		return null;
	}

	@Override
	public List<String> getRequirements() {
		return null;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public Tier getTier() {
		return tier;
	}

	@Override
	public void setTier(final Tier tier) {
		this.tier = tier;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
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
