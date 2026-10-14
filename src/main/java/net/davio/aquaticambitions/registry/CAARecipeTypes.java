package net.davio.aquaticambitions.registry;

import com.zurrtum.create.AllRecipeSets;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.davio.aquaticambitions.content.kinetics.fan.processing.ChannelingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Locale;
import java.util.Optional;

@SuppressWarnings("unchecked")
public enum CAARecipeTypes {
    CHANNELING(ChannelingRecipe.SERIALIZER,
            recipe -> recipe instanceof ChannelingRecipe channeling ? Optional.of(channeling.ingredient()) : Optional.empty());

    public final Identifier id;
    public final RecipeSerializer<?> serializerObject;
    public final RecipeType<?> type;
    public final ResourceKey<RecipePropertySet> propertySet;

    CAARecipeTypes(RecipeSerializer<?> serializer, RecipeManager.IngredientExtractor ingredients) {
        String name = name().toLowerCase(Locale.ROOT);
        id = CreateAquaticAmbitions.asResource(name);
        serializerObject = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
        type = Registry.register(BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
        propertySet = ResourceKey.create(RecipePropertySet.TYPE_KEY, id);
        AllRecipeSets.ALL.put(propertySet, ingredients);
    }

    public static void register() {
    }

    public Identifier getId() {
        return id;
    }

    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject;
    }

    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type;
    }
}
