package dev.drtheo.spellwheel.client;

import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.drtheo.spellwheel.SpellWheelClient;
import dev.drtheo.spellwheel.client.ui.WheelScreen;
import dev.drtheo.spellwheel.client.ui.Widget;
import dev.drtheo.spellwheel.client.ui.WidgetSet;
import dev.drtheo.spellwheel.client.ui.action.OpenAction;
import dev.drtheo.spellwheel.client.ui.action.SwitchPageAction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WheelKeybinds {

    public static final KeyMapping OPEN_SPELL_WHEEL = new KeyMapping(
            I18n.key("open"), InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE, I18n.keyCategory("main")
    );

    public static void init() {
        KeyMappingRegistry.register(OPEN_SPELL_WHEEL);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (minecraft.player != null && OPEN_SPELL_WHEEL.consumeClick() && !(minecraft.screen instanceof WheelScreen)) {
                ItemStack spellBook = SpellWheelClient.getSpellbook(minecraft.player);

                if (spellBook == null) return;

                int maxPage = ItemSpellbook.highestPage(spellBook);

                if (maxPage == 0) return;

                boolean[] realPages = SpellWheelClient.getRealPages(spellBook);
                MutableComponent[] names = SpellWheelClient.getPageNames(spellBook);

                Widget[] widgets = maxPage > WidgetSet.SET_SIZE ? buildChapters(realPages, names, maxPage) : buildPages(realPages, names, 0, maxPage);
                Minecraft.getInstance().setScreen(new WheelScreen(WidgetSet.create(widgets)));
            }
        });
    }

    private static Widget[] buildChapters(boolean[] realPages, MutableComponent[] names, int maxPages) {
        int chapters = Math.min((int) Math.ceil(maxPages / (double) WidgetSet.SET_SIZE), WidgetSet.SET_SIZE);
        Widget[] widgets = new Widget[chapters];

        for (int i = 0; i < chapters; i++) {
            int pageOffset = i * WidgetSet.SET_SIZE;
            int maxChapterPage = Math.min(pageOffset + WidgetSet.SET_SIZE, realPages.length);

            MutableComponent label = I18n.chapter(i);

            for (int j = pageOffset; j < maxChapterPage; j++) {
                if (!realPages[j]) continue;

                label = label.append(Component.literal("\n - ")
                        .append(names[j] == null ? I18n.page(j) : names[j])
                        .withStyle(ChatFormatting.GRAY));
            }

            widgets[i] = new Widget(label, Items.BOOK, OpenAction.create(
                    () -> buildPages(realPages, names, pageOffset, maxPages)));
        }

        return widgets;
    }

    private static Widget[] buildPages(boolean[] realPages, MutableComponent[] names, int start, int maxPages) {
        Widget[] widgets = new Widget[maxPages - start];

        for (int i = start; i < maxPages; i++) {
            if (!realPages[i]) continue;

            MutableComponent label = I18n.page(i);

            if (names[i] != null)
                label = names[i].append("\n").append(label.withStyle(ChatFormatting.GRAY));

            widgets[i - start] = new Widget(label, Items.PAPER, new SwitchPageAction(i + 1));
        }

        return widgets;
    }
}
