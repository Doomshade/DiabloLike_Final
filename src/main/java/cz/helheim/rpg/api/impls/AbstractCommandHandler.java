package cz.helheim.rpg.api.impls;

import com.sun.istack.internal.NotNull;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommandHandler implements ICommandHandler {
    private final PluginCommand command;
    private final Map<String, SubCommand> subCommands = new TreeMap<>();

    protected AbstractCommandHandler(HelheimPlugin plugin, String command) {
        this.command = plugin.getCommand(command);
    }

    @Override
    public void register() {
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            String commandName = command.getName();
            for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
                sender.sendMessage(String.format("/%s %s - %s", commandName, entry.getKey(), entry.getValue().getDescription()));
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length == 0) {
            tab.addAll(subCommands.keySet());
            return tab;
        }
        final String subCommandName = args[0].toLowerCase();
        final SubCommand subCommand = subCommands.get(subCommandName);
        if (subCommand == null) {
            tab.addAll(subCommands.keySet().stream().filter(x -> x.startsWith(subCommandName)).collect(Collectors.toList()));
        } else {
            tab.addAll(subCommand.onTabComplete(commandSender, Arrays.copyOfRange(args, 1, args.length)));
        }

        return tab.isEmpty() ? null : tab;
    }

    protected void registerSubCommand(@NotNull final String name, @NotNull final SubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }
}
