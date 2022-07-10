package cz.helheim.rpg.item;

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
class DefaultItemRepository implements ItemRepository {
	private final Map<String, BaseItem> items = new LinkedHashMap<>();
	private final File repository;

	private final FileConfiguration root;

	private final String id;

	public DefaultItemRepository(final File repository, final ItemRepositoryLoader itemRepositoryLoader) {
		requireNonNull(repository);
		if (!repository.exists()) {
			throw new IllegalArgumentException(String.format("Repository '%s' does not exist.", repository.getAbsolutePath()));
		}
		if (!repository.isFile()) {
			throw new IllegalArgumentException(String.format("Repository '%s' is not a file.", repository.getAbsolutePath()));
		}
		this.repository = repository;
		final String fileName = repository.getName();
		this.id = fileName.substring(0, fileName.lastIndexOf('.'));
		this.root = YamlConfiguration.loadConfiguration(repository);
		initializeRepository(itemRepositoryLoader);
	}

	private void initializeRepository(final ItemRepositoryLoader itemRepositoryLoader) {
		final FileConfiguration loader = YamlConfiguration.loadConfiguration(repository);
		for (final String key : loader.getKeys(false)) {
			// load the base item
			itemRepositoryLoader.getBaseItem(key)
			                    .ifPresent(baseItem -> addItem(baseItem, key));
		}
	}

	@Override
	public File getRepository() {
		return repository;
	}

	@Override
	public FileConfiguration getRoot() {
		return root;
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
	public void addItem(final BaseItem baseItem, final String id) {
		items.put(id, baseItem);
	}

	@Override
	public Iterator<BaseItem> iterator() {
		return getItems().iterator();
	}
}
