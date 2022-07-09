package cz.helheim.rpg.item;

import cz.helheim.rpg.item.impl.DefaultDrop;
import cz.helheim.rpg.util.Range;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public interface Drop {
	static Drop newDrop(final BaseItem drop, final Range amount, final double dropChance,
	                    final Map<DiabloItem.Tier, Double> rarityChances) {
		return new DefaultDrop(drop, amount, dropChance, rarityChances);
	}

	BaseItem getDrop();

	Range getAmount();

	double getDropChance();

	Map<DiabloItem.Tier, Double> getRarityChances();
}
