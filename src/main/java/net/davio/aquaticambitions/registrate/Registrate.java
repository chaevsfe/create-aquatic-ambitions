package net.davio.aquaticambitions.registrate;

import net.davio.aquaticambitions.registrate.builders.BlockBuilder;
import net.davio.aquaticambitions.registrate.builders.BlockEntityBuilder;
import net.davio.aquaticambitions.registrate.builders.ItemBuilder;
import net.davio.aquaticambitions.registrate.entry.RegistryEntry;
import net.davio.aquaticambitions.registrate.fn.NonNullFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Registrate {
    private final String modid;
    private final Map<ResourceKey<? extends Registry<?>>, List<RegistryEntry<?, ?>>> tracked = new LinkedHashMap<>();

    protected Registrate(String modid) {
        this.modid = modid;
    }

    public static Registrate create(String modid) {
        return new Registrate(modid);
    }

    public String getModid() {
        return modid;
    }

    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modid, path);
    }

    public <T extends Block> BlockBuilder<T, Registrate> block(String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<>(this, this, name, factory);
    }

    public <T extends Item> ItemBuilder<T, Registrate> item(String name, NonNullFunction<Item.Properties, T> factory) {
        return new ItemBuilder<>(this, this, name, factory);
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, Registrate> blockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return new BlockEntityBuilder<>(this, this, name, factory);
    }

    public void track(ResourceKey<? extends Registry<?>> registry, RegistryEntry<?, ?> entry) {
        tracked.computeIfAbsent(registry, key -> new ArrayList<>()).add(entry);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<RegistryEntry<T, ? extends T>> getAll(ResourceKey<? extends Registry<T>> registry) {
        List<RegistryEntry<?, ?>> entries = tracked.get(registry);
        if (entries == null)
            return List.of();
        return (Collection<RegistryEntry<T, ? extends T>>) (Collection<?>) Collections.unmodifiableList(entries);
    }
}
