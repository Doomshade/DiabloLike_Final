package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractSubCommand implements SubCommand {
	public static final String KEY_DESC = "description";
	public static final String KEY_USED_BY = "used-by";
	public static final String KEY_PERM = "permission";
	public static final String KEY_CMD_ARGS = "args";
	public static final String KEY_CMD_ARGS_OPT = "optional-args";
	private static final Collection<String> keys = new HashSet<>();

	static {
		keys.add(KEY_DESC);
		keys.add(KEY_USED_BY);
		keys.add(KEY_PERM);
		keys.add(KEY_CMD_ARGS);
		keys.add(KEY_CMD_ARGS_OPT);
	}

	private final List<String> args = new ArrayList<>();
	private final List<String> optionalArgs = new ArrayList<>();
	private String description;
	private ValidSender[] validSenders;
	private String requiredPermission;
	private Optional<Logger> logger = Optional.empty();

	public AbstractSubCommand() {
		this.description = "";
		this.validSenders = new ValidSender[0];
		this.requiredPermission = "";
	}

	public AbstractSubCommand(Map<String, Object> map) {
		if (!map.keySet()
		        .containsAll(keys)) {
			throw new IllegalArgumentException(String.format("Missing keys in the serialization! Required keys are: %s, received: %s",
			                                                 keys,
			                                                 map.keySet()));
		}
		this.description = (String) map.get(KEY_DESC);
		this.validSenders = ((List<String>) map.get(KEY_USED_BY)).stream()
		                                                         .map(x -> {
			                                                         try {
				                                                         return ValidSender.valueOf(x);
			                                                         } catch (IllegalArgumentException e) {
				                                                         logger.ifPresent(lg -> lg.log(Level.WARNING,
				                                                                                       "Invalid sender {0}. Valid " +
				                                                                                       "senders" +
				                                                                                       " " +
				                                                                                       "are: {1}",
				                                                                                       new Object[] {
						                                                                                       x,
						                                                                                       Arrays.toString(ValidSender.values())
				                                                                                       }));
				                                                         throw new RuntimeException(e);
			                                                         }
		                                                         })
		                                                         .toArray(ValidSender[]::new);

		this.requiredPermission = (String) map.get(KEY_PERM);
		this.args.addAll((List<String>) map.get(KEY_CMD_ARGS));
		this.optionalArgs.addAll((List<String>) map.get(KEY_CMD_ARGS_OPT));
	}

	@Override
	public String toString(final String command, final String subCommand) {
		return String.format("%s/%s %s - %s%s", ChatColor.DARK_AQUA, command, subCommand, ChatColor.GOLD, getDescription());
	}

	@Override
	public void setLogger(final Logger logger) {
		this.logger = Optional.of(logger);
	}

	@Override
	public List<String> getRequiredArgs() {
		return args;
	}

	@Override
	public List<String> getOptionalArgs() {
		return optionalArgs;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getRequiredPermission() {
		return requiredPermission;
	}

	protected void setRequiredPermission(final String requiredPermission) {
		this.requiredPermission = requiredPermission;
	}

	@Override
	public String getDescription() {
		return description;
	}

	protected void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public ValidSender[] validSenders() {
		return validSenders;
	}

	@Override
	public boolean isValidSender(final CommandSender sender) {
		for (ValidSender validSender : validSenders) {
			if (validSender.isValid(sender)) {
				return sender.hasPermission(getRequiredPermission());
			}
		}
		return false;
	}

	protected void setOptionalArgs(String... args) {
		setArgs(this.optionalArgs, args);
	}

	protected void setArgs(String... args) {
		setArgs(this.args, args);
	}

	private void setArgs(List<String> list, String... args) {
		list.clear();
		list.addAll(Arrays.asList(args));
	}

	protected void setValidSenders(final ValidSender... validSenders) {
		this.validSenders = validSenders;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(KEY_DESC, description);
		map.put(KEY_PERM, requiredPermission);
		map.put(KEY_USED_BY,
		        Arrays.stream(validSenders)
		              .map(Enum::name)
		              .collect(Collectors.toList()));
		map.put(KEY_CMD_ARGS, args);
		map.put(KEY_CMD_ARGS_OPT, optionalArgs);
		return map;
	}
}
