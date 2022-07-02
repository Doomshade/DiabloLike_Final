package cz.helheim.rpg.item;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.06.2022
 */
public interface ItemLoader {

	void registerItem(String id, ConfigurationSection section, ItemRepository repository);

	void setTier(DiabloItem.Tier tier, DiabloItem diabloItem);
}
