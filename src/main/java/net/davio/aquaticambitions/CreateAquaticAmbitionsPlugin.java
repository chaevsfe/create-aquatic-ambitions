package net.davio.aquaticambitions;

import com.zurrtum.create.api.registry.CreateRegisterPlugin;

public final class CreateAquaticAmbitionsPlugin implements CreateRegisterPlugin {
    private static boolean blocksRegistered;

    @Override
    public void onBlockRegister() {
        if (blocksRegistered)
            throw new IllegalStateException("Create Fly invoked Aquatic Ambitions block registration more than once");
        CreateAquaticAmbitions.registerBlocks();
        blocksRegistered = true;
    }

    static void verifyBlocksRegistered() {
        if (!blocksRegistered)
            throw new IllegalStateException("Create Fly did not invoke Aquatic Ambitions block registration");
    }
}
