package net.smileycorp.fbiomes.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.inventory.ContainerPixieTable;

import java.io.IOException;

public class GuiPixieTable extends GuiContainer {
    
    private static final ResourceLocation GUI_TEXTURE = Constants.loc("textures/gui/container/pixie_table.png");

    public GuiPixieTable(ContainerPixieTable inventorySlotsIn) {
        super(inventorySlotsIn);
        xSize = 176;
        ySize = 176;
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(
                new GuiButtonPixieAutomation(
                        0,
                        guiLeft + 114,
                        guiTop + 53,
                        18,
                        12,
                        GUI_TEXTURE
                )
        );

        addButton(
                new GuiPixieHouse(
                        1,
                        guiLeft + 84,
                        guiTop + 14,
                        177,
                        14,
                        14,
                        14,
                        GUI_TEXTURE
                )
        );
        addButton(
                new GuiPixieHouse(
                        2,
                        guiLeft + 101,
                        guiTop + 6,
                        194,
                        6,
                        13,
                        11,
                        GUI_TEXTURE
                )
        );
        addButton(
                new GuiPixieHouse(
                        3,
                        guiLeft + 116,
                        guiTop + 13,
                        209,
                        13,
                        15,
                        15,
                        GUI_TEXTURE
                )
        );
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                ((GuiButtonPixieAutomation) button).toggle();
                button.playPressSound(mc.getSoundHandler());
                break;
            case 1:
            case 2:
            case 3:
                ((GuiPixieHouse) button).toggle();
                button.playPressSound(mc.getSoundHandler());
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        /*String s = I18n.format("tile.pixietable.name");
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        fontRenderer.drawString(
                ((ContainerPixieTable) inventorySlots).playerInv.getDisplayName().getUnformattedText(),
                8,
                ySize - 96 + 2,
                4210752
        );*/
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(GUI_TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }


}
