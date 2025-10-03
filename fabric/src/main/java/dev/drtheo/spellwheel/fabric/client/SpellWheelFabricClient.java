package dev.drtheo.spellwheel.fabric.client;

import dev.drtheo.spellwheel.SpellWheelClient;
import net.fabricmc.api.ClientModInitializer;

public final class SpellWheelFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpellWheelClient.init();
    }
}
