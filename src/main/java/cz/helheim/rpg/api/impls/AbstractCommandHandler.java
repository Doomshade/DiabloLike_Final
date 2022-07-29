package cz.helheim.rpg.api.impls;

import com.google.common.collect.ImmutableMap;
import com.sun.istack.internal.NotNull;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommandHandler implements ICommandHandler {
	private final PluginCommand command;
	private final Map<String, SubCommand> subCommands = new TreeMap<>();
	private final HelheimPlugin plugin;

	protected AbstractCommandHandler(HelheimPlugin plugin, String command) {
		this.plugin = plugin;
		this.command = plugin.getCommand(command);
	}

	protected HelheimPlugin getPlugin() {
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		final String commandName = command.getName();

		// no args means we print all the commands
		if (args.length == 0) {
			printCommands(sender, commandName);
			return true;
		}

		final SubCommand subCommand = subCommands.get(commandName);
		// unknown command, print the options
		if (subCommand == null) {
			printCommands(sender, commandName);
			return true;
		}

		// check for permissions and whether the sender can actually call the command
		int usedBy = sender instanceof Player ? SubCommand.PLAYER : SubCommand.CONSOLE;
		if ((usedBy & subCommand.usedBy()) == 0) {
			printCommands(sender, commandName);
			return true;
		}
		if (!sender.hasPermission(subCommand.getRequiredPermission())) {
			printCommands(sender, commandName);
			return true;
		}

		final String[] subCommandArgs = new String[args.length - 1];
		System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
		subCommand.onCommand(sender, subCommandArgs);
		return true;
	}

	private void printCommands(final CommandSender sender, final String commandName) {
		// TODO make this prettier
		for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
			sender.sendMessage(String.format("/%s %s - %s",
			                                 commandName,
			                                 entry.getKey(),
			                                 entry.getValue()
			                                      .getDescription()));
		}
	}

	@Override
	public void register() {
		command.setExecutor(this);
	}

	@Override
	public void saveCommands(final FileConfiguration loader) {
		subCommands.forEach(loader::set);
	}

	@Override
	public void loadCommands(final FileConfiguration loader) {
		loader.getKeys(false)
		      .forEach(x -> subCommands.put(x, (SubCommand) loader.get(x)));
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

		// the sub-command does not exist, highlight all commands starting with the given prefix
		if (subCommand == null) {
			tab.addAll(subCommands.keySet()
			                      .stream()
			                      .filter(x -> x.startsWith(subCommandName))
			                      .collect(Collectors.toList()));
		} else {
			// the command does exist, delegate the highlight to the sub-command
			// don't use addAll as the delegate could return null
			// returning null means it highlights players by default
			tab = subCommand.onTabComplete(commandSender, Arrays.copyOfRange(args, 1, args.length));
		}

		return tab;
	}

	protected void registerSubCommand(@NotNull final String name, @NotNull final SubCommand subCommand) {
		subCommands.put(name.toLowerCase(), subCommand);
	}

	protected final Map<String, SubCommand> getSubCommands() {
		return ImmutableMap.copyOf(subCommands);
	}
}
