package cz.helheim.rpg.api.command;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand extends Comparable<SubCommand> {
    void onCommand(CommandSender sender, String... args);

    @Nullable
    List<String> onTabComplete(CommandSender sender, String... args);

    @NotNull
    Iterable<String> getRequiredPermissions();

    String getDescription();
}
