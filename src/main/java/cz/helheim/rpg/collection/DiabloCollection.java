package cz.helheim.rpg.collection;

import cz.helheim.rpg.item.DiabloItem;

import java.util.Collection;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 28.06.2022
 */
public interface DiabloCollection {
	/**
	 * @return the collection of {@link DiabloItem}s
	 */
	Collection<DiabloItem> getDiabloItems();
}
