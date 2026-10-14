package net.davio.aquaticambitions.content.processing.conduit;

import net.davio.aquaticambitions.content.processing.conduit.MechanicalConduitEffectDefinition.Behavior;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.material.Fluid;

public class MechanicalConduitEffect {

    private final MechanicalConduitEffectDefinition definition;
    private float ticks;
    private int amplifier;

    public MechanicalConduitEffect(MechanicalConduitEffectDefinition definition) {
        this.definition = definition;
        this.ticks = 0;
        this.amplifier = 0;
    }

    public int getTicks(){
        return Mth.floor(ticks);
    }

    public void setTicks(int newTicks) {
        this.ticks = newTicks;
    }

    public boolean isActive(){
        return ticks > 0f;
    }

    public void addTicks(float amount){
        ticks += amount;
    }

    public void subtractTicks(){
        ticks --;
        if (ticks < 0f) ticks = 0f;
    }

    public int getAmplifier() {return amplifier;}

    public void setAmplifier(int newAmplifier) {
        if (newAmplifier > this.amplifier) {
            amplifier = newAmplifier;
        }
    }

    public void resetAmplifier() {
        amplifier = 0;
    }

    public String getLangKey(){
        return definition.langKey();
    }

    public int getColor(){
        return definition.color();
    }

    public Holder<MobEffect> getEffect(){
        return definition.mobEffect().orElse(null);
    }

    public TagKey<Fluid> getFluidTag(){
        return definition.fluidTag();
    }

    public Behavior getBehavior(){
        return definition.behavior();
    }
}
