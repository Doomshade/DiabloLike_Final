package cz.helheim.rpg.diablolike.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public final class ItemUtils {
	public static ItemStack readItemStack(ConfigurationSection section) throws InvalidConfigurationException {
		if (!section.isString("displayName")) {
			throw new InvalidConfigurationException("'displayName' is missing!");
		}
		if (!section.isString("materialName")) {
			throw new InvalidConfigurationException("'materialName' is missing!");
		}
		if (!section.isList("lore")) {
			throw new InvalidConfigurationException("'lore' is missing!");
		}
		final String displayName = section.getString("displayName");
		if (displayName.isEmpty() || displayName.equals(" ")) {
			throw new InvalidConfigurationException("'displayName' is missing!");
		}
		final Material material = Material.matchMaterial(section.getString("materialName"));
		if (material == null) {
			throw new InvalidConfigurationException("Invalid 'materialName'!");
		}

		final short damage = (short) section.getInt("data", 0);
		final List<String> lore = section.getStringList("lore")
		                                 .stream()
		                                 .map(x -> x.isEmpty() ? x : ChatColor.translateAlternateColorCodes('&', x))
		                                 .collect(Collectors.toList());
		final ItemStack item = new ItemStack(material, 1, damage);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static boolean isValid(final ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta()
		                                                 .hasLore();
	}
}
