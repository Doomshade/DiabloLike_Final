package cz.helheim.rpg.command;

import cz.helheim.rpg.api.impls.AbstractCommandHandler;
import cz.helheim.rpg.api.impls.HelheimPlugin;

public class DiabloLikeCommandHandler extends AbstractCommandHandler {


	public DiabloLikeCommandHandler(HelheimPlugin plugin) {
		super(plugin, "dl");
	}

	@Override
	public void registerSubCommands() {
		registerSubCommand("add", new AddCommand());
	}
}
