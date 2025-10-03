package dev.drtheo.spellwheel.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.drtheo.spellwheel.SpellWheel;
import dev.drtheo.spellwheel.SpellWheelClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpellWheel.MOD_ID)
public final class SpellWheelForge {
    public SpellWheelForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(SpellWheel.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.addListener(this::onClient);

        // Run our common setup.
        SpellWheel.init();
    }

    public void onClient(FMLClientSetupEvent event) {
        SpellWheelClient.init();
    }
}
