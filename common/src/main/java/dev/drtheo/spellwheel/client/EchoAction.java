package dev.drtheo.spellwheel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public class EchoAction extends Action {

    final Component message;
    final boolean overlay;

    public EchoAction(Component message, boolean overlay){
        this.message = message;
        this.overlay = overlay;
    }

    public Component getMessage() {
        return message;
    }

    public boolean isOverlay() {
        return overlay;
    }

    @Override
    public void run(Minecraft client) {
        assert client.player != null;
        client.player.displayClientMessage(bakeText(message), overlay);
    }

    public Component bakeText(Component rawText) {
        MutableComponent result = Component.empty();
        List<Component> parts = new ArrayList<>(rawText.getSiblings());
        parts.add(MutableComponent.create(rawText.getContents()).setStyle(rawText.getStyle()));
        for (Component part : parts){
            Style style = part.getStyle();
            String string = part.getString();
            result.append(Component.literal(string).setStyle(style));
        }
        return result;
    }
}