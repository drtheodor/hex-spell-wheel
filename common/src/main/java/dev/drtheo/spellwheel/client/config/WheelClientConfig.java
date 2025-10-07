package dev.drtheo.spellwheel.client.config;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WheelClientConfig {

    private static final String FILENAME = "hex-spell-wheel.txt";
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(FILENAME);

    private static WheelClientConfig INSTANCE;

    private final Map<String, Item> icons;

    public static void init() {
        ClientLifecycleEvent.CLIENT_STOPPING.register(instance -> {
            if (INSTANCE != null) INSTANCE.write();
        });

        ClientLifecycleEvent.CLIENT_STARTED.register(instance -> get());
    }

    private WheelClientConfig() {
        this(new HashMap<>());
    }

    private WheelClientConfig(Map<String, Item> map) {
        this.icons = map;
    }

    public static WheelClientConfig get() {
        return INSTANCE == null ? INSTANCE = read() : INSTANCE;
    }

    public static WheelClientConfig read() {
        try {
            Map<String, Item> icons = new HashMap<>();
            String prev = null;

            for (String s : Files.readString(CONFIG_PATH).lines().toArray(String[]::new)) {
                if (prev != null) {
                    icons.put(prev, BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(s)));
                    prev = null;
                } else {
                    prev = s;
                }
            }

            return new WheelClientConfig(icons);
        } catch (Exception e) {
            e.printStackTrace();
            return new WheelClientConfig();
        }
    }

    private void write() {
        try {
            if (Files.notExists(CONFIG_PATH))
                Files.createFile(CONFIG_PATH);

            StringBuilder result = new StringBuilder();

            icons.forEach((s, item) -> result
                    .append(s).append("\n").append(item.arch$registryName()));

            Files.writeString(CONFIG_PATH, result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Item getIconOr(@Nullable Component name, Item def) {
        return Optional.ofNullable(name).map(Component::getString)
                .flatMap(s -> Optional.ofNullable(icons.get(s)))
                .orElse(def);
    }

    public void setIcon(Component name, Item item) {
        icons.put(name.getString(), item);
    }
}
