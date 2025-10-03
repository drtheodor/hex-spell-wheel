package dev.drtheo.spellwheel;

import net.minecraft.resources.ResourceLocation;

public final class SpellWheel {
    public static final String MOD_ID = "spellwheel";

    public static void init() { }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
