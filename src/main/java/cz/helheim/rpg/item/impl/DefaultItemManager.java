package cz.helheim.rpg.item.impl;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.sun.istack.internal.Nullable;
import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.ItemManager;
import cz.helheim.rpg.item.ItemUtils;
import cz.helheim.rpg.item.Scroll;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
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
	private static final Pattern LVL_PATTERN = Pattern.compile("Potřebný Lvl: (?<lvl>\\d+)");
	private final IOManager io;

	public DefaultItemManager(final IOManager io) {
		this.io = io;
	}

	@Override
	@Nullable
	public Optional<Scroll> getScroll(final ItemStack item) {
		return DefaultScroll.getScroll(item);
	}

	@Override
	public Optional<Scroll> getScroll(final ConfigurationSection section) {
		return DefaultScroll.getScroll(section);
	}

	@Override
	@Nullable
	public Optional<DiabloItem> getDiabloItem(final ItemStack item) {
		return empty();
	}

	@Override
	public Optional<DiabloItem> getDiabloItem(ConfigurationSection section) {
		ItemStack rawItem = ItemUtils.readItemStack(section);
		if (rawItem == null) {
			return empty();
		}
		int itemLevel = -1;
		// TODO config
		for (String s : rawItem.getItemMeta()
		                       .getLore()) {
			Matcher m = LVL_PATTERN.matcher(s);
			if (m.find()) {
				itemLevel = Integer.parseInt(m.group("lvl"));
				break;
			}
		}
		if (itemLevel < 0) {
			return Optional.empty();
		}
		DiabloItem diabloItem = new DefaultDiabloItem(rawItem, itemLevel);
		if (section.isConfigurationSection("enchantments")) {
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
		return of(diabloItem);
	}

}
