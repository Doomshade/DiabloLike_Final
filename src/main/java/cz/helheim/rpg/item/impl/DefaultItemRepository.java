package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.*;
import cz.helheim.rpg.util.Range;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class DefaultItemRepository implements ItemRepository {
	private final Map<String, BaseItem> items = new LinkedHashMap<>();
	private final File repository;
	private final Map<Integer, Collection<DiabloItem>> drops = new HashMap<>();
	private final HelheimPlugin plugin;
	private final String id;

	public DefaultItemRepository(final HelheimPlugin plugin, final File repository, final ItemLoader itemLoader) {
		requireNonNull(plugin);
		requireNonNull(repository);
		if (!repository.exists()) {
			throw new IllegalArgumentException(String.format("Repository '%s' does not exist.", repository.getAbsolutePath()));
		}
		if (!repository.isFile()) {
			throw new IllegalArgumentException(String.format("Repository '%s' is not a file.", repository.getAbsolutePath()));
		}
		this.plugin = plugin;
		this.repository = repository;
		final String fileName = repository.getName();
		this.id = fileName.substring(0, fileName.lastIndexOf('.'));
		initializeRepository(itemLoader);
	}

	private void initializeRepository(final ItemLoader itemLoader) {
		final FileConfiguration loader = YamlConfiguration.loadConfiguration(repository);
		for (final String key : loader.getKeys(false)) {
			// load the base item
			itemLoader.getBaseItem(key, loader.getConfigurationSection(key))
			          .ifPresent(baseItem -> addItem(baseItem, key));
		}
	}

	@Override
	public File getRepository() {
		return repository;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Collection<BaseItem> getItems() {
		return Collections.unmodifiableCollection(items.values());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseItem> Optional<T> getItem(final String id) throws ClassCastException {
		return (Optional<T>) Optional.ofNullable(items.get(id));
	}

	@Override
	public <T extends BaseItem> Optional<T> getItem(final ItemStack rawItem) throws ClassCastException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Drop getDropForLevel(final int level) {
		final Collection<DiabloItem> drops = new LinkedHashSet<>();
		final DiabloLikeSettings settings = plugin.getSettings();
		final Range levelRange = settings.getDropRange()
		                                 .surround(level);
		for (int num : levelRange) {
			final Collection<? extends DiabloItem> diabloItems = this.drops.get(num);
			if (diabloItems != null) {
				drops.addAll(diabloItems);
			}
		}
		return Drop.newDrop(Drop.toRangedDrop(drops));
	}

	@Override
	public void addItem(final BaseItem baseItem, final String id) {
		items.put(id, baseItem);
		if (baseItem instanceof DiabloItem) {
			final DiabloItem diabloItem = (DiabloItem) baseItem;
			drops.computeIfAbsent(diabloItem.getLevel(), k -> new HashSet<>())
			     .add(diabloItem);
		}
	}

	@Override
	public Iterator<BaseItem> iterator() {
		return getItems().iterator();
	}
}
