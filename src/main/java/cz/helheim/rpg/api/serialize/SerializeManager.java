package cz.helheim.rpg.api.serialize;

import com.sun.istack.internal.NotNull;
import cz.helheim.rpg.api.exception.SerializationException;
import cz.helheim.rpg.api.impls.HelheimPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Doomshade
 * @version 1.0
 * @since 24.06.2022
 */
public class SerializeManager {

    private final Logger logger;

    public SerializeManager(@NotNull final HelheimPlugin plugin) {
        this.logger = plugin.getLogger();
    }

    @Deprecated
    public <T> Map<String, Object> serialize(@NotNull final T object) throws SerializationException {
        final Map<String, Object> map = new LinkedHashMap<>();
        final Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            // skip transient fields
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
                continue;
            }
            try {
                map.put(field.getName(), field.get(object));
            } catch (ReflectiveOperationException e) {
                throw new SerializationException(e, true);
            }
        }
        return map;
    }

    @Deprecated
    public <T> void deserialize(@NotNull final T object, @NotNull final FileConfiguration file) throws SerializationException {
        deserialize(object, file.getValues(true));
    }

    @Deprecated
    public <T> void deserialize(@NotNull final T object, @NotNull final Map<String, Object> serialize) throws SerializationException {
        final Class<?> clazz = object.getClass();
        logger.log(Level.INFO, String.format("Deserializing '%s' (%s): %s", clazz.getSimpleName(), object, serialize));
        // TODO this needs testing
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
            } catch (NoSuchFieldException ignored) {
            } catch (ReflectiveOperationException e) {
                throw new SerializationException(e, true);
            }
        }
    }
}
