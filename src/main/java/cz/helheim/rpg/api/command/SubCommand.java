package cz.helheim.rpg.api.command;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;

/**
 * <p>A subcommand implementation.</p>
 * <p>Usage: {@code /{command} {subcommand} {args}}</p>
 *
 * @see ICommandHandler
 */
public interface SubCommand extends ConfigurationSerializable {
    int PLAYER = 1;
    int CONSOLE = 1 << 1;

    /**
     * @param sender the sender of this subcommand
     * @param args   the arguments of this subcommand
     */
    void onCommand(CommandSender sender, String... args);

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
     * @return the bitset of senders who can use this subcommand
     *
     * @see SubCommand#PLAYER
     * @see SubCommand#CONSOLE
     */
    int usedBy();
}
