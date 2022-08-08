package cz.helheim.rpg.diablolike.item;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 08.07.2022
 */
public enum NBTKey {
	TIER("dl-tier"),
	DL_TYPE("dl-type"),
	ID("dl-id");

	private final String key;

	NBTKey(final String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
