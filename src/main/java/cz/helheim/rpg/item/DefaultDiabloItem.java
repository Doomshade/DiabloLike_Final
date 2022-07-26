package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.apache.commons.lang.Validate.noNullElements;
import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DefaultDiabloItem extends AbstractBaseItem implements DiabloItem {

	private final Collection<Pair<CustomEnchantment, Integer>> customEnchantments = new ArrayList<>();
	private final Collection<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();

	private final int level;

	private final List<String> originalLore = new ArrayList<>();
	private final Map<Tier, Double> customRarities = new LinkedHashMap<>();

	DefaultDiabloItem(final String id, final ItemStack itemStack, final int level, final List<String> originalLore,
	                  final double dropChance, final Map<Tier, Double> customRarities, final boolean hasDefaultProperties,
	                  final boolean dropsRepeatedly) {
		super(itemStack, id, dropChance, hasDefaultProperties, dropsRepeatedly);
		notNull(originalLore);
		noNullElements(originalLore);


		if (level < 0) {
			throw new IllegalArgumentException(String.format("Invalid level %d for %s",
			                                                 level,
			                                                 itemStack.getItemMeta()
			                                                          .getDisplayName()));
		}

		this.level = level;
		this.originalLore.addAll(originalLore);
		this.customRarities.putAll(customRarities);
		NBTTagManager.getInstance()
		             .addNBTTag(this, NBTKey.ID, (item, key) -> item.setString(key, id));
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
		setHasDefaultProperties(false);
		this.customRarities.putAll(rarities);
	}

	@Override
	public int getLevel() {
		return level;
	}
}