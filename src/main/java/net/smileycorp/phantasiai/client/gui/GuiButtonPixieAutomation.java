package net.smileycorp.phantasiai.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieWorkshop;

public class GuiButtonPixieAutomation extends GuiButton {

    private final TilePixieWorkshop tile;

    public GuiButtonPixieAutomation(TilePixieWorkshop tile, int x, int y) {
        super(0, x, y, 18, 12, "");
        this.tile = tile;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        mc.getTextureManager().bindTexture(GuiPixieWorkshop.TEXTURE);
        GlStateManager.disableDepth();
        if (tile.isActive()) drawTexturedModalRect(x, y, 206, 53, width, height);
        if (hovered) drawTexturedModalRect(x, y, 206, 68, width, height);
        GlStateManager.enableDepth();
    }
}
