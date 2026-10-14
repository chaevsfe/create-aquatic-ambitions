package net.davio.aquaticambitions.infrastructure.config;

import com.zurrtum.create.catnip.config.Builder;
import net.davio.aquaticambitions.CreateAquaticAmbitions;

public class CAAConfig {

    private static CAAServerConfig SERVER_CONFIG;

    public static CAAServerConfig server() {
        return SERVER_CONFIG;
    }

    public static void register() {
        SERVER_CONFIG = Builder.create(CAAServerConfig::new, CreateAquaticAmbitions.MODID, "server", true);
    }
}
