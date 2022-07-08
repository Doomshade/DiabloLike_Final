package cz.helheim.rpg;

import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.enchantment.Rychlostrelba;
import cz.helheim.rpg.item.*;
import cz.helheim.rpg.item.impl.DefaultDrop;
import cz.helheim.rpg.listener.MobListener;
import cz.helheim.rpg.listener.MythicMobListener;
import cz.helheim.rpg.util.Pair;
import cz.helheim.rpg.util.Range;
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
import java.util.stream.Collectors;

public class DiabloLike extends HelheimPlugin {

	private static final Pattern levelPattern = Pattern.compile("\\[Lv\\. (?<lvl>\\d+)]");
	private static DiabloLike instance = null;
	private final Map<String, ItemRepository> repositories = new LinkedHashMap<>();
	private final Map<String, DungeonDrop> dungeonSpecificDrops = new HashMap<>();
	private ItemLoader itemLoader;

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
		this.itemLoader = ItemLoader.createInstance(this);
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
		final ItemRepository repo = ItemRepository.newItemRepository(this, itemsFile, itemLoader);
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
				final ItemRepository repo = ItemRepository.newItemRepository(this, collection, itemLoader);
				repositories.put(repo.getId(), repo);
			}
		}
	}

	public Drop getAvailableDropsForLevel(int level) {
		final Collection<DiabloItem> drops = new LinkedHashSet<>();
		for (final ItemRepository repository : repositories.values()) {
			drops.addAll(repository.getDropForLevel(level));
		}
		return Drop.newDrop(drops.stream()
		                         .map(x -> new Pair<>(x, new Range(1, 1)))
		                         .collect(Collectors.toSet()));
	}

	public Drop getAvailableDrop(Entity entity) {
		if (usesMythicMob) {
			final IMobsAPI api = MythicMobs.inst()
			                               .getAPI()
			                               .getMobAPI();
			if (!api.isMythicMob(entity)) {
				return Drop.newDrop();
			}
		}

		String mobName = entity.getCustomName();
		if (mobName == null) {
			return new DefaultDrop();
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
				dungeonDrop.getAvailableDropsForMob();
			}
		}
		return Drop.newDrop();
	}

	public ItemRepository getRepository(String id) {
		return repositories.get(id);
	}
}
