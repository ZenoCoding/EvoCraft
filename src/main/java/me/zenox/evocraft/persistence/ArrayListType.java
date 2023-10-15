package me.zenox.evocraft.persistence;

import me.zenox.evocraft.util.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * ArrayList implementation of PersistentDataType
 *
 * @param <T> The type of ArrayList object
 */
public class ArrayListType<T> implements PersistentDataType<byte[], ArrayList<T>> {
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<ArrayList<T>> getComplexType() {
        return ((Class<ArrayList<T>>) ((Class<?>) ArrayList.class));
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull ArrayList<T> complex, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @NotNull
    @Override
    public ArrayList<T> fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return (ArrayList<T>) SerializationUtils.deserialize(primitive);
    }
}
