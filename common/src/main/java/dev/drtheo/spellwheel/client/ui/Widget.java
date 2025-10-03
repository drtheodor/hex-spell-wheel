package dev.drtheo.spellwheel.client.ui;

import dev.drtheo.spellwheel.client.ui.action.Action;
import dev.drtheo.spellwheel.client.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public record Widget(Component label, ItemStack preview, Optional<Action> actions, boolean keepOpened) {

    private static final ItemStack EMPTY_STACK = new ItemStack(Items.BARRIER);

    public Widget(Component label, Item item, Action actions) {
        this(label, new ItemStack(item), Optional.of(actions), false);
    }

    public void run(Minecraft client) {
        actions.ifPresent(action -> action.run(client));
    }

    public static Widget empty() {
        return new Widget(I18n.emptyWidget(), EMPTY_STACK, Optional.empty(), true);
    }
}