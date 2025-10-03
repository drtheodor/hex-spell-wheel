package dev.drtheo.spellwheel.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public class WheelScreen extends Screen {

    final WidgetSet widgets;
    final String subPath;

    public WheelScreen(String name, WidgetSet set) {
        super(Component.empty());

        this.subPath = name;
        this.widgets = set;
    }

    @Override
    protected void init() {
        widgets.forEach((slot, widget) -> {
            WheelOptionWidget option = new WheelOptionWidget(slot.getX(width), slot.getY(height), widget, slot.getXOffset(), slot.getYOffset());
            addRenderableWidget(option);
        });
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        Language language = Language.getInstance();
        String key = turnIntoTitleKey();
        if (language.has(key) && !isRoot()){
            renderTitle(context, Component.translatable(key));
        }
    }

    public void renderTitle(GuiGraphics context, Component text){
        int centerY = height / 2;
        int centerX = width / 2;
        int y = centerY - 90;
        int x = centerX - (font.width(text) / 2);
        context.drawString(font, text, x, y, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isRoot(){
        return subPath.isEmpty();
    }

    public String turnIntoTitleKey(){
        return "wheel.title." + subPath.replace("/", ".");
    }
}