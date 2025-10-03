package dev.drtheo.spellwheel;

import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import dev.drtheo.spellwheel.client.WheelKeybinds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SpellWheelClient {

    public static void init() {
        WheelKeybinds.init();
    }

    public static ItemStack getSpellbook(Player player) {
        ItemStack offStack = player.getOffhandItem();
        if (offStack.getItem() instanceof ItemSpellbook) return offStack;

        ItemStack mainStack = player.getMainHandItem();
        if (mainStack.getItem() instanceof ItemSpellbook) return mainStack;

        return null;
    }

    public static boolean[] getRealPages(ItemStack spellBook) {
        CompoundTag data = NBTHelper.getCompound(spellBook, ItemSpellbook.TAG_PAGES);

        int shiftedIdx = Math.max(1, ItemSpellbook.highestPage(spellBook));
        boolean[] result = new boolean[shiftedIdx];

        for (int i = 1; i < shiftedIdx + 1; i++) {
            String nameKey = String.valueOf(i);
            result[i - 1] = data.contains(nameKey);
        }

        return result;
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
