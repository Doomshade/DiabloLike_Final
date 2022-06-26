package cz.helheim.rpg.api.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

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
}
