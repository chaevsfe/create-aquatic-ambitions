package net.davio.aquaticambitions.infrastructure.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class CAALootModifiers {

    private static final List<Block> SILKLESS_CORALS = List.of(
            Blocks.BRAIN_CORAL, Blocks.BUBBLE_CORAL, Blocks.FIRE_CORAL, Blocks.HORN_CORAL, Blocks.TUBE_CORAL,
            Blocks.BRAIN_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN, Blocks.FIRE_CORAL_FAN, Blocks.HORN_CORAL_FAN, Blocks.TUBE_CORAL_FAN,
            Blocks.DEAD_BRAIN_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.DEAD_TUBE_CORAL,
            Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN
    );

    public static void register() {
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> addSilklessCoral(context, drops));
    }

    private static void addSilklessCoral(LootContext context, List<ItemStack> drops) {
        BlockState state = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        if (state == null || !SILKLESS_CORALS.contains(state.getBlock()))
            return;
        if (hasSilkTouch(context.getOptionalParameter(LootContextParams.TOOL)))
            return;
        drops.add(new ItemStack(state.getBlock().asItem()));
    }

    private static boolean hasSilkTouch(ItemInstance tool) {
        if (tool == null)
            return false;
        ItemEnchantments enchantments = tool.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null)
            return false;
        for (Holder<Enchantment> enchantment : enchantments.keySet()) {
            if (enchantment.is(Enchantments.SILK_TOUCH) && enchantments.getLevel(enchantment) >= 1)
                return true;
        }
        return false;
    }
}
