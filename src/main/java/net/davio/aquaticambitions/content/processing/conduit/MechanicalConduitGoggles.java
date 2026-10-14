package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.client.api.goggles.IHaveGoggleInformation;
import com.zurrtum.create.client.foundation.blockEntity.behaviour.tooltip.TooltipBehaviour;
import net.davio.aquaticambitions.util.CAALang;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.List;

@Environment(EnvType.CLIENT)
public class MechanicalConduitGoggles extends TooltipBehaviour<MechanicalConduitBlockEntity> implements IHaveGoggleInformation {

    public MechanicalConduitGoggles(MechanicalConduitBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if (!blockEntity.getConduitLevel().isAwakened()) {
            return false;
        }

        int before = tooltip.size();

        CAALang.translate("tooltip.conduitcage.header").forGoggles(tooltip);

        for(MechanicalConduitEffect conduitEffect : blockEntity.getConduitEffects().values()) {

            Component effectName = Component.translatable(conduitEffect.getLangKey())
                    .withStyle(Style.EMPTY.withColor(conduitEffect.getColor()));

            if (conduitEffect.isActive()){
                CAALang.text("")
                    .add(effectName)
                    .add(CAALang.text(" "))
                    .add(CAALang.text(blockEntity.tickToDuration(conduitEffect.getTicks()))
                    .style(ChatFormatting.WHITE))
                    .forGoggles(tooltip, 1);
            }
        }
        return tooltip.size() > before;
    }
}
