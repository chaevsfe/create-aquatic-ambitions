package net.davio.aquaticambitions.content.logistics;

import com.zurrtum.create.api.registry.CreateRegistries;
import com.zurrtum.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.zurrtum.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import net.davio.aquaticambitions.CreateAquaticAmbitions;
import net.davio.aquaticambitions.registry.CAAFanProcessingTypes;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.BiPredicate;

public class CAAItemAttributes {

    public static final ItemAttributeType CAN_BE_CHANNELED = Registry.register(CreateRegistries.ITEM_ATTRIBUTE_TYPE,
            CreateAquaticAmbitions.asResource("can_be_channeled"),
            singleton("can_be_channeled", CAAFanProcessingTypes.CHANNELING::canProcess));

    private static ItemAttributeType singleton(String id, BiPredicate<ItemStack, Level> predicate) {
        return new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, predicate, id));
    }

    public static void register() {
    }
}
