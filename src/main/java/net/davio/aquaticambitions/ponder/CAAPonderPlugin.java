package net.davio.aquaticambitions.ponder;

import com.zurrtum.create.client.ponder.api.registration.PonderPlugin;
import com.zurrtum.create.client.ponder.api.registration.PonderSceneRegistrationHelper;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class CAAPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreateAquaticAmbitions.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
        CAAPonderScenes.register(helper);
    }
}
