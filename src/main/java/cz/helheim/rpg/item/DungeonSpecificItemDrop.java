package cz.helheim.rpg.item;

import cz.helheim.rpg.util.Range;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 03.07.2022
 */
public interface DungeonSpecificItemDrop {
	Range getDropAmount();

	double getChance();

	Map<DiabloItem.Tier, Double> getRarityChances();
}
