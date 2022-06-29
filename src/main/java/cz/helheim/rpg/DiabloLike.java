package cz.helheim.rpg;

import com.rit.sucy.EnchantmentAPI;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import cz.helheim.rpg.data.DiabloLikeSettings;
import cz.helheim.rpg.enchantment.Rychlostrelba;

public class DiabloLike extends HelheimPlugin {

	private static DiabloLike instance = null;

	public static DiabloLike getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		instance = null;
	}

	@Override
	public void onEnable() {
		instance = this;
		super.onEnable();
		EnchantmentAPI.registerCustomEnchantment(new Rychlostrelba());
		setSettings(new DiabloLikeSettings(this));
	}
}
