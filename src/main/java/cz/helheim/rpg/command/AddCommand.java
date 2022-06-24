package cz.helheim.rpg.command;

import cz.helheim.rpg.api.impls.AbstractSubCommand;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.command.CommandSender;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public class AddCommand extends AbstractSubCommand {
	public AddCommand(final HelheimPlugin plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(final CommandSender sender, final String... args) {

	}
}
