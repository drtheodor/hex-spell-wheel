package dev.drtheo.spellwheel;

import dev.drtheo.spellwheel.client.WheelKeybinds;
import dev.drtheo.spellwheel.client.config.WheelClientConfig;

public final class SpellWheelClient {

    public static void init() {
        WheelClientConfig.init();
        WheelKeybinds.init();
    }
}
