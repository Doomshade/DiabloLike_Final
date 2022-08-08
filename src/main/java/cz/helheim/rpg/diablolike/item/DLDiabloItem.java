package cz.helheim.rpg.diablolike.item;

import com.rit.sucy.CustomEnchantment;
import cz.helheim.rpg.diablolike.api.item.DiabloItem;
import cz.helheim.rpg.diablolike.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DLDiabloItem extends AbstractBaseItem implements DiabloItem {

	private final Collection<Pair<CustomEnchantment, Integer>> customEnchantments = new ArrayList<>();
	private final Collection<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();

	private final int level;

	private final List<String> originalLore = new ArrayList<>();
	private final Map<Tier, Double> customRarities = new LinkedHashMap<>();
	private boolean identified = false;

	DLDiabloItem(final String id, final ItemStack itemStack, final int level, final double dropChance,
				 final Map<Tier, Double> customRarities, final boolean hasDefaultProperties, final boolean dropsRepeatedly) {
		super(itemStack, id, dropChance, hasDefaultProperties, dropsRepeatedly);
		assert level > 0 : String.format("Invalid level %d for %s",
		                                 level,
		                                 itemStack.getItemMeta()
		                                          .getDisplayName());
		this.level = level;
		this.originalLore.addAll(itemStack.getItemMeta()
		                                  .getLore());
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

	@Override
	public List<String> getOriginalLore() {
		return Collections.unmodifiableList(originalLore);
	}

	@Override
	public boolean isIdentified() {
		return identified;
	}

	@Override
	public void setIdentified(final List<String> newLore, final boolean identified) {
		final ItemMeta meta = super.item.getItemMeta();
		meta.setLore(newLore);
		super.item.setItemMeta(meta);
		this.identified = identified;
	}
}
