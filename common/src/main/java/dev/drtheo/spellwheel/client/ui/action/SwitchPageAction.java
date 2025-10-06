package dev.drtheo.spellwheel.client.ui.action;

import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import at.petrak.hexcasting.common.msgs.MsgShiftScrollC2S;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.drtheo.spellwheel.client.util.HandUtil;
import dev.drtheo.spellwheel.client.util.StackInHand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;

public class SwitchPageAction implements Action {

    private final int targetPage;

    public SwitchPageAction(int page) {
        this.targetPage = page;
    }

    @Override
    public void run(Minecraft client) {
        if (client.player == null)
            return;

        StackInHand spellbook = HandUtil.getOffPriorityItem(ItemSpellbook.class);

        if (spellbook == null)
            return;

        int currentPage = ItemSpellbook.getPage(spellbook.stack(), -1);

        if (currentPage == -1)
            return;

        if (targetPage != currentPage) {
            int sign = currentPage > targetPage ? 1 : -1;

            MsgShiftScrollC2S packet = new MsgShiftScrollC2S(
                    spellbook.isMainHand() ? sign : 0, spellbook.isOffHand() ? sign : 0,
                    false, false, false
            );

            while (targetPage != currentPage) {
                IClientXplatAbstractions.INSTANCE.sendPacketToServer(packet);
                currentPage -= sign;
            }
        }

        if (Screen.hasControlDown()) this.tryCast(client, spellbook);
    }

    private void tryCast(Minecraft client, StackInHand spellbook) {
        InteractionHand trinketHand = spellbook.inverseHand();
        StackInHand trinket = HandUtil.getPriorityItem(ItemPackagedHex.class, trinketHand);

        if (trinket == null)
            return;

        client.gameMode.useItem(client.player, trinketHand);
    }
}
