package cz.helheim.rpg.api.exception;

import java.io.IOException;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class SerializationException extends IOException {
    private final boolean pluginError;


    public SerializationException(final String message, boolean pluginError) {
        super(message);
        this.pluginError = pluginError;
    }

    public SerializationException(final String message, final Throwable cause, boolean pluginError) {
        super(message, cause);
        this.pluginError = pluginError;
    }

    public SerializationException(final Throwable cause, boolean pluginError) {
        super(cause);
        this.pluginError = pluginError;
    }

    public boolean isPluginError() {
        return pluginError;
    }

    @Override
    public String getLocalizedMessage() {
        String msg = super.getLocalizedMessage();
        if (pluginError) {
            msg += " This is a plugin error, please see the newest log file in the logs folder, and report the error to the plugin author.";
        }
        return msg;
    }
}
