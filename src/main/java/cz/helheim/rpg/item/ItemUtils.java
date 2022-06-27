package cz.helheim.rpg.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class ItemUtils {
	public static ItemStack readItemStack(ConfigurationSection section) {
		if (!section.isString("displayName") || !section.isString("materialName") || !section.isList("lore")) {
			return null;
		}
		String displayName = section.getString("displayName");
		Material material = Material.matchMaterial(section.getString("materialName"));
		short damage = (short) section.getInt("data", 0);
		List<String> lore = section.getStringList("lore")
		                           .stream()
		                           .map(x -> x.isEmpty() ? x : ChatColor.translateAlternateColorCodes('&', x))
		                           .collect(Collectors.toList());
		ItemStack item = new ItemStack(material, 1, damage);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
