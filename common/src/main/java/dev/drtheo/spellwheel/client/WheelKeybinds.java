package dev.drtheo.spellwheel.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.drtheo.spellwheel.client.util.SpellbookUtil;
import net.minecraft.client.KeyMapping;

public class WheelKeybinds {

    public static final KeyMapping OPEN_SPELL_WHEEL = new KeyMapping(
            I18n.key("open"), InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE, I18n.keyCategory("main")
    );

    public static void init() {
        KeyMappingRegistry.register(OPEN_SPELL_WHEEL);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (minecraft.player == null || minecraft.screen != null)
                return;

            if (!OPEN_SPELL_WHEEL.consumeClick()) return;

            SpellbookUtil.createWheel(minecraft).ifPresent(minecraft::setScreen);
        });
    }
}
