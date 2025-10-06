package dev.drtheo.spellwheel.client.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record StackInHand(ItemStack stack, InteractionHand hand) {

    public boolean isMainHand() {
        return hand == InteractionHand.MAIN_HAND;
    }

    public boolean isOffHand() {
        return hand == InteractionHand.OFF_HAND;
    }

    public InteractionHand inverseHand() {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
}
