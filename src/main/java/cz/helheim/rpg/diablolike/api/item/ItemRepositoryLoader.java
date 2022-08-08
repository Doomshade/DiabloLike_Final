package cz.helheim.rpg.diablolike.api.item;

import cz.helheim.rpg.diablolike.util.Range;

import java.util.Map;
import java.util.Optional;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.06.2022
 */
public interface ItemRepositoryLoader {
	Optional<? extends BaseItem> getBaseItem(String id);

	Map<DiabloItem.Tier, Double> getRarityChances();

	double getDropChance();

	Range getAmount();
}
