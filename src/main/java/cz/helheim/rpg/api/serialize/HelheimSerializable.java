package cz.helheim.rpg.api.serialize;

import cz.helheim.rpg.api.exception.SerializationException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public interface HelheimSerializable extends ConfigurationSerializable {

	void deserialize(Map<String, Object> serialize) throws SerializationException;
}
