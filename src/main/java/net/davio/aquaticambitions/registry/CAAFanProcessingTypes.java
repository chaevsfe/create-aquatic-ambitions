package net.davio.aquaticambitions.registry;

import com.zurrtum.create.api.registry.CreateRegistries;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.davio.aquaticambitions.content.kinetics.fan.processing.ChannelingFanProcessingType;
import net.minecraft.core.Registry;

public class CAAFanProcessingTypes {

    public static final ChannelingFanProcessingType CHANNELING = Registry.register(CreateRegistries.FAN_PROCESSING_TYPE,
            CreateAquaticAmbitions.asResource("channeling"), new ChannelingFanProcessingType());

    public static void register() {
    }
}
