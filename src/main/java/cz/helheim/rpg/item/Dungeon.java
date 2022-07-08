package cz.helheim.rpg.item;

import java.util.Collection;

/**
 * @author Doomshade
 * @version 1.0
 * @since 03.07.2022
 */
public interface Dungeon {
	Collection<DungeonItemDrop> getDungeonDrops();
}
