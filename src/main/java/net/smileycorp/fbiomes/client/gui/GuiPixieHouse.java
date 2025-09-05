package net.smileycorp.fbiomes.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.fbiomes.common.blocks.tiles.TilePixieWorkshop;
import net.smileycorp.fbiomes.common.entities.PixieData;

import java.util.List;

public class GuiPixieHouse extends Gui {
    
    private final int x, y, textureX, textureY, width, height, index;
    private final TilePixieWorkshop tile;
    
    public GuiPixieHouse(int x, int y, int textureX, int textureY, int width, int height, int index, TilePixieWorkshop tile) {
        this.x = x;
        this.y = y;
        this.textureX = textureX;
        this.textureY = textureY;
        this.width = width;
        this.height = height;
        this.index = index;
        this.tile = tile;
    }
    
    private boolean isActive() {
        return tile.getPixieCount() > index;
    }
    
    public void draw() {
        if (!isActive()) return;
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        if (!isActive()) return false;
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
    
    public List<String> getTooltipText() {
        List<String> tooltips = Lists.newArrayList();
        PixieData pixie = tile.getPixie(index);
        tooltips.add(pixie.hasName() ? pixie.getName() : new TextComponentTranslation("entity.fbiomes.pixie.name").getFormattedText());
        pixie.addTooltips(tooltips);
        tooltips.add(new TextComponentTranslation("tooltip.fbiomes.efficiency",
                String.format("%.2f", (pixie.getEfficiency()))).getFormattedText());
        return tooltips;
    }
    
}
