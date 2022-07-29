package cz.helheim.rpg.item;

import org.bukkit.inventory.ItemStack;

import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 18.07.2022
 */
abstract class AbstractBaseItem implements BaseItem {

	protected final ItemStack item;
	private final String id;
	private final boolean dropsRepeatedly;
	private double dropChance;
	private boolean defaultProperties;
	private int price = 0;

	AbstractBaseItem(final ItemStack item, final String id, final double dropChance, final boolean defaultProperties,
	                 final boolean dropsRepeatedly) {
		this(item, id, dropChance, defaultProperties, dropsRepeatedly, true);
	}

	AbstractBaseItem(final ItemStack item, final String id, final double dropChance, final boolean defaultProperties,
	                 final boolean dropsRepeatedly, final boolean validateParams) {
		if (validateParams) {
			notNull(id);
			notEmpty(id);
			notNull(item);
			if (!item.hasItemMeta() || item.getItemMeta()
			                               .hasEnchants()) {
				throw new IllegalArgumentException(String.format("The item '%s' must have a meta and no enchants!", item));
			}
		}
		this.item = item;
		this.id = id;
		this.dropChance = dropChance;
		this.defaultProperties = defaultProperties;
		this.dropsRepeatedly = dropsRepeatedly;
	}

	@Override
	public ItemStack getItemStack() {
		return item.clone();
	}

	@Override
	public final String getId() {
		return id;
	}

	@Override
	public final double getDropChance() {
		return dropChance;
	}

	@Override
	public final void setDropChance(final double dropChance) {
		this.defaultProperties = false;
		this.dropChance = dropChance;
	}

	@Override
	public final boolean hasDefaultProperties() {
		return defaultProperties;
	}

	@Override
	public final boolean canDropRepeatedly() {
		return dropsRepeatedly;
	}

	protected void setHasDefaultProperties(boolean defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	@Override
	public final int getPrice() {
		return price;
	}

	@Override
	public final void setPrice(final int price) {
		this.price = price;
	}
}
