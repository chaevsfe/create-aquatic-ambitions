package net.davio.aquaticambitions.content.processing.conduit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public record MechanicalConduitEffectDefinition(
        Optional<Holder<MobEffect>> mobEffect,
        int color,
        String langKey,
        TagKey<Fluid> fluidTag,
        Behavior behavior,
        boolean enabled
) {
    public static final Codec<Integer> COLOR_CODEC = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(Integer.parseInt(s.startsWith("#") ? s.substring(1) : s, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> "Not a hex color string: " + s);
                }
            },
            i -> String.format("#%06X", i & 0xFFFFFF));

    public static final MapCodec<Optional<Holder<MobEffect>>> MOB_EFFECT_FIELD = Codec.STRING.optionalFieldOf("mob_effect").xmap(
            optString -> optString
                    .map(Identifier::tryParse)
                    .flatMap(BuiltInRegistries.MOB_EFFECT::get)
                    .map(ref -> (Holder<MobEffect>) ref),
            optHolder -> optHolder.flatMap(Holder::unwrapKey).map(key -> key.identifier().toString()));

    public static final Codec<MechanicalConduitEffectDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MOB_EFFECT_FIELD.forGetter(MechanicalConduitEffectDefinition::mobEffect),
            COLOR_CODEC.fieldOf("color").forGetter(MechanicalConduitEffectDefinition::color),
            Codec.STRING.fieldOf("lang_key").forGetter(MechanicalConduitEffectDefinition::langKey),
            TagKey.codec(Registries.FLUID).fieldOf("fluid_tag").forGetter(MechanicalConduitEffectDefinition::fluidTag),
            Behavior.CODEC.optionalFieldOf("behavior", Behavior.APPLY).forGetter(MechanicalConduitEffectDefinition::behavior),
            Codec.BOOL.optionalFieldOf("enabled", true).forGetter(MechanicalConduitEffectDefinition::enabled)
    ).apply(instance, MechanicalConduitEffectDefinition::new));

    public enum Behavior implements StringRepresentable {
        APPLY("apply"),
        CLEAR_EFFECTS("clear_effects"),
        SET_ON_FIRE("set_on_fire");

        public static final Codec<Behavior> CODEC = StringRepresentable.fromEnum(Behavior::values);

        private final String name;

        Behavior(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
