package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.command.SubCommand;
import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.serialize.Serialize;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractSubCommand implements SubCommand {
	protected final HelheimPlugin plugin;
	private final Collection<String> requiredPermissions = new ArrayList<>();
	@Serialize("description")
	private String description = "";

	public AbstractSubCommand(HelheimPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public Collection<String> getRequiredPermissions() {
		return requiredPermissions;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Map<String, Object> serialize() {
		// TODO
		return null;
	}

	public void deserialize(Map<String, Object> map) throws SerializationException {
		// TODO
	}
}
