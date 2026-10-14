package net.davio.aquaticambitions.compat.viewer;

import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.client.foundation.gui.AllGuiTextures;
import dev.chaevsfe.createreiviewer.api.ViewerIngredient;
import dev.chaevsfe.createreiviewer.api.ViewerRecipe;
import dev.chaevsfe.createreiviewer.api.client.CreateViewerClientPlugin;
import dev.chaevsfe.createreiviewer.api.client.ViewerCanvas;
import dev.chaevsfe.createreiviewer.api.client.ViewerCategory;
import dev.chaevsfe.createreiviewer.api.client.ViewerCategoryRegistry;
import net.davio.aquaticambitions.client.ConduitFanRenderState;
import net.davio.aquaticambitions.util.CAALang;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public final class CAAViewerClientPlugin implements CreateViewerClientPlugin {
    private static final String FAN_CHANNELING = "fan_channeling";

    @Override
    public void registerCategories(ViewerCategoryRegistry registry) {
        var id = CAAViewerPlugin.CHANNELING.withPath(FAN_CHANNELING);
        ItemStack catalyst = new ItemStack(AllBlocks.ENCASED_FAN);
        catalyst.set(DataComponents.CUSTOM_NAME, CAALang.description("recipe", id, "fan").component().withStyle(style -> style.withItalic(false)));
        registry.add(ViewerCategory.builder(CAAViewerPlugin.CHANNELING)
            .title(CAALang.description("recipe", id).component())
            .icon(new ItemStack(AllItems.PROPELLER), new ItemStack(Items.HEART_OF_THE_SEA))
            .height(72)
            .workstations(catalyst)
            .layout(CAAViewerClientPlugin::channeling)
            .build());
    }

    private static void channeling(ViewerRecipe recipe, ViewerCanvas canvas) {
        List<ViewerIngredient> outputs = recipe.outputs();
        int count = outputs.size();
        int shift = 1 - Math.min(3, count);

        canvas.texture(AllGuiTextures.JEI_SHADOW, 46, 27);
        canvas.texture(AllGuiTextures.JEI_LIGHT, 65, 39);
        canvas.texture(AllGuiTextures.JEI_LONG_ARROW, 54 + 7 * shift, 51);
        canvas.pip(56, 4, ConduitFanRenderState::new);

        if (count == 1) {
            canvas.slot(21, 48, recipe.input(0));
            canvas.output(141, 48, outputs.get(0), recipe.chance(0));
            return;
        }
        canvas.slot(21 + 5 * shift, 48, recipe.input(0));
        int xBase = 141 + 9 * shift;
        int yBase = count > 9 ? 57 : 48;
        for (int i = 0; i < count; i++) {
            canvas.output(xBase + 19 * (i % 3), yBase - 19 * (i / 3), outputs.get(i), recipe.chance(i));
        }
    }
}
