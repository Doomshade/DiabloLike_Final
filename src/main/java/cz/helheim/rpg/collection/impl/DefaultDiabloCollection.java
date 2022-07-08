package cz.helheim.rpg.collection.impl;

import cz.helheim.rpg.collection.DiabloCollection;
import cz.helheim.rpg.item.DiabloItem;
import cz.helheim.rpg.item.ItemRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public class DefaultDiabloCollection implements DiabloCollection {

	private final Collection<DiabloItem> diabloItemCollection = new ArrayList<>();

	public DefaultDiabloCollection(final Collection<DiabloItem> diabloItemCollection) {
		this.diabloItemCollection.addAll(diabloItemCollection);
	}

	public DefaultDiabloCollection(final ItemRepository repository) {
	}

	@Override
	public Collection<DiabloItem> getDiabloItems() {
		return Collections.unmodifiableCollection(diabloItemCollection);
	}
}
