package me.zenox.superitems.persistence;

import me.zenox.superitems.item.ComplexItem;
import me.zenox.superitems.util.Util;
import me.zenox.superitems.util.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public class SerializedPersistentType<T extends Serializable> implements PersistentDataType<byte[], T> {
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<T> getComplexType() {
        return ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @NotNull
    @Override
    public T fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return (T) SerializationUtils.deserialize(primitive);
    }
}
