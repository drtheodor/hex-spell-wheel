package dev.drtheo.spellwheel.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class HandUtil {

    private static final Minecraft client = Minecraft.getInstance();

    public static StackInHand getOffPriorityItem(Predicate<ItemStack> predicate) {
        ItemStack stack;

        if (predicate.test(stack = client.player.getOffhandItem())) {
            return new StackInHand(stack, InteractionHand.OFF_HAND);
        } else if (predicate.test(stack = client.player.getMainHandItem())) {
            return new StackInHand(stack, InteractionHand.MAIN_HAND);
        }

        return null;
    }

    public static StackInHand getOffPriorityItem(Class<?> clazz) {
        return getOffPriorityItem(stack -> clazz.isInstance(stack.getItem()));
    }

    public static StackInHand getItem(Predicate<ItemStack> predicate) {
        ItemStack stack;

        if (predicate.test(stack = client.player.getMainHandItem())) {
            return new StackInHand(stack, InteractionHand.MAIN_HAND);
        } else if (predicate.test(stack = client.player.getOffhandItem())) {
            return new StackInHand(stack, InteractionHand.OFF_HAND);
        }

        return null;
    }

    public static StackInHand getItem(Class<?> clazz) {
        return getItem(stack -> clazz.isInstance(stack.getItem()));
    }

    public static StackInHand getPriorityItem(Class<?> clazz, InteractionHand priority) {
        return priority == InteractionHand.MAIN_HAND ? getItem(clazz) : getOffPriorityItem(clazz);
    }
}
