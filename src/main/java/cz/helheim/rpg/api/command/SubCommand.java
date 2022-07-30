package cz.helheim.rpg.api.command;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * <p>A subcommand implementation.</p>
 * <p>Usage: {@code /{command} {subcommand} {args}}</p>
 *
 * @see ICommandHandler
 */
public interface SubCommand extends ConfigurationSerializable {

	/**
	 * @param sender the sender of this subcommand
	 * @param args   the arguments of this subcommand
	 */
	void onCommand(CommandSender sender, String... args);

	String toString(String command, String subCommand);

	void setLogger(Logger logger);

	List<String> getRequiredArgs();

	List<String> getOptionalArgs();

	/**
	 * @param sender the sender of the tab-complete
	 * @param args   the args of the tab-complete
	 *
	 * @return {@code null} for the current online player list or a list of tab-completions
	 */
	@Nullable
	List<String> onTabComplete(CommandSender sender, String... args);

	/**
	 * @return the required permissions to perform this subcommand
	 */
	@NotNull
	String getRequiredPermission();

	/**
	 * @return the description of this command
	 */
	@NotNull
	String getDescription();

	/**
	 * @return the flags of senders who can use this subcommand
	 *
	 * @see ValidSender#PLAYER
	 * @see ValidSender#CONSOLE
	 */
	ValidSender[] validSenders();

	boolean isValidSender(CommandSender sender);

	enum ValidSender {
		PLAYER(x -> x instanceof Player),
		CONSOLE(x -> x instanceof ConsoleCommandSender);

		private final Predicate<CommandSender> senderValidator;

		ValidSender(final Predicate<CommandSender> senderValidator) {
			this.senderValidator = senderValidator;
		}

		public boolean isValid(CommandSender sender) {
			return senderValidator.test(sender);
		}
	}
}
