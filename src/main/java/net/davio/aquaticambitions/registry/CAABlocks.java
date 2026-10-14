package net.davio.aquaticambitions.registry;

import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlock;
import net.davio.aquaticambitions.ponder.util.fakeConduitEntity.FakeActiveConduitBlock;
import net.davio.aquaticambitions.registrate.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

import static net.davio.aquaticambitions.CreateAquaticAmbitions.REGISTRATE;

public class CAABlocks {

    public static final BlockEntry<Block> PRISMARINE_ALLOY_BLOCK = REGISTRATE
            .block("prismarine_alloy_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.mapColor(MapColor.GLOW_LICHEN).requiresCorrectToolForDrops())
            .simpleItem()
            .register();

    public static final BlockEntry<MechanicalConduitBlock> MECHANICAL_CONDUIT = REGISTRATE
            .block("mechanical_conduit", MechanicalConduitBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK.weathering().unaffected())
            .properties(p -> p
                    .mapColor(MapColor.COLOR_GRAY)
                    .noOcclusion()
                    .lightLevel(MechanicalConduitBlock::getLight)
            )
            .simpleItem()
            .register();

    public static final BlockEntry<FakeActiveConduitBlock> FAKE_CONDUIT = REGISTRATE
            .block("fake_active_conduit", FakeActiveConduitBlock::new)
            .register();

    public static void register() {
    }
}
