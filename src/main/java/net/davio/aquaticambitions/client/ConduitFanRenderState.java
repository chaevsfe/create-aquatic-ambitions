package net.davio.aquaticambitions.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
public record ConduitFanRenderState(Matrix3x2f pose, int x0, int y0, ScreenRectangle bounds) implements PictureInPictureRenderState {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 44;
    public static final float SCALE = 24;

    public ConduitFanRenderState(Matrix3x2f pose, int x, int y) {
        this(pose, x, y, new ScreenRectangle(x, y, WIDTH, HEIGHT).transformMaxBounds(pose));
    }

    @Override
    public int x1() {
        return x0 + WIDTH;
    }

    @Override
    public int y1() {
        return y0 + HEIGHT;
    }

    @Override
    public float scale() {
        return SCALE;
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }
}
