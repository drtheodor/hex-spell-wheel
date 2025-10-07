package dev.drtheo.spellwheel.client.ui;

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

    public void simulateClick(int index) {
        this.click(this.widgets.widgets[index]);
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