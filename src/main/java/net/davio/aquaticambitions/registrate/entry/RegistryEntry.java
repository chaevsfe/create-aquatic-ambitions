package net.davio.aquaticambitions.registrate.entry;

import net.davio.aquaticambitions.registrate.fn.NonNullSupplier;
import net.minecraft.resources.Identifier;

public class RegistryEntry<R, T extends R> implements NonNullSupplier<T> {
    private final Identifier id;
    private final T value;

    public RegistryEntry(Identifier id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + "]";
    }
}
