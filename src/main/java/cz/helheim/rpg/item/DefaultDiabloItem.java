package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.apache.commons.lang.Validate.*;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DefaultDiabloItem implements DiabloItem {

	private final Collection<Pair<CustomEnchantment, Integer>> customEnchantments = new ArrayList<>();
	private final Collection<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();

	private final ItemStack itemStack;
	private final int level;

	private final List<String> originalLore = new ArrayList<>();
	private final String id;
	private final Map<Tier, Double> customRarities = new LinkedHashMap<>();
	private boolean hasDefaultProperties;
	private int price = 0;
	private double dropChance;

	public DefaultDiabloItem(final String id, final ItemStack itemStack, final int level, final List<String> originalLore,
	                         final double dropChance, final Map<Tier, Double> customRarities, final boolean hasDefaultProperties) {
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

		ItemRepositoryLoader loader;

		this.id = id;
		this.itemStack = itemStack;
		this.level = level;
		this.originalLore.addAll(originalLore);
		this.dropChance = dropChance;
		this.customRarities.putAll(customRarities);
		this.hasDefaultProperties = hasDefaultProperties;
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
	public Map<Tier, Double> getRarities() {
		return Collections.unmodifiableMap(customRarities);
	}

	@Override
	public void setRarities(final Map<Tier, Double> rarities) {
		if (rarities.size() != Tier.values().length) {
			throw new IllegalArgumentException("Invalid rarities length");
		}
		hasDefaultProperties = false;
		this.customRarities.putAll(rarities);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack.clone();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public double getDropChance() {
		return dropChance;
	}

	@Override
	public void setDropChance(final double dropChance) {
		this.hasDefaultProperties = false;
		this.dropChance = dropChance;
	}

	@Override
	public boolean hasDefaultProperties() {
		return hasDefaultProperties;
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
