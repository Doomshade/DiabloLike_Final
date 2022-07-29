package cz.helheim.rpg.item;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.util.ItemUtils;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.helheim.rpg.item.ItemInstantiationHelper.*;
import static java.util.Optional.empty;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
class DefaultItemRepositoryLoader implements ItemRepositoryLoader {
	private final HelheimPlugin plugin;
	private final ConfigurationSection section;

	DefaultItemRepositoryLoader(final HelheimPlugin plugin, final ConfigurationSection section) {
		this.plugin = plugin;
		this.section = section;
	}

	@Override
	public Optional<? extends BaseItem> getBaseItem(final String id) {
		final ItemStack rawItem;
		try {
			if (!section.isConfigurationSection(id)) {
				throw new InvalidConfigurationException(String.format("Section with ID '%s' does not exist!", id));
			}
			rawItem = ItemUtils.readItemStack(section);
		} catch (InvalidConfigurationException e) {
			plugin.getLogger()
			      .log(Level.WARNING, String.format("Failed to read an item with key '%s'", id), e);
			return empty();
		}
		final List<String> lore = rawItem.getItemMeta()
		                                 .getLore();
		final DiabloLikeSettings settings = plugin.getSettings();

		// look for the "level" item in the lore
		// if the level is not present the method returns <0
		// the level MUST be present in order for the item to be treated
		// as a valid diablo item
		final int itemLevel = new LoreParser(lore).findItemLevel();

		// look for the required lore for the scroll
		// put it in the else-if statement so the computation is only done
		// if the else-if branch is reached
		final Matcher requiredScrollLore;

		// get the drop chance for the base item
		double dropChance = settings.getDropChance();
		boolean hasDefaultProperties = true;
		if (section.isDouble("chance")) {
			hasDefaultProperties = false;
			dropChance = section.getDouble("chance");
		}

		// instantiate the base item
		// it's either a DiabloItem (has level), a Scroll (has a specific lore), or a BaseItem (else)
		final BaseItem baseItem;
		if (itemLevel >= 0) {
			final Map<DiabloItem.Tier, Double> customRarities = new LinkedHashMap<>(settings.getRarityChances());
			for (DiabloItem.Tier tier : DiabloItem.Tier.values()) {
				if (section.isDouble(tier.toString())) {
					hasDefaultProperties = false;
					customRarities.put(tier, section.getDouble(tier.toString()));
				}
			}
			baseItem = diabloItem(id, rawItem, itemLevel, dropChance, customRarities, hasDefaultProperties, false);
		} else if ((requiredScrollLore = findRequiredScrollLore(lore, settings)) != null) {
			baseItem = scroll(id, rawItem, new Range(requiredScrollLore.group()), 0d, true, false);
		} else {
			baseItem = baseItem(rawItem, id, dropChance, hasDefaultProperties, false);
		}

		NBTTagManager.getInstance()
		             .addNBTTag(baseItem, NBTKey.DL_TYPE, (x, y) -> x.setString(y, baseItem.getType()));

		// add enchantments to a diabloitem
		if (baseItem instanceof DiabloItem && section.isConfigurationSection("enchantments")) {
			addEnchantments((DiabloItem) baseItem);
		}
		return Optional.of(baseItem);
	}

	@Override
	public Map<DiabloItem.Tier, Double> getRarityChances() {
		final DiabloLikeSettings settings = plugin.getSettings();
		final Map<DiabloItem.Tier, Double> customRarities = new HashMap<>();
		for (final DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			customRarities.put(tier, section.getDouble(tier.toString(), settings.getRarityChance(tier)));
		}
		return customRarities;
	}

	@Override
	public double getDropChance() {
		final DiabloLikeSettings settings = plugin.getSettings();
		return section.getDouble("chance", settings.getDropChance());
	}

	@Override
	public Range getAmount() {
		return new Range(section.getString("amount", "1"));
	}

	private Matcher findRequiredScrollLore(final List<String> lore, final DiabloLikeSettings settings) {
		final Pattern pattern = Pattern.compile(settings.getIdentifierLorePattern("(?<lvl_min>\\d+)", "(?<lvl_max>\\d+)"));
		for (final String s : lore) {
			final Matcher m = pattern.matcher(s);
			if (m.find()) {
				return m;
			}
		}
		return null;
	}

	private DiabloItem getDiabloItem(final String id, final ItemStack rawItem, final int itemLevel,
	                                 final double dropChance, final Map<DiabloItem.Tier, Double> customRarities,
	                                 final boolean hasDefaultProperties) {
		// the item is a diablo item, parse the section and add some metadata
		final DiabloItem diabloItem =
				diabloItem(id, rawItem, itemLevel, dropChance, customRarities, hasDefaultProperties, false);
		NBTTagManager.getInstance()
		             .addNBTTag(diabloItem, NBTKey.ID, (item, key) -> item.setString(key, id));
		return diabloItem;
	}

	private void addEnchantments(final DiabloItem diabloItem) {
		ConfigurationSection enchantments = section.getConfigurationSection("enchantments");
		for (final Map.Entry<String, Object> entry : enchantments.getValues(false)
		                                                         .entrySet()) {
			final String enchantmentName = entry.getKey();
			final Enchantment minecraftEnch = Enchantment.getByName(enchantmentName);
			final int enchLevel = (int) entry.getValue();
			if (minecraftEnch != null) {
				diabloItem.addEnchantment(minecraftEnch, enchLevel);
				continue;
			}

			final CustomEnchantment customEnchantment = EnchantmentAPI.getEnchantment(enchantmentName);
			if (customEnchantment == null) {
				plugin.getLogger()
				      .log(Level.WARNING, "Invalid enchantment '{}' found at '{}'", new Object[] {
						      enchantmentName,
						      enchantments.getCurrentPath()
				      });
			} else {
				diabloItem.addCustomEnchantment(customEnchantment, enchLevel);
			}
		}
	}

}
