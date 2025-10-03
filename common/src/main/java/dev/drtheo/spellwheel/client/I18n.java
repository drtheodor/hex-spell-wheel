package dev.drtheo.spellwheel.client;

import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import dev.drtheo.spellwheel.SpellWheel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class I18n {

    public static MutableComponent page(int index) {
        return Component.translatable("widget." + SpellWheel.MOD_ID + ".page", index + 1);
    }

    public static MutableComponent chapter(int index) {
        return Component.translatable("widget." + SpellWheel.MOD_ID + ".chapter", index + 1);
    }

    public static MutableComponent hexPage(@Nullable Component name, ItemStack stack, int page, int len, boolean sealed) {
        if (name != null) {
            if (sealed) {
                return Component.translatable("hexcasting.tooltip.spellbook.page_with_name.sealed",
                        Component.literal(String.valueOf(page)).withStyle(ChatFormatting.WHITE),
                        Component.literal(String.valueOf(len)).withStyle(ChatFormatting.WHITE),
                        Component.literal("").withStyle(stack.getRarity().color, ChatFormatting.ITALIC)
                                .append(name),
                        Component.translatable("hexcasting.tooltip.spellbook.sealed").withStyle(ChatFormatting.GOLD));
            } else {
                return Component.translatable("hexcasting.tooltip.spellbook.page_with_name",
                        Component.literal(String.valueOf(page)).withStyle(ChatFormatting.WHITE),
                        Component.literal(String.valueOf(len)).withStyle(ChatFormatting.WHITE),
                        Component.literal("").withStyle(stack.getRarity().color, ChatFormatting.ITALIC)
                                .append(name));
            }
        } else {
            if (sealed) {
                return Component.translatable("hexcasting.tooltip.spellbook.page.sealed",
                        Component.literal(String.valueOf(page)).withStyle(ChatFormatting.WHITE),
                        Component.literal(String.valueOf(len)).withStyle(ChatFormatting.WHITE),
                        Component.translatable("hexcasting.tooltip.spellbook.sealed").withStyle(ChatFormatting.GOLD));
            } else {
                return Component.translatable("hexcasting.tooltip.spellbook.page",
                        Component.literal(String.valueOf(page)).withStyle(ChatFormatting.WHITE),
                        Component.literal(String.valueOf(len)).withStyle(ChatFormatting.WHITE));
            }
        }
    }

    public static MutableComponent hexPage(ItemStack stack, int page, int len) {
        return hexPage(stack.hasCustomHoverName() ? stack.getHoverName() : null, stack, page, len, ItemSpellbook.isSealed(stack));
    }

    public static String key(String name) {
        return "key." + SpellWheel.MOD_ID + "." + name;
    }

    public static String keyCategory(String name) {
        return "category." + SpellWheel.MOD_ID + "." + name;
    }

    public static Component emptyWidget() {
        return Component.translatable("widget." + SpellWheel.MOD_ID + ".empty")
                .append(Component.literal("\n"))
                .append(Component.translatable("widget." + SpellWheel.MOD_ID + ".empty.desc").withStyle(ChatFormatting.GRAY));
    }
}
