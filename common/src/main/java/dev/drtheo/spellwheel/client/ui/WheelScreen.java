package dev.drtheo.spellwheel.client.ui;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WheelScreen extends Screen {

    private final WidgetSet widgets;

    public WheelScreen(WidgetSet set) {
        super(Component.empty());

        this.widgets = set;
    }

    @Override
    protected void init() {
        widgets.forEach((slot, widget) -> addRenderableWidget(new WheelOptionWidget(
                slot.getX(width), slot.getY(height), widget, slot.getXOffset(), slot.getYOffset())));
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (super.keyPressed(i, j, k))
            return true;

        if (minecraft.options.keyInventory.matches(i, j)) {
            super.onClose();
            return true;
        }

        KeyMapping[] hotbar = minecraft.options.keyHotbarSlots;

        for (int l = 0; l < hotbar.length; l++) {
            if (hotbar[l].matches(i, j)) {
                this.simulateClick(l);
                return true;
            }
        }

        return false;
    }

    public void simulateClick(int index) {
        Widget widget = this.widgets.get(index);

        if (widget == null)
            return;

        this.click(widget);
    }

    protected void click(Widget widget) {
        if (!widget.keepOpened() && !Screen.hasShiftDown())
            this.onClose();

        widget.run(Minecraft.getInstance());
    }

    protected void rightClick(Widget widget) {
        widget.runAlt(Minecraft.getInstance());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}