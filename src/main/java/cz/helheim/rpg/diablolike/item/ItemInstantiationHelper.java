package cz.helheim.rpg.diablolike.item;

import cz.helheim.rpg.diablolike.api.DiabloLike;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.diablolike.api.item.*;
import cz.helheim.rpg.diablolike.item.*;
import cz.helheim.rpg.diablolike.util.Range;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 18.07.2022
 */
public interface ItemInstantiationHelper {

	Scroll ADMIN_SCROLL = new DefaultScroll(null, null, null, 0d, false, false, false);

	static BaseItem baseItem(final ItemStack item, final String id, final double dropChance, final boolean defaultProperties,
							 final boolean dropsRepeatedly) {
		return new DefaultBaseItem(item, id, dropChance, defaultProperties, dropsRepeatedly);
	}

	static DiabloItem diabloItem(final String id, final ItemStack itemStack, final int level, final double dropChance,
								 final Map<DiabloItem.Tier, Double> customRarities, final boolean hasDefaultProperties,
								 final boolean dropsRepeatedly) {
		return new DefaultDiabloItem(id, itemStack, level, dropChance, customRarities, hasDefaultProperties, dropsRepeatedly);
	}

	static Drop drop(final Iterable<BaseItem> drop, final Range amount, final double dropChance,
					 final Map<DiabloItem.Tier, Double> rarityChances) {
		return new DefaultDrop(drop, amount, dropChance, rarityChances);
	}

	static Drop drop(final Iterable<BaseItem> drop, final int amount, final double dropChance,
	                 final Map<DiabloItem.Tier, Double> rarityChances) {
		return new DefaultDrop(drop, amount, dropChance, rarityChances);
	}


	static Drop emptyDrop() {
		return new DefaultDrop();
	}

	static DropManager dropManager(final DiabloLike plugin) {
		return new DefaultDropManager(plugin);
	}

	static ItemRepository itemRepository(final File repository, final ItemRepositoryLoader itemRepositoryLoader) {
		return new DefaultItemRepository(repository, itemRepositoryLoader);
	}

	static ItemRepositoryLoader itemRepositoryLoader(HelheimPlugin plugin, ConfigurationSection section) {
		return new DefaultItemRepositoryLoader(plugin, section);
	}

	static Scroll scroll(final String id, final ItemStack item, final Range range, final double dropChance,
	                     final boolean hasDefaultProperties, final boolean dropsRepeatedly) {
		return new DefaultScroll(id, item, range, dropChance, hasDefaultProperties, dropsRepeatedly);
	}
}
