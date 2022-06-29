package cz.helheim.rpg.item;

/**
 * @author Doomshade
 * @version 1.0
 * @since 28.06.2022
 */
public interface ItemRepository {
	DiabloItem getDiabloItem(String id);

	Scroll getScroll(String id);
}
