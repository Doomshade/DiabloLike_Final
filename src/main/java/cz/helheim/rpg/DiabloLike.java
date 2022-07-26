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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.helheim.rpg.item.ItemInstantiationHelper.*;
import static cz.helheim.rpg.util.ExceptionUtils.internalError;

public class DiabloLike extends HelheimPlugin {

	private static final String MAIN_REPOSITORY_ID = "items";
	private static DiabloLike instance = null;

	/**
	 * The level pattern used in mobs
	 * TODO: add it to config
	 */
	private final Pattern levelPattern = Pattern.compile("\\[Lv\\. (?<lvl>\\d+)]");
	/**
	 * An item repository mapped to an ID
	 */
	private final Map<String, ItemRepository> repositories = new HashMap<>();

	/**
	 * An item repository loader mapped to a repo ID
	 */
	private final Map<String, ItemRepositoryLoader> repositoryLoaders = new HashMap<>();
	/**
	 * Collection of drops mapped to a repo ID
	 */
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
			loadDungeons(dungeons);
		}
	}

	private void loadDungeons(final File[] dungeons) {

		for (final File dungeon : dungeons) {
			final FileConfiguration loader = YamlConfiguration.loadConfiguration(dungeon);
			for (final String mobId : loader.getKeys(false)) {
				/*
				EfritHC:
				  SinisterKolekceHC:
				    amount: 1
				    chance: 25.0
				  SinisterKolekce:
				    chance: 0.0
				    amount: 1-1
				  Mesmarka:
				    chance: 25
				    amount: 1-2
				*/
				final ConfigurationSection mobSection = loader.getConfigurationSection(mobId);
				for (final String repoId : mobSection.getKeys(false)) {
					/*
				    SinisterKolekceHC:
				      amount: 1
				      chance: 25.0
					*/
					final ConfigurationSection repoSection = mobSection.getConfigurationSection(repoId);
					// the repository loader loads the metadata of the repository such as the global amount, chance, etc.
					// this can be used for an inner repository of a mob -- the repository ID is provided as a section
					// and its global modifiers are passed down as properties
					// from that we are able to construct a new drop
					final ItemRepositoryLoader repoLoader =
							itemRepositoryLoader(this, repoSection);
					dungeonSpecificDrops.computeIfAbsent(mobId, k -> new ArrayList<>())
					                    .add(drop(getRepository(repoId),
					                              repoLoader.getAmount(),
					                              repoLoader.getDropChance(),
					                              repoLoader.getRarityChances()));
				}
			}
		}
	}

	private void loadRepositories() {
		repositories.clear();
		loadItemsRepository();
		loadCollectionRepositories();
		dropManager = dropManager(this);
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
				itemRepositoryLoader(this, YamlConfiguration.loadConfiguration(itemsFile));
		final ItemRepository repo = itemRepository(itemsFile, itemRepositoryLoader);
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
						itemRepositoryLoader(this, YamlConfiguration.loadConfiguration(collection));
				final ItemRepository repo = itemRepository(collection, itemRepositoryLoader);
				repositories.put(repo.getId(), repo);
				repositoryLoaders.put(repo.getId(), itemRepositoryLoader);
			}
		}
	}

	/**
	 * @return the main {@link ItemRepository} (items.yml)
	 */
	public ItemRepository getMainItemRepository() {
		return repositories.get(MAIN_REPOSITORY_ID);
	}

	public ItemRepositoryLoader getRepositoryLoader(ItemRepository repository) {
		final String repoId = repository.getId();
		if (!repositoryLoaders.containsKey(repoId)) {
			final Logger logger = getLogger();
			logger.log(Level.INFO, "Could not find a repository with ID " + repoId);
			logger.log(Level.INFO, "Reloading repositories...");
			loadRepositories();
			if (!repositoryLoaders.containsKey(repoId)) {
				logger.log(Level.SEVERE,
				           internalError(this, "Could not find a repository with ID {} even after reloading it."),
				           new Object[] {repoId});
			}
		}
		return repositoryLoaders.get(repoId);
	}

	public DropManager getItemDropManager() {
		assert dropManager != null;
		return dropManager;
	}

	public Drop getAvailableDrop(Entity entity) {
		// the entity must be a mythic mob if possible
		if (usesMythicMob) {
			final IMobsAPI api = MythicMobs.inst()
			                               .getAPI()
			                               .getMobAPI();
			if (!api.isMythicMob(entity)) {
				return emptyDrop();
			}
		}

		// the mob doesn't have a custom name, abort
		String mobName = entity.getCustomName();
		if (mobName == null) {
			return emptyDrop();

		}
		mobName = ChatColor.stripColor(mobName);
		final Matcher m = levelPattern.matcher(mobName);

		// the mob name contains [Lv. XX]
		if (m.find()) {
			return getItemDropManager().getAvailableDropForLevel(Integer.parseInt(m.group("lvl")));
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
		return emptyDrop();
	}

	public ItemRepository getRepository(String id) {
		return repositories.get(id);
	}
}
