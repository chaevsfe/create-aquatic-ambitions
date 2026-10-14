package net.davio.aquaticambitions.registrate.builders;

import net.davio.aquaticambitions.registrate.Registrate;
import net.davio.aquaticambitions.registrate.fn.NonNullConsumer;
import net.davio.aquaticambitions.registrate.fn.NonNullUnaryOperator;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBuilder<V, P, S extends AbstractBuilder<V, P, S>> {
    private final Registrate owner;
    private final String name;
    private final P parent;
    private final List<NonNullConsumer<? super V>> onRegister = new ArrayList<>();

    protected AbstractBuilder(Registrate owner, String name, P parent) {
        this.owner = owner;
        this.name = name;
        this.parent = parent;
    }

    public Registrate getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public P getParent() {
        return parent;
    }

    public Identifier getId() {
        return owner.id(name);
    }

    @SuppressWarnings("unchecked")
    protected final S self() {
        return (S) this;
    }

    public S onRegister(NonNullConsumer<? super V> callback) {
        onRegister.add(callback);
        return self();
    }

    public S transform(NonNullUnaryOperator<S> operator) {
        return operator.apply(self());
    }

    public P build() {
        return parent;
    }

    protected void runRegisterCallbacks(V value) {
        for (NonNullConsumer<? super V> callback : onRegister)
            callback.accept(value);
        onRegister.clear();
    }
}
