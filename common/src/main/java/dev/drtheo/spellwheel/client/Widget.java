package dev.drtheo.spellwheel.client;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.function.Function;

public record Widget(Component label, Either<Holder<Item>, ItemStack> preview, Action actions, Optional<WidgetSlot> takenSlot, boolean keepOpened) {

    private static final ItemStack EMPTY_STACK = new ItemStack(Items.BARRIER);

    public Widget(Component label, Item item, Action actions) {
        this(label, Either.right(new ItemStack(item)), actions, Optional.empty(), false);
    }

    public void run(Minecraft client) {
        if (actions != null)
            actions.run(client);
    }

    public ItemStack getStack(){
        return preview.map(ItemStack::new, Function.identity());
    }

    public boolean hasSlotTaken(){
        return takenSlot.isPresent();
    }

    public static Widget empty() {
        return new Widget(I18n.emptyWidget(), Either.right(EMPTY_STACK), null, Optional.empty(), true);
    }
}