package dev.drtheo.spellwheel.client.util;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import dev.drtheo.spellwheel.client.I18n;
import dev.drtheo.spellwheel.client.config.WheelClientConfig;
import dev.drtheo.spellwheel.client.ui.WheelScreen;
import dev.drtheo.spellwheel.client.ui.Widget;
import dev.drtheo.spellwheel.client.ui.WidgetSet;
import dev.drtheo.spellwheel.client.ui.action.IconChangeAction;
import dev.drtheo.spellwheel.client.ui.action.OpenAction;
import dev.drtheo.spellwheel.client.ui.action.SwitchPageAction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SpellbookUtil {

    public static final Item DEFAULT_ICON = Items.PAPER;

    public static Optional<WheelScreen> createWheel(Minecraft client) {
        ItemStack spellBook = getSpellbook(client.player);

        if (spellBook == null) return Optional.empty();

        int maxPage = ItemSpellbook.highestPage(spellBook);

        if (maxPage == 0) return Optional.empty();

        CompoundTag iotas = NBTHelper.getCompound(spellBook, ItemSpellbook.TAG_PAGES);
        MutableComponent[] names = getPageNames(spellBook);

        Widget[] widgets = maxPage > WidgetSet.SET_SIZE ? buildChapters(iotas, names, maxPage) : buildPages(iotas, names, 0, maxPage);
        return Optional.of(new WheelScreen(WidgetSet.create(widgets)));
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
        Item icon = WheelClientConfig.get().getIconOr(name, DEFAULT_ICON);

        if (iotas != null) {
            Component displayIota = IotaType.getDisplay(iotas);
            label.append("\n").append(Component.translatable("hexcasting.spelldata.onitem", displayIota));
        }

        return new Widget(label, icon, new SwitchPageAction(page), new IconChangeAction(name, DEFAULT_ICON, 0x7f0000));
    }

    public static ItemStack getSpellbook(Player player) {
        ItemStack offStack = player.getOffhandItem();
        if (offStack.getItem() instanceof ItemSpellbook) return offStack;

        ItemStack mainStack = player.getMainHandItem();
        if (mainStack.getItem() instanceof ItemSpellbook) return mainStack;

        return null;
    }

    public static MutableComponent[] getPageNames(ItemStack spellBook) {
        CompoundTag names = NBTHelper.getCompound(spellBook, ItemSpellbook.TAG_PAGE_NAMES);
        int shiftedIdx = Math.max(1, ItemSpellbook.highestPage(spellBook));

        MutableComponent[] components = new MutableComponent[shiftedIdx];

        for (int i = 1; i < shiftedIdx + 1; i++) {
            String nameKey = String.valueOf(i);
            String name = NBTHelper.getString(names, nameKey);

            components[i - 1] = name == null ? null : Component.Serializer.fromJson(name);
        }

        return components;
    }
}
