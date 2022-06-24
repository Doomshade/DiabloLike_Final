package cz.helheim.rpg.api.impls;

import com.google.common.collect.ImmutableMap;
import com.sun.istack.internal.NotNull;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.command.SubCommand;
import cz.helheim.rpg.api.exception.SerializationException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractCommandHandler implements ICommandHandler {

	private final Logger logger;

	private final PluginCommand command;
	private final Map<String, SubCommand> subCommands = new TreeMap<>();
	private final HelheimPlugin plugin;

	protected AbstractCommandHandler(HelheimPlugin plugin, String command) {
		this.plugin = plugin;
		this.command = plugin.getCommand(command);
		this.logger = plugin.getLogger();
	}

	protected HelheimPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void register() {
		command.setExecutor(this);
	}

	@Override
	public void deserialize(final FileConfiguration file) throws SerializationException {
		for (final String key : file.getKeys(false)) {
			SubCommand subCommand = subCommands.get(key);
			if (subCommand == null) {
				logger.log(Level.WARNING, String.format("Invalid command '%s'.", key));
				continue;
			}
			subCommand.deserialize(file.getConfigurationSection(key)
			                           .getValues(true));
		}
	}

	@Override
	public void serialize(final FileConfiguration file) {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (args.length == 0) {
			String commandName = command.getName();
			for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
				sender.sendMessage(String.format("/%s %s - %s",
				                                 commandName,
				                                 entry.getKey(),
				                                 entry.getValue()
				                                      .getDescription()));
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
			tab.addAll(subCommands.keySet()
			                      .stream()
			                      .filter(x -> x.startsWith(subCommandName))
			                      .collect(Collectors.toList()));
		} else {
			tab.addAll(subCommand.onTabComplete(commandSender, Arrays.copyOfRange(args, 1, args.length)));
		}

		return tab.isEmpty() ? null : tab;
	}

	protected void registerSubCommand(@NotNull final String name, @NotNull final SubCommand subCommand) {
		subCommands.put(name.toLowerCase(), subCommand);
	}

	protected final Map<String, SubCommand> getSubCommands() {
		return ImmutableMap.copyOf(subCommands);
	}
}
