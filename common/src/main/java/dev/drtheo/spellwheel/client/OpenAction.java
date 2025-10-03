package dev.drtheo.spellwheel.client;

import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Supplier;

public class OpenAction extends Action {

    private final String name;

    private final Supplier<WidgetSet> func;

    public OpenAction(String name, Supplier<WidgetSet> set) {
        this.name = name;
        this.func = set;
    }

    public static OpenAction create(String name, Supplier<List<Widget>> supplier) {
        return new OpenAction(name, () -> WidgetSet.create(supplier.get()));
    }

    @Override
    public void run(Minecraft client) {
        client.setScreen(new WheelScreen(name, func.get()));
    }
}