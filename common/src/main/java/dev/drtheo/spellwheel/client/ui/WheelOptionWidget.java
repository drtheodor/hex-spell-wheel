package dev.drtheo.spellwheel.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.drtheo.spellwheel.SpellWheel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WheelOptionWidget extends Button {

    public static final ResourceLocation EMPTY = SpellWheel.id("textures/gui/empty_widget.png");

    public final Widget widget;
    private final int xOffset;
    private final int yOffset;
    private float animationProgress = 0;

    protected WheelOptionWidget(int x, int y, Widget widget, int xOffset, int yOffset) {
        super(x, y, 32, 32, Component.empty(), button -> { }, DEFAULT_NARRATION);

        this.widget = widget;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.setTooltip(Tooltip.create(widget.label()));
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        PoseStack matrices = context.pose();
        matrices.pushPose();

        if (animationProgress < 1) animationProgress += (float) 0.25 * delta;
        matrices.translate(xOffset * animationProgress, yOffset * animationProgress, 0);
        isHovered = isMouseOver(mouseX, mouseY);
        renderButton(context);
        matrices.popPose();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        float xOff = xOffset * animationProgress;
        float yOff = yOffset * animationProgress;
        return this.active && this.visible && mouseX >= (double) this.getX() + xOff && mouseY >= (double) this.getY() + yOff && mouseX < (double) (this.getX() + this.width + xOff) && mouseY < (double) (this.getY() + this.height + yOff);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY);
    }

    protected void renderButton(GuiGraphics context) {
        boolean displayHovered = isHovered() && widget.actions().isPresent();
        int color = (180 << 24) | (displayHovered ? 0x3c8527 : 0x2c2c2c);
        int x = getX();
        int y = getY();
        int contentX = x + 8;
        int contentY = y + 8;

        context.fill(x, y, x + width, y + height, color);
        ItemStack preview = widget.preview();

        if (preview.isEmpty()) {
            context.blit(EMPTY, contentX, contentY, 0, 0, 16, 16, 16, 16);
            return;
        }

        context.renderItem(preview, contentX, contentY);
    }

    @Override
    public void onPress() {
        Minecraft client = Minecraft.getInstance();

        if (!widget.keepOpened() && !Screen.hasShiftDown())
            client.screen.onClose();

        widget.run(client);
    }
}