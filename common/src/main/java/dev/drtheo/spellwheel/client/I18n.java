package dev.drtheo.spellwheel.client;

import dev.drtheo.spellwheel.SpellWheel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class I18n {

    public static MutableComponent page(@Nullable MutableComponent name, int index) {
        if (name == null)
            return Component.translatable("widget." + SpellWheel.MOD_ID + ".page", index);

        return name.append(Component.translatable("widget." + SpellWheel.MOD_ID + ".page.suffix", index).withStyle(ChatFormatting.GRAY));
    }

    public static MutableComponent chapter(int index) {
        return Component.translatable("widget." + SpellWheel.MOD_ID + ".chapter", index + 1);
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

    public static MutableComponent numbered(int num, Component text) {
        return Component.empty().append(Component.literal(num + ". ").withStyle(ChatFormatting.GRAY)).append(text);
    }
}
