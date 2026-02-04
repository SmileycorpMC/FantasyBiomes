package net.smileycorp.phantasiai.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieWorkshop;
import net.smileycorp.phantasiai.common.entities.PixieData;
import net.smileycorp.phantasiai.common.inventory.ContainerPixieTable;
import net.smileycorp.phantasiai.common.network.PacketHandler;
import net.smileycorp.phantasiai.common.network.PixieTableEnableMessage;

import java.io.IOException;
import java.util.List;

public class GuiPixieWorkshop extends GuiContainer {
    
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/container/pixie_table.png");
    private final List<GuiPixieHouse> houses = Lists.newArrayList();
    private final TilePixieWorkshop tile;
    
    public GuiPixieWorkshop(TilePixieWorkshop tile, ContainerPixieTable inventorySlotsIn) {
        super(inventorySlotsIn);
        this.tile = tile;
        xSize = 176;
        ySize = 176;
    }
    
    public void addPixieHouse(int index, int x, int y, int textureX, int textureY, int width, int height) {
        houses.add(new GuiPixieHouse(x, y, textureX, textureY, width, height, index, tile));
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(new GuiButtonPixieAutomation(tile, guiLeft + 114, guiTop + 53));
        addPixieHouse(0, guiLeft + 84, guiTop + 14, 177, 14, 14, 14);
        addPixieHouse(1, guiLeft + 101, guiTop + 6, 194, 6, 13, 11);
        addPixieHouse(2, guiLeft + 116, guiTop + 13, 209, 13, 15, 15);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id != 0) return;
        PacketHandler.NETWORK_INSTANCE.sendToServer(new PixieTableEnableMessage(tile.getPos(), !tile.isActive()));
        button.playPressSound(mc.getSoundHandler());
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.disableDepth();
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        houses.forEach(GuiPixieHouse::draw);
        //slot bowls
        for (int i = 11; i < 18; i++) {
            Slot slot = inventorySlots.getSlot(i);
            if (slot.getHasStack()) continue;
            drawTexturedModalRect(guiLeft + slot.xPos, guiTop + slot.yPos,
                    177, 30, 16, 16);
        }
        if (tile.getCraftingProgress() > 0) drawTexturedModalRect((width - xSize) / 2 + 78, (height - ySize) / 2 + 47,
                177, 47, (int)(tile.getCraftingProgress() * 57), 7);
        int food = tile.getFoodTimerProgress();
        if (food > 0) {
            //bowl
            drawTexturedModalRect(guiLeft + 92, guiTop + 54,
                    177, 54, 29, 13);
            //soup
            drawTexturedModalRect(guiLeft + 94, guiTop + 62 - food,
                    179, 78 - food, 25, food);
        }
        if (tile.getEfficiency() > 1) {
            String text = "x" +  String.format("%.2f", tile.getEfficiency());
            mc.fontRenderer.drawStringWithShadow(text,
                    guiLeft + 106 - mc.fontRenderer.getStringWidth(text) / 2, guiTop + 37, 0xFFEFC772);
        }
        GlStateManager.enableDepth();
    }
    
    @Override
    protected void renderHoveredToolTip(int x, int y) {
        if (tile.getEfficiency() > 1 && x >= guiLeft + 94 && y >= guiTop + 33 && x <= guiLeft + 120 && y <= guiTop + 47) {
            List<String> text = Lists.newArrayList();
            if (tile.getBaseEfficiency() > 1) text.add(new TextComponentTranslation("tooltip.phantasiai.efficiency.food",
                    tile.getLastConsumedFood().getDisplayName(), "\u00a76" + String.format("%.2f", tile.getBaseEfficiency())).getFormattedText());
            for (int i = 0; i < tile.getPixieCount(); i++) {
                PixieData pixie = tile.getPixie(i);
                text.add(new TextComponentTranslation("tooltip.phantasiai.efficiency.pixie",
                        new TextComponentTranslation(pixie.hasName() ? pixie.getName() :
                                I18n.format("entity.phantasiai.pixie.name") + " " + (i + 1)).setStyle(new Style()
                                .setColor(pixie.getVariant().getRarity().getColor())),
                        "\u00a76" + String.format("%.2f", pixie.getEfficiency())).getFormattedText());
            }
            drawHoveringText(text, x, y, fontRenderer);
        }

        for (GuiPixieHouse house : houses) if (house.isHovered(x, y)) {
            drawHoveringText(house.getTooltipText(), x, y, fontRenderer);
            return;
        }
        super.renderHoveredToolTip(x, y);
    }

}
