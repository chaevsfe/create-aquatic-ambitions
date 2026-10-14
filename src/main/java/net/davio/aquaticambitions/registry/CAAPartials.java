package net.davio.aquaticambitions.registry;

import com.zurrtum.create.client.flywheel.lib.model.baked.PartialModel;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CAAPartials {
    public static final PartialModel

            CONDUIT_CAGE = block("conduit_cage"),
            INACTIVE_CONDUIT = block("inactive_conduit"),
            CONDUIT_EYE = block("conduit_eye"),
            CONDUIT_WIND = block("conduit_wind");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateAquaticAmbitions.asResource("block/" + path));
    }

    public static void init() {
    }
}
