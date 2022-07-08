package cz.helheim.rpg.item;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.item.impl.DefaultItemLoader;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.06.2022
 */
public interface ItemLoader {

	static ItemLoader createInstance(HelheimPlugin plugin) {
		return new DefaultItemLoader(plugin);
	}

	Optional<? extends BaseItem> getBaseItem(String id, ConfigurationSection section);

	void setTier(DiabloItem.Tier tier, DiabloItem diabloItem);
}
