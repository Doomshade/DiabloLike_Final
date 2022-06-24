package cz.helheim.rpg.api.command;

import cz.helheim.rpg.api.exception.SerializationException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * A command handler that registers sub commands
 *
 * @see SubCommand
 */
public interface ICommandHandler extends CommandExecutor, TabCompleter {

	/**
	 * Registers {@link SubCommand}s
	 */
	void registerSubCommands();

	/**
	 * Registers this command handler as the {@link CommandExecutor}.
	 */
	void register();

	void deserialize(FileConfiguration file) throws SerializationException;

	void serialize(FileConfiguration file);
}
