package cz.helheim.rpg.api.serialize;

import cz.helheim.rpg.api.exception.SerializationException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public interface HelheimSerializable extends ConfigurationSerializable {

    /**
     * Deserializes this object. This method should not be called by anything but {@link SerializeManager}!
     *
     * @param serialize the serialized version
     *
     * @throws SerializationException if the deserialization fails for whatever reason
     */
    void deserialize(Map<String, Object> serialize) throws SerializationException;
}
