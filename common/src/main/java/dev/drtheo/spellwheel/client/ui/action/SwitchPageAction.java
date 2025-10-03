package dev.drtheo.spellwheel.client.ui.action;

import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import at.petrak.hexcasting.common.msgs.MsgShiftScrollC2S;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class SwitchPageAction implements Action {

    private final int targetPage;

    public SwitchPageAction(int page) {
        this.targetPage = page;
    }

    @Override
    public void run(Minecraft client) {
        if (client.player == null)
            return;

        ItemStack stack;
        boolean mainHand = false;
        boolean offHand = false;

        if ((stack = client.player.getOffhandItem()).getItem() instanceof ItemSpellbook) {
            offHand = true;
        } else if ((stack = client.player.getMainHandItem()).getItem() instanceof ItemSpellbook) {
            mainHand = true;
        }

        if (!mainHand && !offHand)
            return;

        int currentPage = ItemSpellbook.getPage(stack, -1);

        if (currentPage == -1 || targetPage == currentPage)
            return;

        int sign = currentPage > targetPage ? 1 : -1;

        while (targetPage != currentPage) {
            IClientXplatAbstractions.INSTANCE.sendPacketToServer(new MsgShiftScrollC2S(
                    (mainHand ? sign : 0), (offHand ? sign : 0), false, false, false));
            currentPage -= sign;
        }
    }
}
