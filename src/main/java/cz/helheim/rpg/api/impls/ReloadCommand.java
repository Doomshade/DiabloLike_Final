package cz.helheim.rpg.api.impls;

import org.bukkit.command.CommandSender;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public class ReloadCommand extends AbstractSubCommand {
	public ReloadCommand(final HelheimPlugin plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(final CommandSender sender, final String... args) {
		plugin.reload();
	}
}
