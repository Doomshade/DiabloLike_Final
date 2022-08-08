package cz.helheim.rpg.diablolike.command;

import cz.helheim.rpg.api.impls.AbstractSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Map;

import static cz.helheim.rpg.api.command.SubCommand.ValidSender.CONSOLE;
import static cz.helheim.rpg.api.command.SubCommand.ValidSender.PLAYER;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class AddCommand extends AbstractSubCommand {

	public AddCommand() {
		setDescription("Přidá předmět do inventáře");
		setValidSenders(PLAYER, CONSOLE);
		setRequiredPermission("dl.helper");
	}

	public AddCommand(final Map<String, Object> map) {
		super(map);
	}

	@Override
	public void onCommand(final CommandSender sender, final String... args) {
		// TODO
		throw new UnsupportedOperationException();
	}


}
