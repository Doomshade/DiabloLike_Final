package cz.helheim.rpg.item;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 08.07.2022
 */
public enum NBTKey {
	DIABLO_ITEM("dl-diabloitem"),
	TIER("dl-tier"),
	SCROLL("dl-scroll"),
	ID("dl-id");

	private final String key;

	NBTKey(final String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
