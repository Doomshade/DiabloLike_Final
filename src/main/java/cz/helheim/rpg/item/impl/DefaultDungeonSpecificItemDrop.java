package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.DungeonSpecificItemDrop;
import cz.helheim.rpg.util.Range;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public class DefaultDungeonSpecificItemDrop implements DungeonSpecificItemDrop {
	private final Range dropAmount;
	private final double chance;
	private final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>();

	public DefaultDungeonSpecificItemDrop(final Range dropAmount, final double chance, final Map<DiabloItem.Tier, Double> rarityChances) {
		this.dropAmount = dropAmount;
		this.chance = chance;
		this.rarityChances.putAll(rarityChances);
	}


	@Override
	public Range getDropAmount() {
		return dropAmount;
	}

	@Override
	public double getChance() {
		return chance;
	}

	@Override
	public Map<DiabloItem.Tier, Double> getRarityChances() {
		return Collections.unmodifiableMap(rarityChances);
	}
}
