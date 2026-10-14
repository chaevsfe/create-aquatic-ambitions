package net.davio.aquaticambitions.content.processing.conduit;

import com.zurrtum.create.client.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.zurrtum.create.client.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitBlockEntity.EntitySelectionMode;
import net.davio.aquaticambitions.registry.CAAIcons;
import net.davio.aquaticambitions.util.CAALang;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public class EntitySelectionModeScroll extends ScrollOptionBehaviour<EntitySelectionMode> {

    public EntitySelectionModeScroll(MechanicalConduitBlockEntity be) {
        super(EntitySelectionModeIcon.class, mode -> EntitySelectionModeIcon.values()[mode.ordinal()],
                CAALang.translateDirect("mechanical_conduit.entity_filter.title"), be,
                new MechanicalConduitModeSlot());
    }

    public enum EntitySelectionModeIcon implements INamedIconOptions {
        EVERYONE(CAAIcons.I_EVERYONE),
        PLAYERS(CAAIcons.I_PLAYERS),
        FRIENDLY_MOBS(CAAIcons.I_FRIENDLY_MOBS),
        PLAYERS_FRIENDLY_MOBS(CAAIcons.I_PLAYERS_FRIENDLY_MOBS),
        MONSTERS(CAAIcons.I_MONSTERS);

        private final String translationKey;
        private final CAAIcons icon;

        EntitySelectionModeIcon(CAAIcons icon) {
            this.icon = icon;
            this.translationKey = "mechanical_conduit.selection_mode." + name().toLowerCase(Locale.ROOT);
        }

        @Override
        public CAAIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }
}
