package net.smileycorp.fbiomes.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPixieHouse extends GuiButton {
    private final int textureX;
    private final int textureY;
    private final ResourceLocation texture;
    public boolean toggled = false;

    public GuiPixieHouse(int buttonId, int x, int y, int textureX, int textureY, int width, int height, ResourceLocation texture) {
        super(buttonId, x, y, width, height, "");
        this.textureX = textureX;
        this.textureY = textureY;
        this.texture = texture;
    }

    public void toggle() {
        toggled = !toggled;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        //this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        mc.getTextureManager().bindTexture(texture);
        GlStateManager.disableDepth();

        if(toggled) {
            drawTexturedModalRect(x, y, textureX, textureY, this.width, this.height);
        }

        /*if(hovered) {
            drawTexturedModalRect(x, y, 206, 68, this.width, this.height);
        }*/

        GlStateManager.enableDepth();
    }
}
