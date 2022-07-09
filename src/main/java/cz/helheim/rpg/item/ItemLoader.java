package cz.helheim.rpg.item;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.item.impl.DefaultItemLoader;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Optional;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.06.2022
 */
public interface ItemLoader {

	static ItemLoader newInstance(HelheimPlugin plugin, ConfigurationSection section) {
		return new DefaultItemLoader(plugin, section);
	}

	Optional<? extends BaseItem> getBaseItem(String id);

	Map<DiabloItem.Tier, Double> getRarityChances();

	double getDropChance();

	Range getAmount();
}
