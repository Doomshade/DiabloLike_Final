package cz.helheim.rpg.item;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;

import java.util.function.BiConsumer;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 08.07.2022
 */
public class NBTTagManager {
	private static final NBTTagManager instance = new NBTTagManager();

	private NBTTagManager() {
	}

	public static NBTTagManager getInstance() {
		return instance;
	}

	public void addNBTTag(BaseItem item, NBTKey key, BiConsumer<NBTItem, String> applyFunction) {
		applyFunction.accept(getNbtItem(item), key.getKey());
	}

	private NBTItem getNbtItem(final BaseItem item) {
		return new NBTItem(item.getItemStack(), true);
	}

	public String getString(BaseItem item, NBTKey key) {
		return getNbtItem(item).getString(key.getKey());
	}


	public int getInteger(BaseItem item, NBTKey key) {
		return getNbtItem(item).getInteger(key.getKey());
	}


	public boolean getBoolean(BaseItem item, NBTKey key) {
		return getNbtItem(item).getBoolean(key.getKey());
	}


	public double getDouble(BaseItem item, NBTKey key) {
		return getNbtItem(item).getDouble(key.getKey());
	}


	public NBTList<String> getStringList(BaseItem item, NBTKey key) {
		return getNbtItem(item).getStringList(key.getKey());
	}

	public NBTList<Integer> getIntegerList(BaseItem item, NBTKey key) {
		return getNbtItem(item).getIntegerList(key.getKey());
	}
}
