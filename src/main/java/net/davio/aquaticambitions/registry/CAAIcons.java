package net.davio.aquaticambitions.registry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.zurrtum.create.client.catnip.gui.element.ScreenElement;
import com.zurrtum.create.client.foundation.gui.AllIcons;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class CAAIcons extends AllIcons {

    public static final Identifier ICON_ATLAS = CreateAquaticAmbitions.asResource("textures/gui/icons.png");
    public static final int ATLAS_SIZE = 256;
    private static int x = 0, y = -1;
    private final int iconX;
    private final int iconY;

    public static final CAAIcons
            I_EVERYONE = newRow(),
            I_PLAYERS = next(),
            I_FRIENDLY_MOBS = next(),
            I_PLAYERS_FRIENDLY_MOBS = next(),
            I_MONSTERS = next();

    public static final ScreenElement
            I_CONDUIT_POWER = mobEffect("conduit_power"),
            I_FIRE_RES = mobEffect("fire_resistance");

    public CAAIcons(int x, int y) {
        super(x, y);
        iconX = x * 16;
        iconY = y * 16;
    }

    private static CAAIcons next() {
        return new CAAIcons(++x, y);
    }

    private static CAAIcons newRow() {
        x = 0;
        y++;
        return new CAAIcons(x, y);
    }

    private static ScreenElement mobEffect(String effect) {
        Identifier sprite = Identifier.withDefaultNamespace("mob_effect/" + effect);
        return (graphics, x, y) -> graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 16, 16);
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICON_ATLAS, x, y, iconX, iconY, 16, 16, ATLAS_SIZE, ATLAS_SIZE);
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, int x, int y, int color) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ICON_ATLAS, x, y, iconX, iconY, 16, 16, 16, 16, ATLAS_SIZE, ATLAS_SIZE, color);
    }

    @Override
    public RenderType bind() {
        return RenderTypes.text(ICON_ATLAS);
    }

    @Override
    public void render(PoseStack ms, MultiBufferSource buffer, int color) {
        VertexConsumer builder = buffer.getBuffer(RenderTypes.text(ICON_ATLAS));
        Matrix4f matrix = ms.last().pose();
        int light = LightCoordsUtil.FULL_BRIGHT;

        float u1 = iconX * 1f / ATLAS_SIZE;
        float u2 = (iconX + 16) * 1f / ATLAS_SIZE;
        float v1 = iconY * 1f / ATLAS_SIZE;
        float v2 = (iconY + 16) * 1f / ATLAS_SIZE;

        vertex(builder, matrix, 0, 0, u1, v1, color, light);
        vertex(builder, matrix, 0, 1, u1, v2, color, light);
        vertex(builder, matrix, 1, 1, u2, v2, color, light);
        vertex(builder, matrix, 1, 0, u2, v1, color, light);
    }

    private static void vertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float u, float v, int color, int light) {
        builder.addVertex(matrix, x, y, 0)
                .setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255)
                .setUv(u, v)
                .setLight(light);
    }
}
