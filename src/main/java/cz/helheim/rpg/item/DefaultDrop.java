package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
class DefaultDrop implements Drop {

	private final Iterable<BaseItem> drop;
	private final Range amount;
	private final double dropChance;
	private final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>();

	public DefaultDrop(final Iterable<BaseItem> drop, final Range amount, final double dropChance,
	                   final Map<DiabloItem.Tier, Double> rarityChances) {
		notNull(drop);
		notNull(amount);
		notNull(rarityChances);
		this.drop = drop;
		this.amount = amount;
		this.dropChance = dropChance;
		this.rarityChances.putAll(rarityChances);
	}

	public DefaultDrop() {
		this(new ArrayList<>(), new Range(0), 0d, new HashMap<>());
	}

	@Override
	public void addItem(final BaseItem item) throws IllegalStateException {
		if (!(drop instanceof Collection)) {
			throw new IllegalStateException();
		}
		((Collection<BaseItem>) drop).add(item);
	}

	@Override
	public Range getAmount() {
		return amount;
	}

	@Override
	public double getDropChance() {
		return dropChance;
	}

	@Override
	public Map<DiabloItem.Tier, Double> getRarityChances() {
		return rarityChances;
	}

	@Override
	public int compareTo(final Drop o) {
		return Double.compare(getDropChance(), o.getDropChance());
	}

	@Override
	public Iterator<BaseItem> iterator() {
		return drop.iterator();
	}
}
