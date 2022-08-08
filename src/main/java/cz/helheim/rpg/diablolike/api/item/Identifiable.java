package cz.helheim.rpg.diablolike.api.item;

import cz.helheim.rpg.diablolike.item.LoreParser;

import java.util.List;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 27.07.2022
 */
public interface Identifiable extends BaseItem {

	int getLevel();

	List<String> getOriginalLore();

	default LoreParser getLoreParser() {
		return new LoreParser(getOriginalLore());
	}

	boolean isIdentified();

	void setIdentified(List<String> newLore, boolean identified);
}
