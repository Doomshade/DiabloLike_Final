package cz.helheim.rpg.command;

import cz.helheim.rpg.api.impls.AbstractSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class AddCommand extends AbstractSubCommand {

	public AddCommand() {
		super();
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
