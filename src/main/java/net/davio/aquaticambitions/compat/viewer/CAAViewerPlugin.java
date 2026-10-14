package net.davio.aquaticambitions.compat.viewer;

import dev.chaevsfe.createreiviewer.api.CreateViewerPlugin;
import dev.chaevsfe.createreiviewer.api.ViewerRecipeRegistry;
import net.davio.aquaticambitions.content.kinetics.fan.processing.ChannelingRecipe;
import net.davio.aquaticambitions.registry.CAARecipeTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public final class CAAViewerPlugin implements CreateViewerPlugin {
    public static final Identifier CHANNELING = CAARecipeTypes.CHANNELING.getId();

    @Override
    public void registerRecipes(ViewerRecipeRegistry registry) {
        RecipeType<ChannelingRecipe> type = CAARecipeTypes.CHANNELING.getType();
        registry.add(CHANNELING, type, ChannelingRecipe.class, (holder, recipe) -> recipe
            .input(holder.value().ingredient())
            .results(holder.value().results())
            .build());
        registry.synchronize(CAARecipeTypes.CHANNELING.getSerializer());
    }
}
