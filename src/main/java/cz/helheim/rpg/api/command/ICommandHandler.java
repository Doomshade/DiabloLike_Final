package cz.helheim.rpg.api.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public interface ICommandHandler extends CommandExecutor, TabCompleter {
    void registerSubCommands();

    void register();
}
