package cz.helheim.rpg.api.serialize;

import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jakub Šmrha
 * @version 1.0
 * @since 24.06.2022
 */
public class SerializeManager {

	private final Logger logger;

	public SerializeManager(HelheimPlugin plugin) {
		this.logger = plugin.getLogger();
	}

	public <T> Map<String, Object> serialize(T object) throws SerializationException {
		final Map<String, Object> map = new LinkedHashMap<>();
		final Class<?> clazz = object.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(Serialize.class)) {
				continue;
			}
			final Serialize annot = field.getAnnotation(Serialize.class);
			final String name = annot.value();
			if (name.isEmpty()) {
				throw new SerializationException("Missing name for field " + field.getName() + " in class " + clazz.getName());
			}
			try {
				map.put(name, field.get(object));
			} catch (ReflectiveOperationException e) {
				throw new SerializationException(e);
			}
		}
		return map;
	}

	public <T> void deserialize(T object, FileConfiguration file) throws SerializationException {
		deserialize(object, file.getValues(true));
	}

	public <T> void deserialize(T object, Map<String, Object> serialize) throws SerializationException {
		final Class<?> clazz = object.getClass();
		logger.log(Level.INFO, String.format("Deserializing '%s' (%s): %s", clazz.getSimpleName(), object, serialize));
		for (final Map.Entry<String, Object> entry : serialize.entrySet()) {
			if (entry.getValue() instanceof Map) {
				deserialize(object, (Map<String, Object>) entry.getValue());
				continue;
			}

			final String fieldName = entry.getKey();
			try {
				final Field field = clazz.getDeclaredField(fieldName);
				if (field.getType()
				         .isInstance(HelheimSerializable.class)) {
					((HelheimSerializable) object).deserialize(serialize);
				} else {
					field.set(object, entry.getValue());
				}
			} catch (NoSuchFieldException e) {
				logger.log(Level.WARNING, String.format("Invalid key '%s' -- it does not exist!", fieldName));
			} catch (ReflectiveOperationException e) {
				throw new SerializationException(e);
			}
		}
	}
}
