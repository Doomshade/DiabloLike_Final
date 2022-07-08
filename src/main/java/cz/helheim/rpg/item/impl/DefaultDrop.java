package cz.helheim.rpg.item.impl;

import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.Drop;
import cz.helheim.rpg.util.Pair;
import cz.helheim.rpg.util.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 09.07.2022
 */
public class DefaultDrop implements Drop {
	private final Collection<Pair<DiabloItem, Range>> drop = new LinkedHashSet<>();

	public DefaultDrop(Collection<Pair<DiabloItem, Range>> drop) {
		this.drop.addAll(drop);
	}

	public DefaultDrop() {

	}

	@Override
	public Collection<Pair<DiabloItem, Range>> getAvailableDrop() {
		return drop;
	}
}
