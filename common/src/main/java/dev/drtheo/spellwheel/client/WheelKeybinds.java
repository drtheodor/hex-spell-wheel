package dev.drtheo.spellwheel.client;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.drtheo.spellwheel.SpellWheelClient;
import dev.drtheo.spellwheel.client.config.WheelClientConfig;
import dev.drtheo.spellwheel.client.ui.WheelScreen;
import dev.drtheo.spellwheel.client.ui.Widget;
import dev.drtheo.spellwheel.client.ui.WidgetSet;
import dev.drtheo.spellwheel.client.ui.action.OpenAction;
import dev.drtheo.spellwheel.client.ui.action.SwitchPageAction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

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

            ItemStack spellBook = SpellWheelClient.getSpellbook(minecraft.player);

            if (spellBook == null) return;

            int maxPage = ItemSpellbook.highestPage(spellBook);

            if (maxPage == 0) return;

            CompoundTag iotas = NBTHelper.getCompound(spellBook, ItemSpellbook.TAG_PAGES);
            MutableComponent[] names = SpellWheelClient.getPageNames(spellBook);

            Widget[] widgets = maxPage > WidgetSet.SET_SIZE ? buildChapters(iotas, names, maxPage) : buildPages(iotas, names, 0, maxPage);
            Minecraft.getInstance().setScreen(new WheelScreen(WidgetSet.create(widgets)));
        });
    }

    private static Widget[] buildChapters(CompoundTag iotas, MutableComponent[] names, int maxPages) {
        int chapters = Math.min((int) Math.ceil(maxPages / (double) WidgetSet.SET_SIZE), WidgetSet.SET_SIZE);
        Widget[] widgets = new Widget[chapters];

        for (int i = 0; i < chapters; i++) {
            int pageOffset = i * WidgetSet.SET_SIZE;
            int maxChapterPage = Math.min(pageOffset + WidgetSet.SET_SIZE, maxPages);

            MutableComponent label = I18n.chapter(i);

            for (int j = pageOffset; j < maxChapterPage; j++) {
                int page = j + 1;

                if (!iotas.contains(String.valueOf(page)))
                    continue;

                label = label.append(Component.literal("\n - ")
                        .append(I18n.page(names[j], page))
                        .withStyle(ChatFormatting.GRAY));
            }

            label = I18n.numbered(i + 1, label);
            widgets[i] = new Widget(label, Items.BOOK, OpenAction.create(
                    () -> buildPages(iotas, names, pageOffset, maxPages)));
        }

        return widgets;
    }

    private static Widget[] buildPages(CompoundTag iotas, MutableComponent[] names, int start, int maxPages) {
        Widget[] widgets = new Widget[maxPages - start];

        for (int i = start; i < maxPages; i++) {
            int page = i + 1;
            CompoundTag tag = (CompoundTag) iotas.get(String.valueOf(page));

            if (tag == null)
                continue;

            widgets[i - start] = createPageWidget(names[i], tag, page, start);
        }

        return widgets;
    }

    private static Widget createPageWidget(@Nullable MutableComponent name, @Nullable CompoundTag iotas, int page, int offset) {
        MutableComponent label = I18n.numbered(page - offset, I18n.page(name, page));
        Item icon = name != null ? WheelClientConfig.getIconOr(name.getString(), Items.PAPER) : Items.PAPER;

        if (iotas != null) {
            Component displayIota = IotaType.getDisplay(iotas);
            label.append("\n").append(Component.translatable("hexcasting.spelldata.onitem", displayIota));
        }

        return new Widget(label, icon, new SwitchPageAction(page));
    }
}
