package cz.helheim.rpg;

import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.enchantment.Rychlostrelba;
import cz.helheim.rpg.item.*;
import cz.helheim.rpg.listener.MobListener;
import cz.helheim.rpg.listener.MythicMobListener;
import net.elseland.xikage.MythicMobs.API.IMobsAPI;
import net.elseland.xikage.MythicMobs.MythicMobs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiabloLike extends HelheimPlugin {

	private static DiabloLike instance = null;
	private final Pattern levelPattern = Pattern.compile("\\[Lv\\. (?<lvl>\\d+)]");
	private final Map<String, ItemRepository> repositories = new LinkedHashMap<>();
	private final Map<String, DungeonDrop> dungeonSpecificDrops = new HashMap<>();
	private final Map<String, ItemDropManager> dropManagers = new HashMap<>();
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
			registerListener(new MythicMobListener());
			usesMythicMob = true;
		} else {
			registerListener(new MobListener());
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
		dungeonSpecificDrops.clear();
		final File dungeonsFolder = new File(getDataFolder(), "dungeons");
		if (!dungeonsFolder.isDirectory()) {
			dungeonsFolder.mkdirs();
		}
		final File[] dungeons = dungeonsFolder.listFiles((x, y) -> x.exists() && x.isFile() && y.endsWith(".yml"));
		if (dungeons != null) {
			for (File dungeon : dungeons) {
				FileConfiguration loader = YamlConfiguration.loadConfiguration(dungeon);
				for (final String mobId : loader.getKeys(false)) {
					dungeonSpecificDrops.put(mobId, DungeonDrop.newDungeonSpecificDrop(this, loader.getConfigurationSection(mobId)));
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
		final ItemRepository repo =
				ItemRepository.newItemRepository(itemsFile, ItemLoader.newInstance(this, YamlConfiguration.loadConfiguration(itemsFile)));
		repositories.put(repo.getId(), repo);
	}

	private void loadCollectionRepositories() {
		final File collectionsFolder = new File(getDataFolder(), "collections");
		if (!collectionsFolder.exists()) {
			collectionsFolder.mkdirs();
		}
		final File[] collections = collectionsFolder.listFiles((x, y) -> x.exists() && x.isFile() && y.endsWith(".yml"));
		if (collections != null) {
			for (final File collection : collections) {
				final ItemRepository repo = ItemRepository.newItemRepository(collection,
				                                                             ItemLoader.newInstance(this,
				                                                                                    YamlConfiguration.loadConfiguration(
						                                                                                    collection)));
				repositories.put(repo.getId(), repo);
			}
		}
	}

	public List<Drop> getAvailableDropsForLevel(int level) {
		final ItemDropManager manager = dropManagers.computeIfAbsent("items", x -> ItemDropManager.newInstance(this, repositories.get(x)));
		return manager.getDropForLevel(level);
	}

	public List<Drop> getAvailableDrop(Entity entity) {
		// the entity must be a mythic mob if possible
		if (usesMythicMob) {
			final IMobsAPI api = MythicMobs.inst()
			                               .getAPI()
			                               .getMobAPI();
			if (!api.isMythicMob(entity)) {
				return Collections.emptyList();
			}
		}

		// the mob doesn't have a custom name, abort
		String mobName = entity.getCustomName();
		if (mobName == null) {
			return Collections.emptyList();
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
			final DungeonDrop dungeonDrop = this.dungeonSpecificDrops.get(mobId);
			if (dungeonDrop != null) {
				return dungeonDrop.getAvailableDrops();
			}
		}
		return Collections.emptyList();
	}

	public ItemRepository getRepository(String id) {
		return repositories.get(id);
	}
}
