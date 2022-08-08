package cz.helheim.rpg.diablolike.command;

import cz.helheim.rpg.diablolike.api.DiabloLike;
import cz.helheim.rpg.api.impls.AbstractSubCommand;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 26.07.2022
 */
public class ReloadCommand extends AbstractSubCommand {

	public ReloadCommand() {
		super();
	}

	public ReloadCommand(final Map<String, Object> map) {
		super(map);
	}
	
	@Override
	public void onCommand(final CommandSender sender, final String... args) {
		DiabloLike.getInstance()
		          .reload();
	}
}
