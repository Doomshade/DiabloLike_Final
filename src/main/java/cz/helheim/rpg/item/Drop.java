package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public interface Drop {
	BaseItem getDrop();

	Range getAmount();

	double getDropChance();

	Map<DiabloItem.Tier, Double> getRarityChances();
}
