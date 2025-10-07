package dev.drtheo.spellwheel.client.ui.action;

import dev.drtheo.spellwheel.client.ui.Widget;
import net.minecraft.client.Minecraft;

public interface Action {
    default void run(Minecraft client, Widget widget) { }
    default void runAlt(Minecraft client, Widget widget) { }

    static Action and(Action... actions) {
        return actions.length == 1 ? actions[0] : new AndAction(actions);
    }
}