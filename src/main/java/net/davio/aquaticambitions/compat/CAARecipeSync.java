package net.davio.aquaticambitions.compat;

import com.zurrtum.create.compat.Mods;
import net.davio.aquaticambitions.registry.CAARecipeTypes;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;

public final class CAARecipeSync {
    private CAARecipeSync() {
    }

    public static void register() {
        if (!Mods.JEI.isLoaded() && !Mods.RRV.isLoaded())
            return;
        RecipeSynchronization.synchronizeRecipeSerializer(CAARecipeTypes.CHANNELING.getSerializer());
    }
}
