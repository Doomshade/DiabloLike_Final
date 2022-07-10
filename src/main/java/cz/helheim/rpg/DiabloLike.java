package cz.helheim.rpg;

import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.enchantment.Rychlostrelba;
import cz.helheim.rpg.item.Drop;
import cz.helheim.rpg.item.DropManager;
import cz.helheim.rpg.item.ItemRepository;
import cz.helheim.rpg.item.ItemRepositoryLoader;
import cz.helheim.rpg.listener.MobListener;
import cz.helheim.rpg.listener.MythicMobListener;
import net.elseland.xikage.MythicMobs.API.IMobsAPI;
import net.elseland.xikage.MythicMobs.MythicMobs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiabloLike extends HelheimPlugin {

	private static DiabloLike instance = null;
	private final Pattern levelPattern = Pattern.compile("\\[Lv\\. (?<lvl>\\d+)]");
	private final Map<String, ItemRepository> repositories = new HashMap<>();

	private final Map<String, ItemRepositoryLoader> repositoryLoaders = new HashMap<>();
	private final Map<String, Collection<Drop>> dungeonSpecificDrops = new HashMap<>();
	private DropManager dropManager = null;
	private boolean usesMythicMob = false;

	public static DiabloLike getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		instance = null;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		EnchantmentAPI.registerCustomEnchantment(new Rychlostrelba());
		setSettings(new DiabloLikeSettings(this));
		if (Bukkit.getPluginManager()
		          .isPluginEnabled("MythicMobs")) {
			registerListener(new MythicMobListener(this));
			usesMythicMob = true;
		} else {
			registerListener(new MobListener(this));
		}
	}

	@Override
	protected void load() {
		super.load();
		loadRepositories();
		loadDungeons();
	}

	@Override
	@SuppressWarnings("unchecked")
	public DiabloLikeSettings getSettings() {
		return super.getSettings();
	}

	private void loadDungeons() {
		// TODO generate dungeons
		dungeonSpecificDrops.clear();
		final File dungeonsFolder = new File(getDataFolder(), "dungeons");
		if (!dungeonsFolder.isDirectory()) {
			dungeonsFolder.mkdirs();
		}

		final File[] dungeons = dungeonsFolder.listFiles((x, y) -> x.exists() && x.isFile() && y.endsWith(".yml"));
		if (dungeons != null) {
			for (final File dungeon : dungeons) {
				final FileConfiguration loader = YamlConfiguration.loadConfiguration(dungeon);
				for (final String mobId : loader.getKeys(false)) {
					final ConfigurationSection mobSection = loader.getConfigurationSection(mobId);
					for (final String repoId : mobSection.getKeys(false)) {
						final ItemRepositoryLoader repoLoader =
								ItemRepositoryLoader.newInstance(this, mobSection.getConfigurationSection(repoId));
						dungeonSpecificDrops.computeIfAbsent(mobId, k -> new ArrayList<>())
						                    .add(Drop.newDrop(getRepository(repoId),
						                                      repoLoader.getAmount(),
						                                      repoLoader.getDropChance(),
						                                      repoLoader.getRarityChances()));
					}
				}
			}
		}
	}

	private void loadRepositories() {
		repositories.clear();
		loadItemsRepository();
		loadCollectionRepositories();
	}

	private void loadItemsRepository() {
		final File itemsFile = new File(getDataFolder(), "items.yml");
		if (!itemsFile.exists()) {
			try {
				itemsFile.createNewFile();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		final ItemRepositoryLoader itemRepositoryLoader =
				ItemRepositoryLoader.newInstance(this, YamlConfiguration.loadConfiguration(itemsFile));
		final ItemRepository repo = ItemRepository.newItemRepository(itemsFile, itemRepositoryLoader);
		repositories.put(repo.getId(), repo);
		repositoryLoaders.put(repo.getId(), itemRepositoryLoader);
	}

	private void loadCollectionRepositories() {
		final File collectionsFolder = new File(getDataFolder(), "collections");
		if (!collectionsFolder.exists()) {
			collectionsFolder.mkdirs();
		}
		final File[] collections = collectionsFolder.listFiles((x, y) -> x.exists() && x.isFile() && y.endsWith(".yml"));
		if (collections != null) {
			for (final File collection : collections) {
				final ItemRepositoryLoader itemRepositoryLoader =
						ItemRepositoryLoader.newInstance(this, YamlConfiguration.loadConfiguration(collection));
				final ItemRepository repo = ItemRepository.newItemRepository(collection, itemRepositoryLoader);
				repositories.put(repo.getId(), repo);
				repositoryLoaders.put(repo.getId(), itemRepositoryLoader);
			}
		}
	}

	private Drop getAvailableDropsForLevel(int level) {
		return getItemDropManager().getAvailableDropsForLevel(repositories.get("items"), level);
	}

	public ItemRepositoryLoader getRepositoryLoader(ItemRepository repository) {
		return repositoryLoaders.get(repository.getId());
	}

	public DropManager getItemDropManager() {
		return dropManager == null ? dropManager = DropManager.newInstance(this) : dropManager;
	}

	public Drop getAvailableDrop(Entity entity) {
		// the entity must be a mythic mob if possible
		if (usesMythicMob) {
			final IMobsAPI api = MythicMobs.inst()
			                               .getAPI()
			                               .getMobAPI();
			if (!api.isMythicMob(entity)) {
				return Drop.emptyDrop();
			}
		}

		// the mob doesn't have a custom name, abort
		String mobName = entity.getCustomName();
		if (mobName == null) {
			return Drop.emptyDrop();

		}
		mobName = ChatColor.stripColor(mobName);
		final Matcher m = levelPattern.matcher(mobName);

		// the mob name contains [Lv. XX]
		if (m.find()) {
			return getAvailableDropsForLevel(Integer.parseInt(m.group("lvl")));
		} else if (usesMythicMob) {
			final String mobId = MythicMobs.inst()
			                               .getAPI()
			                               .getMobAPI()
			                               .getMythicMobInstance(entity)
			                               .getType()
			                               .getInternalName();
			/*final DungeonDrop dungeonDrop = this.dungeonSpecificDrops.get(mobId);
			if (dungeonDrop != null) {
				return dungeonDrop.getAvailableDrops();
			}*/
		}
		return Drop.emptyDrop();
	}

	public ItemRepository getRepository(String id) {
		return repositories.get(id);
	}
}
