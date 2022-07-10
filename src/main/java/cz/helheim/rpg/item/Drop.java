package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public interface Drop extends Comparable<Drop>, Iterable<BaseItem> {
	static Drop newDrop(final Iterable<BaseItem> drop, final Range amount, final double dropChance,
	                    final Map<DiabloItem.Tier, Double> rarityChances) {
		return new DefaultDrop(drop, amount, dropChance, rarityChances);
	}

	static Drop emptyDrop() {
		return new DefaultDrop();
	}

	void addItem(BaseItem item) throws IllegalStateException;

	Range getAmount();

	double getDropChance();

	Map<DiabloItem.Tier, Double> getRarityChances();
}
