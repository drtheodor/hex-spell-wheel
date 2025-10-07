package dev.drtheo.spellwheel.client.config;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WheelClientConfig {

    private static final Map<String, Holder<Item>> ICONS = new HashMap<>();

    public static void read() {
        
    }

    static {
        ICONS.put("Theo's Gambit", Items.ACACIA_PLANKS.arch$holder());
    }

    public static Item getIconOr(String name, Item def) {
        return Optional.ofNullable(ICONS.get(name)).map(Holder::value).orElse(def);
    }
}
