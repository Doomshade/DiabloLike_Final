package cz.helheim.rpg.diablolike.command;

import cz.helheim.rpg.api.impls.AbstractCommandHandler;
import cz.helheim.rpg.api.impls.HelheimPlugin;

public class DiabloLikeCommandHandler extends AbstractCommandHandler {


	public DiabloLikeCommandHandler(HelheimPlugin plugin) {
		super(plugin, "dll");
	}

	@Override
	public void registerSubCommands() {
		registerSubCommand("add", new AddCommand());
	}
}
