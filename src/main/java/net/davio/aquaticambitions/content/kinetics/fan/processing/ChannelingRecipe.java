package net.davio.aquaticambitions.content.kinetics.fan.processing;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zurrtum.create.content.processing.recipe.ProcessingOutput;
import com.zurrtum.create.foundation.recipe.CreateSingleStackRollableRecipe;
import net.davio.aquaticambitions.registry.CAARecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

public record ChannelingRecipe(List<ProcessingOutput> results, Ingredient ingredient) implements CreateSingleStackRollableRecipe {
    public static final int MAX_OUTPUT_COUNT = 12;

    public static final MapCodec<ChannelingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ProcessingOutput.CODEC.listOf(1, MAX_OUTPUT_COUNT).fieldOf("results").forGetter(ChannelingRecipe::results),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ChannelingRecipe::ingredient)
    ).apply(instance, ChannelingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChannelingRecipe> STREAM_CODEC = StreamCodec.composite(
            ProcessingOutput.STREAM_CODEC.apply(ByteBufCodecs.list()), ChannelingRecipe::results,
            Ingredient.CONTENTS_STREAM_CODEC, ChannelingRecipe::ingredient,
            ChannelingRecipe::new);

    public static final RecipeSerializer<ChannelingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(SingleRecipeInput inv, Level worldIn) {
        return !inv.isEmpty() && ingredient.test(inv.getItem(0));
    }

    @Override
    public RecipeSerializer<ChannelingRecipe> getSerializer() {
        return CAARecipeTypes.CHANNELING.getSerializer();
    }

    @Override
    public RecipeType<ChannelingRecipe> getType() {
        return CAARecipeTypes.CHANNELING.getType();
    }
}
