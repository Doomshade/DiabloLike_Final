package cz.helheim.rpg.api.command;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.serialize.HelheimSerializable;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

/**
 * <p>A subcommand implementation.</p>
 * <p>Usage: {@code /{command} {subcommand} {args}}</p>
 *
 * @see ICommandHandler
 */
public interface SubCommand extends HelheimSerializable {
	void onCommand(CommandSender sender, String... args);

	@Nullable
	List<String> onTabComplete(CommandSender sender, String... args);

	@NotNull
	Iterable<String> getRequiredPermissions();

	@NotNull
	String getDescription();

	@Override
	Map<String, Object> serialize();

	void deserialize(Map<String, Object> map) throws SerializationException;
}
