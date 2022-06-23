package cz.helheim.rpg.command;

import cz.helheim.rpg.api.impls.AbstractCommandHandler;
import cz.helheim.rpg.api.impls.AbstractSubCommand;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.command.CommandSender;

public class DiabloLikeCommandHandler extends AbstractCommandHandler {
    protected DiabloLikeCommandHandler(HelheimPlugin plugin) {
        super(plugin, "dl");
    }

    @Override
    public void registerSubCommands() {
        registerSubCommand(new AbstractSubCommand() {
            @Override
            public void onCommand(CommandSender sender, String... args) {

            }
        });
    }
}
