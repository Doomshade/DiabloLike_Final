package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

/**
 * @author Doomshade
 * @version 1.0
 * @since 18.07.2022
 */
class DefaultBaseItem extends AbstractBaseItem {
	DefaultBaseItem(final ItemStack item, final String id, final double dropChance, final boolean defaultProperties,
	                final boolean dropsRepeatedly) {
		super(item, id, dropChance, defaultProperties, dropsRepeatedly);
	}

	@Override
	public String getType() {
		return "regularitem";
	}
}