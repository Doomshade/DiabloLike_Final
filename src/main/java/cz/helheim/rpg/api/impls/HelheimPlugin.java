package cz.helheim.rpg.api.impls;

import cz.helheim.rpg.api.IHelheimPlugin;
import cz.helheim.rpg.api.command.ICommandHandler;
import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.io.IOManager;
import cz.helheim.rpg.api.io.PluginLogHandler;
import cz.helheim.rpg.api.serialize.SerializeManager;
import cz.helheim.rpg.command.DiabloLikeCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class HelheimPlugin extends JavaPlugin implements IHelheimPlugin {

    private IOManager io;
    private SerializeManager serializeManager;

    private ICommandHandler commandHandler;

    @Override
    public void onDisable() {
        save();
    }

    @Override
    public void onEnable() {
        register();
        load();
    }

    private void register() {
        this.io = new IOManager();
        this.commandHandler = new DiabloLikeCommandHandler(this);
        this.commandHandler.register();
        this.commandHandler.registerSubCommands();

        this.serializeManager = new SerializeManager(this);
    }

    private void load() {
        loadIO();
        loadLogger();
        loadCommands();
    }

    private void loadCommands() {
        try {
            this.serializeManager.deserialize(this.commandHandler, io.getCommandsFileConfiguration());
        } catch (SerializationException e) {
            getLogger().log(Level.SEVERE, "Failed to load commands!", e);
        }
    }

    private void loadIO() {
        try {
            io.init(this);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "An IO exception occurred, stopping plugin...", e);
            Bukkit.getPluginManager()
                  .disablePlugin(this);
        }
    }

    private void loadLogger() {
        final Logger logger = getLogger();
        try {
            logger.addHandler(new PluginLogHandler(io));
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "Failed to initialize the log handler for " + io.getLogFile()
                                                                    .getAbsolutePath(), e);
        }
    }

    private void save() {
        saveCommands();
    }

    private void saveCommands() {
        try {
            final FileConfiguration commandsFileConfiguration = this.io.getCommandsFileConfiguration();
            commandsFileConfiguration.addDefaults(this.serializeManager.serialize(this.commandHandler));
            commandsFileConfiguration.save(this.io.getCommandsFile());
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Failed to save commands!", e);
        }
    }

    @Override
    public IOManager getIOManager() {
        ensureEnabled();
        return io;
    }

    private void ensureEnabled() {
        if (!isEnabled()) {
            throw new IllegalStateException("Plugin is not yet enabled!");
        }
    }

    @Override
    public void reload() {
        save();
        load();
    }
}
