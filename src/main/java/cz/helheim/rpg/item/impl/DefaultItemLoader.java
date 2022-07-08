package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.*;
import cz.helheim.rpg.util.ItemUtils;
import cz.helheim.rpg.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class DefaultItemLoader implements ItemLoader {
	private final HelheimPlugin plugin;

	public DefaultItemLoader(final HelheimPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Optional<? extends BaseItem> getBaseItem(final String id, final ConfigurationSection section) {
		final ItemStack rawItem;
		try {
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
		final int itemLevel = findItemLevel(lore, settings);

		// look for the required lore for the scroll
		// put it in the else-if statement so the computation is only done
		// if the else-if branch is reached
		final Matcher requiredScrollLore;

		final BaseItem baseItem;
		if (itemLevel >= 0) {
			baseItem = getDiabloItem(id, section, rawItem, lore, settings, itemLevel);
		} else if ((requiredScrollLore = findRequiredScrollLore(lore, settings)) != null) {
			final int lvlMin = Integer.parseInt(requiredScrollLore.group("lvl_min"));
			final String lvlMaxGroup = requiredScrollLore.group("lvl_max");
			baseItem = new DefaultScroll(rawItem, new Range(lvlMin, lvlMaxGroup == null ? lvlMin : Integer.parseInt(lvlMaxGroup)));
		} else {
			plugin.getLogger()
			      .log(Level.INFO,
			           "Failed to register item with ID " + id + ". Item is neither a DiabloItem or Scroll (does " +
			           "not have level or required scroll lore)");
			return empty();
		}

		// add enchantments to a diabloitem
		if (baseItem instanceof DiabloItem && section.isConfigurationSection("enchantments")) {
			addEnchantments(section, rawItem, (DiabloItem) baseItem);
		}
		return Optional.of(baseItem);
	}

	@Override
	public void setTier(final DiabloItem.Tier tier, final DiabloItem diabloItem) {
		final ItemStack itemStack = diabloItem.getItemStack();

		// get the tier colour
		final DiabloLikeSettings settings = plugin.getSettings();
		String tierColour = settings.getTierColour(tier);

		// validate the tier colour in settings
		if (tierColour.isEmpty()) {
			plugin.getLogger()
			      .log(Level.INFO, "Missing tier colour for " + tier.name());
			return;
		}
		tierColour = ChatColor.translateAlternateColorCodes('&', tierColour);

		// add a NBT tag for the tier
		NBTTagManager.getInstance()
		             .addNBTTag(diabloItem, NBTKey.TIER, (item, key) -> item.setInteger(key, tier.ordinal()));

		// prefix it with the colour
		final ItemMeta meta = itemStack.getItemMeta();
		String displayName = meta.getDisplayName();

		// if the name starts with a colour replace it, otherwise just add the prefix
		if (displayName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
			displayName = tierColour.concat(displayName.substring(2));
		} else {
			displayName = tierColour.concat(displayName);
		}
		meta.setDisplayName(displayName);
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

	private DiabloItem getDiabloItem(final String id, final ConfigurationSection section, final ItemStack rawItem, final List<String> lore,
	                                 final DiabloLikeSettings settings, final int itemLevel) {
		final double dropChance = section.getDouble("chance", settings.getDropChance());
		final Map<DiabloItem.Tier, Double> customRarities = new HashMap<>();
		for (final DiabloItem.Tier tier : DiabloItem.Tier.values()) {
			customRarities.put(tier, section.getDouble(tier.toString(), settings.getRarityChance(tier)));
		}

		// the item is a diablo item, parse the section and add some metadata
		final DiabloItem diabloItem = new DefaultDiabloItem(rawItem, itemLevel, lore, dropChance, customRarities);
		NBTTagManager.getInstance()
		             .addNBTTag(diabloItem, NBTKey.ID, (item, key) -> item.setString(key, id));
		return diabloItem;
	}

	private int findItemLevel(final List<String> lore, final DiabloLikeSettings settings) {
		final Pattern lvlPattern = Pattern.compile(settings.getRequiredLevelFormat("(?<lvl>\\d+)"));
		int itemLevel = -1;
		for (String s : lore) {
			Matcher m = lvlPattern.matcher(s);
			if (m.find()) {
				itemLevel = Integer.parseInt(m.group("lvl"));
				break;
			}
		}
		return itemLevel;
	}

	private void addEnchantments(final ConfigurationSection section, final ItemStack rawItem, final DiabloItem diabloItem) {
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
				// TODO log sth
				plugin.getLogger()
				      .log(Level.WARNING,
				           String.format("Invalid enchantment '%s' found at '%s'", enchantmentName, enchantments.getCurrentPath()));
			} else {
				diabloItem.addCustomEnchantment(customEnchantment, enchLevel);
			}
		}
	}

}
