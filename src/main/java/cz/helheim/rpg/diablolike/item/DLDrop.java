package cz.helheim.rpg.diablolike.item;

import cz.helheim.rpg.diablolike.api.item.BaseItem;
import cz.helheim.rpg.diablolike.api.item.DiabloItem;
import cz.helheim.rpg.diablolike.api.item.Drop;
import cz.helheim.rpg.diablolike.util.Range;

import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
class DLDrop implements Drop {

	private final Collection<BaseItem> drop = new LinkedList<>();
	private final Range amount;
	private final double dropChance;
	private final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>();
	private boolean empty;

	DLDrop(final Iterable<BaseItem> drop, final Range amount, final double dropChance,
		   final Map<DiabloItem.Tier, Double> rarityChances) {
		this(drop, amount, dropChance, rarityChances, false);
	}

	DLDrop(final Iterable<BaseItem> drop, final int amount, final double dropChance,
		   final Map<DiabloItem.Tier, Double> rarityChances) {
		this(drop, new Range(amount), dropChance, rarityChances);
	}

	private DLDrop(final Iterable<BaseItem> drop, final Range amount, final double dropChance,
				   final Map<DiabloItem.Tier, Double> rarityChances, final boolean empty) {
		notNull(drop);
		notNull(amount);
		notNull(rarityChances);

		drop.forEach(this.drop::add);
		this.amount = amount;
		this.dropChance = dropChance;
		this.rarityChances.putAll(rarityChances);
		this.empty = empty;
	}

	public DLDrop() {
		this(new ArrayList<>(), new Range(0), 0d, new HashMap<>(), true);
	}

	@Override
	public boolean isEmpty() {
		return empty;
	}

	@Override
	public void addItem(final BaseItem item) throws NullPointerException {
		notNull(item);
		drop.add(item);
		empty = false;
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
