package cz.helheim.rpg.item;

import cz.helheim.rpg.DiabloLike;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

import static cz.helheim.rpg.item.ItemInstantiationHelper.drop;
import static cz.helheim.rpg.item.ItemInstantiationHelper.emptyDrop;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
class DefaultDropManager implements DropManager {

	private static final double EPSILON = 0.001;
	private final DiabloLike plugin;

	private final Map<Integer, Iterable<BaseItem>> dropCache = new HashMap<>();


	DefaultDropManager(final DiabloLike plugin) {
		this.plugin = plugin;
	}

	@Override
	public Collection<ItemStack> getRandomLoot(final Drop drop) {
		final List<BaseItem> dropPool = new ArrayList<>();
		final Collection<ItemStack> loot = new ArrayList<>();
		final Random random = new Random();

		// sort the tiers in reverse order so the highest tiers go first
		final DiabloItem.Tier[] values = DiabloItem.Tier.values();
		Arrays.sort(values, Comparator.reverseOrder());
		assert values[values.length - 1] == DiabloItem.Tier.COMMON : "Common tier should be last, something wrong happened!";

		for (final BaseItem baseItem : drop) {
			final double roll = random.nextDouble() * 100;
			final double itemDropChance = baseItem.getDropChance();

			// the drop is either put to the loot, aka the drop is (nearly) 100%,
			// or it's put to the drop pool for further actions
			if (Math.abs(itemDropChance - 100d) < EPSILON) {
				// if the item has default properties assign it the drop properties
				// otherwise don't update it as those are custom attributes
				if (baseItem.hasDefaultProperties()) {
					baseItem.setDropChance(drop.getDropChance());
					if (baseItem instanceof DiabloItem) {
						DiabloItem diabloItem = (DiabloItem) baseItem;
						diabloItem.setRarities(drop.getRarityChances());
					}
				}
				addLoot(loot, random, values, baseItem);
			} else if (roll < itemDropChance) {
				dropPool.add(baseItem);
			}
		}

		// sort it in reverse order, aka the rarest items will be first
		dropPool.sort(Comparator.reverseOrder());
		int dropAmount = drop.getAmount()
		                     .randomValue();
		for (final BaseItem baseItem : dropPool) {
			addLoot(loot, random, values, baseItem);
			if (--dropAmount <= 0) {
				return loot;
			}
		}

		return loot;
	}

	@Override
	public List<String> selectAttributes(final Identifiable identifiable) {
		if (!hasTier(identifiable)) {
			return new ArrayList<>();
		}
		final DiabloLikeSettings settings = plugin.getSettings();
		final DiabloItem.Tier tier = getTier(identifiable);
		final double dividedBy = settings.getPocetDiv(tier);
		if (dividedBy <= 0.0) {
			plugin.getLogger()
			      .log(Level.INFO, "Invalid division for {} tier!", tier);
			return new ArrayList<>();
		}
		// use LinkedList to remove items in O(1) as opposed to ArrayList in O(n)
		final List<String> attributes = new LinkedList<>(identifiable.getLoreParser()
		                                                             .getAttributes());
		int count = (int) Math.ceil(attributes.size() / dividedBy);
		final Random random = new Random();

		// invert it to the count that needs to be removed from the pool
		// e.g. attributes size = 4, count = 3 -> count = 1 (aka we need to remove 1 from the attributes to match the count)
		count = attributes.size() - count;
		while (count-- > 0) {
			attributes.remove(random.nextInt(attributes.size()));
		}
		return new ArrayList<>(attributes);
	}

	@Override
	public Drop getAvailableDropForLevel(final int level) {
		if (dropCache.isEmpty()) {
			initializeDrops(plugin.getMainItemRepository());
			if (dropCache.isEmpty()) {
				plugin.getLogger()
				      .log(Level.CONFIG, "No available drops for level {}", level);
				return emptyDrop();
			}
		}


		final List<BaseItem> drops = new ArrayList<>();
		final DiabloLikeSettings settings = plugin.getSettings();
		final Range levelRange = settings.getDropRange()
		                                 .surround(level);
		for (int num : levelRange) {
			final Iterable<? extends BaseItem> items = this.dropCache.get(num);
			if (items != null) {
				for (BaseItem baseItem : items) {
					drops.add(baseItem);
				}
			}
		}
		return drop(drops, 1, settings.getDropChance(), settings.getRarityChances());
	}

	private boolean hasTier(final BaseItem diabloItem) {
		return NBTTagManager.getInstance()
		                    .getInteger(diabloItem, NBTKey.TIER) != DiabloItem.Tier.UNKNOWN_TIER;
	}

	private DiabloItem.Tier getTier(final BaseItem diabloItem) throws IllegalArgumentException {
		final int ordinal = NBTTagManager.getInstance()
		                                 .getInteger(diabloItem, NBTKey.TIER);
		assert ordinal != DiabloItem.Tier.UNKNOWN_TIER : "Item does not have a tier, forgot to check with hasTier()";
		return DiabloItem.Tier.values()[ordinal];
	}

	private void addLoot(final Collection<ItemStack> loot, final Random random, final DiabloItem.Tier[] values, final BaseItem baseItem) {
		final ItemStack itemStack = baseItem.getItemStack();

		if (baseItem instanceof DiabloItem) {
			final DiabloItem diabloItem = (DiabloItem) baseItem;

			// roll rarity
			// a number is rolled. the number decreases with each iterated tier
			// until it reaches <= 0
			// the tiers are in descending order, i.e. common tier is the last one to occur
			// this is to ensure items with rarity rolls such as "rare = 100"
			// those items can't go lower than rare
			double tierRoll = random.nextDouble() * 100;
			DiabloItem.Tier rolledTier = DiabloItem.Tier.COMMON;
			final Map<DiabloItem.Tier, Double> rarityChances = diabloItem.getRarities();
			for (final DiabloItem.Tier tier : values) {
				tierRoll -= rarityChances.getOrDefault(tier, 0d);
				if (tierRoll <= 0) {
					rolledTier = tier;
					break;
				}
			}
			setTier(rolledTier, diabloItem);
		}
		loot.add(itemStack);
	}

	private void initializeDrops(final ItemRepository repository) {
		final FileConfiguration root = repository.getRoot();
		final ItemRepositoryLoader loader = plugin.getRepositoryLoader(repository);
		final Drop drop = drop(new ArrayList<>(), loader.getAmount(), loader.getDropChance(), loader.getRarityChances());

		// yes we are iterating through the items twice, but this shouldn't really matter
		for (final BaseItem baseItem : repository) {
			if (baseItem instanceof DiabloItem) {
				final DiabloItem diabloItem = (DiabloItem) baseItem;
				final ArrayList<BaseItem> baseItems =
						(ArrayList<BaseItem>) dropCache.computeIfAbsent(diabloItem.getLevel(), k -> new ArrayList<>());
				baseItems.add(baseItem);
			}

		}
	}

	private void setTier(final DiabloItem.Tier tier, final DiabloItem diabloItem) {
		final ItemStack itemStack = diabloItem.getItemStack();

		// get the tier colour
		final DiabloLikeSettings settings = plugin.getSettings();
		String tierColour = settings.getTierColour(tier);

		// validate the tier colour in settings
		if (tierColour.isEmpty()) {
			plugin.getLogger()
			      .log(Level.INFO, "Missing tier colour for {}", tier.name());
			tierColour = "&f";
		}
		tierColour = ChatColor.translateAlternateColorCodes('&', tierColour);

		// add a NBT tag for the tier
		NBTTagManager.getInstance()
		             .addNBTTag(diabloItem, NBTKey.TIER, (item, key) -> item.setInteger(key, tier.ordinal()));

		// prefix it with the colour
		final ItemMeta meta = itemStack.getItemMeta();
		String displayName = meta.getDisplayName();

		// if the name starts with a colour replace it, otherwise just add the prefix
		if (displayName.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
			displayName = tierColour.concat(displayName.substring(2));
		} else {
			displayName = tierColour.concat(displayName);
		}
		meta.setDisplayName(displayName);
	}
}
