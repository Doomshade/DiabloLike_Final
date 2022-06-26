package cz.helheim.rpg;

import cz.helheim.rpg.api.impls.HelheimPlugin;

public class DiabloLike extends HelheimPlugin {

    private static DiabloLike instance = null;

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;
    }

    public static DiabloLike getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
    }
}
