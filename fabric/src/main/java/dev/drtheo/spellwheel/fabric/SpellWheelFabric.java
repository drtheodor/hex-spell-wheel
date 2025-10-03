package dev.drtheo.spellwheel.fabric;

import dev.drtheo.spellwheel.SpellWheel;
import net.fabricmc.api.ModInitializer;

public final class SpellWheelFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SpellWheel.init();
    }
}
