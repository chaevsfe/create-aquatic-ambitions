package net.davio.aquaticambitions.registry;

import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlockEntity;
import net.davio.aquaticambitions.ponder.util.fakeConduitEntity.FakeActiveConduitBlockEntity;
import net.davio.aquaticambitions.registrate.entry.BlockEntityEntry;

import static net.davio.aquaticambitions.CreateAquaticAmbitions.REGISTRATE;

public class CAABlockEntityTypes {

    public static final BlockEntityEntry<MechanicalConduitBlockEntity> MECHANICAL_CONDUIT = REGISTRATE
            .blockEntity("mechanical_conduit", MechanicalConduitBlockEntity::new)
            .validBlocks(CAABlocks.MECHANICAL_CONDUIT)
            .register();

    public static final BlockEntityEntry<FakeActiveConduitBlockEntity> FAKE_CONDUIT = REGISTRATE
            .blockEntity("fake_conduit", FakeActiveConduitBlockEntity::new)
            .validBlocks(CAABlocks.FAKE_CONDUIT)
            .register();

    public static void register() {
    }
}
