package net.smileycorp.fbiomes.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.tile.TileMysticStump;
import net.smileycorp.fbiomes.common.inventory.ContainerPixieTable;

import java.io.IOException;
import java.util.List;

public class GuiPixieTable extends GuiContainer {
    
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/container/pixie_table.png");
    private final List<GuiPixieHouse> houses = Lists.newArrayList();
    private final TileMysticStump tile;
    
    public GuiPixieTable(TileMysticStump tile, ContainerPixieTable inventorySlotsIn) {
        super(inventorySlotsIn);
        this.tile = tile;
        xSize = 176;
        ySize = 176;
        addPixieHouse(1, guiLeft + 84, guiTop + 14, 177, 14, 14, 14);
        addPixieHouse(2, guiLeft + 101, guiTop + 6, 194, 6, 13, 11);
        addPixieHouse(3, guiLeft + 116, guiTop + 13, 209, 13, 15, 15);
    }
    
    public void addPixieHouse(int index, int x, int y, int textureX, int textureY, int width, int height) {
        houses.add(new GuiPixieHouse(x, y, textureX, textureY, width, height, index, tile));
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(new GuiButtonPixieAutomation(0, guiLeft + 114, guiTop + 53, 18, 12, TEXTURE));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id != 0) return;
        ((GuiButtonPixieAutomation) button).toggle();
        button.playPressSound(mc.getSoundHandler());
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.disableDepth();
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        houses.forEach(GuiPixieHouse::draw);
        for (int i = 12; i < 19; i++) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.getHasStack()) continue;
            drawTexturedModalRect(guiLeft + slot.xPos, guiTop + slot.yPos,
                    177, 30, 16, 16);
        }
        if (tile.getCraftingProgress() <= 0) drawTexturedModalRect((width - xSize) / 2 + 78, (height - ySize) / 2 + 47,
                177, 47, (int)(tile.getCraftingProgress() * 57), 7);
        GlStateManager.enableDepth();
    }
    
    @Override
    protected void renderHoveredToolTip(int x, int y) {
        for (GuiPixieHouse house : houses) if (house.isHovered(x, y)) {
            drawHoveringText(house.getTooltipText(), x, y, fontRenderer);
            return;
        }
        super.renderHoveredToolTip(x, y);
    }

}
