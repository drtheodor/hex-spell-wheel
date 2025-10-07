package dev.drtheo.spellwheel.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.drtheo.spellwheel.client.ui.WheelScreen;
import dev.drtheo.spellwheel.client.util.SpellbookUtil;
import net.minecraft.client.KeyMapping;

public class WheelKeybinds {

    public static final KeyMapping OPEN_SPELL_WHEEL = new KeyMapping(
            I18n.key("open"), InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE, I18n.keyCategory("main")
    );

    public static void init() {
        KeyMappingRegistry.register(OPEN_SPELL_WHEEL);

        // TODO: find a better way to catch hotbar keys
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            if (!(client.screen instanceof WheelScreen wheelScreen) || action != 1) return EventResult.pass();
            if (keyCode < InputConstants.KEY_1 || keyCode > InputConstants.KEY_9) return EventResult.pass();

            wheelScreen.simulateClick(keyCode - InputConstants.KEY_1);
            return EventResult.interruptDefault();
        });

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (minecraft.player == null || minecraft.screen != null)
                return;

            if (!OPEN_SPELL_WHEEL.consumeClick()) return;

            SpellbookUtil.createWheel(minecraft).ifPresent(minecraft::setScreen);
        });
    }
}
