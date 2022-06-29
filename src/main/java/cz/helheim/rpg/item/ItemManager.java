package cz.helheim.rpg.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.06.2022
 */
public interface ItemManager {

	Optional<Scroll> getScroll(ItemStack item);

	Optional<Scroll> getScroll(String id, ConfigurationSection item);

	Optional<DiabloItem> getDiabloItem(ItemStack item);

	Optional<DiabloItem> getDiabloItem(String id, ConfigurationSection section);

	void setTier(DiabloItem.Tier tier, DiabloItem diabloItem);
}
