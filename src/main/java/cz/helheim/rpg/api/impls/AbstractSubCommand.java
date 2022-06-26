package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.command.SubCommand;
import cz.helheim.rpg.api.exception.SerializationException;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class AbstractSubCommand implements SubCommand {
    public static final String KEY_DESC = "description";
    public static final String KEY_USED_BY = "used-by";
    public static final String KEY_PERM = "permission";
    private static final Collection<String> keys = new HashSet<>();

    static {
        keys.add(KEY_DESC);
        keys.add(KEY_USED_BY);
        keys.add(KEY_PERM);
    }

    private final String description;
    private final int usedBy;
    private final String requiredPermission;

    public AbstractSubCommand() {
        this.description = "";
        this.usedBy = 0;
        this.requiredPermission = "";
    }

    public AbstractSubCommand(Map<String, Object> map) {
        if (!keys.equals(map.keySet())) {
            throw new IllegalArgumentException("Missing keys in the serialization! Required keys are: " + keys);
        }
        this.description = (String) map.get(KEY_DESC);
        this.usedBy = (int) map.get(KEY_USED_BY);
        this.requiredPermission = (String) map.get(KEY_PERM);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getRequiredPermission() {
        return requiredPermission;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int usedBy() {
        return usedBy;
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
