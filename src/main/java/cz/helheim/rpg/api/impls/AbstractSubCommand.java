package cz.helheim.rpg.api.impls;

import com.sun.istack.internal.NotNull;
import cz.helheim.rpg.api.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class AbstractSubCommand implements SubCommand {
    private final Collection<String> requiredPermissions = new ArrayList<>();

    @Override
    public int compareTo(SubCommand o) {
        return 0;
    }

    protected AbstractSubCommand addRequiredPermission(@NotNull final String permission) {
        this.requiredPermissions.add(permission);
        return this;
    }

    protected AbstractSubCommand addPermissions(@NotNull final String... permissions) {
        this.requiredPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    protected AbstractSubCommand addPermission(@NotNull final Iterable<String> permissions) {
        for (String perm : permissions) {
            this.requiredPermissions.add(perm);
        }
        return this;
    }

    @Override
    public Collection<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String... args) {
        return null;
    }
}
