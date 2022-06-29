package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.sun.istack.internal.Nullable;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.ItemManager;
import cz.helheim.rpg.item.ItemUtils;
import cz.helheim.rpg.item.Scroll;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class DefaultItemManager implements ItemManager {
	private static final String NBT_KEY_DIABLO_ITEM = "diabloitem";
	private static final String NBT_KEY_TIER = "tier";
	private final HelheimPlugin plugin;

	public DefaultItemManager(final HelheimPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	@Nullable
	public Optional<Scroll> getScroll(final ItemStack item) {
		return DefaultScroll.getScroll(item);
	}

	@Override
	public Optional<Scroll> getScroll(final String id, final ConfigurationSection section) {
		return DefaultScroll.getScroll(section);
	}

	@Override
	@Nullable
	public Optional<DiabloItem> getDiabloItem(final ItemStack item) {
		return empty();
	}

	@Override
	public Optional<DiabloItem> getDiabloItem(final String id, final ConfigurationSection section) {
		ItemStack rawItem = ItemUtils.readItemStack(section);
		if (rawItem == null) {
			return empty();
		}
		int itemLevel = -1;
		final List<String> lore = rawItem.getItemMeta()
		                                 .getLore();
		final DiabloLikeSettings settings = plugin.getSettings();
		Pattern lvlPattern = Pattern.compile(settings
				                                     .getRequiredLevelFormat()
				                                     .replaceAll("<lvl>", "(?<lvl>\\d+)"));
		for (String s : lore) {
			Matcher m = lvlPattern.matcher(s);
			if (m.find()) {
				itemLevel = Integer.parseInt(m.group("lvl"));
				break;
			}
		}
		if (itemLevel < 0) {
			return Optional.empty();
		}
		// the item is a diablo item, parse the section and add some metadata
		addNBT(id, rawItem);

		final DiabloItem diabloItem = new DefaultDiabloItem(rawItem, itemLevel, lore);
		if (section.isConfigurationSection("enchantments")) {
			addEnchantments(section, rawItem, diabloItem);
		}
		return of(diabloItem);
	}

	@Override
	public void setTier(final DiabloItem.Tier tier, final DiabloItem diabloItem) {
		final ItemStack itemStack = diabloItem.getItemStack();
		final NBTItem nbtItem = new NBTItem(itemStack, true);
		nbtItem.setInteger(NBT_KEY_TIER, tier.ordinal());

		final ItemMeta meta = itemStack.getItemMeta();
		String displayName = meta.getDisplayName();
		DiabloLikeSettings settings = plugin.getSettings();
		String tierColour = settings.getTierColour(tier);
		if (tierColour.isEmpty()) {
			// TODO log
			plugin.getLogger()
			      .log(Level.INFO, "Missing tier colour for " + tier.name());
			return;
		}

		tierColour = ChatColor.translateAlternateColorCodes('&', tierColour);
		if (displayName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
			displayName = tierColour.concat(displayName.substring(2));
		} else {
			displayName = tierColour.concat(displayName);
		}
		meta.setDisplayName(displayName);
	}

	private void addNBT(final String id, final ItemStack rawItem) {
		final NBTItem nbtItem = new NBTItem(rawItem, true);
		nbtItem.setString(NBT_KEY_DIABLO_ITEM, id);
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
			} else {
				diabloItem.addCustomEnchantment(customEnchantment, enchLevel);
				customEnchantment.addToItem(rawItem, enchLevel);
			}
		}
	}

}
