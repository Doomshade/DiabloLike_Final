package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.item.*;
import cz.helheim.rpg.util.Range;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public class DefaultItemDropManager implements ItemDropManager {

	private final HelheimPlugin plugin;

	private final Map<Integer, List<Drop>> drops = new HashMap<>();


	public DefaultItemDropManager(final HelheimPlugin plugin, final ItemRepository repository) {
		this.plugin = plugin;
		final FileConfiguration root = repository.getRoot();

		// yes we are iterating through the items twice, but this shouldn't really matter
		for (final BaseItem baseItem : repository) {
			final ItemLoader loader = ItemLoader.newInstance(plugin, root.getConfigurationSection(baseItem.getId()));
			if (baseItem instanceof DiabloItem) {
				final DiabloItem diabloItem = (DiabloItem) baseItem;
				repository.getItem(diabloItem.getId());
				drops.computeIfAbsent(diabloItem.getLevel(), k -> new ArrayList<>())
				     .add(Drop.newDrop(diabloItem, loader.getAmount(), loader.getDropChance(), loader.getRarityChances()));
			}

		}
	}

	@Override
	public Collection<ItemStack> getRandomLoot(final List<Drop> items, int limit) {
		final Collection<ItemStack> loot = new ArrayList<>();
		final Random random = new Random();
		final DiabloItem.Tier[] values = DiabloItem.Tier.values();
		Arrays.sort(values, (x, y) -> -Integer.compare(x.ordinal(), y.ordinal()));

		// shuffle for more randomness
		Collections.shuffle(items);
		for (final Drop drop : items) {
			final double dropChance = random.nextDouble() * 100;

			// it didn't roll :(
			if (dropChance >= drop.getDropChance()) {
				continue;
			}

			final BaseItem baseItem = drop.getDrop();
			final ItemStack itemStack = baseItem.getItemStack();

			// roll amount
			itemStack.setAmount(drop.getAmount()
			                        .randomValue());

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
				final Map<DiabloItem.Tier, Double> rarityChances = drop.getRarityChances();
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
			limit--;
			if (limit <= 0) {
				break;
			}
		}

		return loot;
	}

	@Override
	public List<String> selectAttributes(final DiabloItem diabloItem) {
		final DiabloLikeSettings settings = plugin.getSettings();

		// use LinkedList to remove items in O(1) as opposed to ArrayList in O(n)
		final List<String> attributes = new LinkedList<>(diabloItem.getAttributes());
		final double dividedBy = settings.getPocetDiv(getTier(diabloItem));

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
	public List<Drop> getDropForLevel(final int level) {
		final List<Drop> drops = new ArrayList<>();
		final DiabloLikeSettings settings = plugin.getSettings();
		final Range levelRange = settings.getDropRange()
		                                 .surround(level);
		for (int num : levelRange) {
			final List<? extends Drop> diabloItems = this.drops.get(num);
			if (diabloItems != null) {
				drops.addAll(diabloItems);
			}
		}
		return drops;
	}

	@Override
	public DiabloItem.Tier getTier(final DiabloItem diabloItem) throws IllegalArgumentException {
		final int ordinal = NBTTagManager.getInstance()
		                                 .getInteger(diabloItem, NBTKey.TIER);
		if (ordinal == Integer.MAX_VALUE) {
			throw new IllegalArgumentException("This diablo item has not yet been instantiated!");
		}
		return DiabloItem.Tier.values()[ordinal];
	}

	private void setTier(final DiabloItem.Tier tier, final DiabloItem diabloItem) {
		final ItemStack itemStack = diabloItem.getItemStack();

		// get the tier colour
		final DiabloLikeSettings settings = plugin.getSettings();
		String tierColour = settings.getTierColour(tier);

		// validate the tier colour in settings
		if (tierColour.isEmpty()) {
			plugin.getLogger()
			      .log(Level.INFO, "Missing tier colour for " + tier.name());
			return;
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
