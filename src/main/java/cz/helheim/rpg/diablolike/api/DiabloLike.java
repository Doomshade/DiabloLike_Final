package cz.helheim.rpg.diablolike.api;

import cz.helheim.rpg.api.IHelheimPlugin;
import cz.helheim.rpg.diablolike.DiabloLikeImpl;
import cz.helheim.rpg.diablolike.api.item.Drop;
import cz.helheim.rpg.diablolike.api.item.DropManager;
import cz.helheim.rpg.diablolike.api.item.ItemRepository;
import cz.helheim.rpg.diablolike.api.item.ItemRepositoryLoader;
import org.bukkit.entity.Entity;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 26.07.2022
 */
public interface DiabloLike extends IHelheimPlugin {
	static DiabloLike getInstance() {
		return DiabloLikeImpl.getInstance();
	}

	/**
	 * @return the main {@link ItemRepository} (items.yml)
	 */
	ItemRepository getMainItemRepository();

	ItemRepositoryLoader getRepositoryLoader(ItemRepository repository);

	DropManager getItemDropManager();

	Drop getAvailableDrop(Entity entity);

	ItemRepository getRepository(String id);
}
