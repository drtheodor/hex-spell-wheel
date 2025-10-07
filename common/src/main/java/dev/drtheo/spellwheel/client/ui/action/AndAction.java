package dev.drtheo.spellwheel.client.ui.action;

import dev.drtheo.spellwheel.client.ui.Widget;
import net.minecraft.client.Minecraft;

public record AndAction(Action... actions) implements Action {

    @Override
    public void run(Minecraft client, Widget widget) {
        for (Action action : actions) {
            action.run(client, widget);
        }
    }

    @Override
    public void runAlt(Minecraft client, Widget widget) {
        for (Action action : actions) {
            action.runAlt(client, widget);
        }
    }
}
