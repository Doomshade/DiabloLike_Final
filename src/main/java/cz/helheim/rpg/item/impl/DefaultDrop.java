package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.item.BaseItem;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.Drop;
import cz.helheim.rpg.util.Range;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public class DefaultDrop implements Drop {

	private final BaseItem drop;
	private final Range amount;
	private final double dropChance;
	private final Map<DiabloItem.Tier, Double> rarityChances = new HashMap<>();

	public DefaultDrop(final BaseItem drop, final Range amount, final double dropChance,
	                   final Map<DiabloItem.Tier, Double> rarityChances) {
		notNull(drop);
		notNull(amount);
		notNull(rarityChances);
		notEmpty(rarityChances);
		this.drop = drop;
		this.amount = amount;
		this.dropChance = dropChance;
		this.rarityChances.putAll(rarityChances);
	}

	@Override
	public BaseItem getDrop() {
		return drop;
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
}
