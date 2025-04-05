package net.smileycorp.fbiomes.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.fbiomes.common.blocks.tile.TileMysticStump;
import net.smileycorp.fbiomes.common.entities.EntityPixie;

import java.util.List;

public class GuiPixieHouse extends Gui {
    
    private final int x, y, textureX, textureY, width, height, index;
    private final TileMysticStump tile;
    
    public GuiPixieHouse(int x, int y, int textureX, int textureY, int width, int height, int index, TileMysticStump tile) {
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
        EntityPixie pixie = tile.getPixie(index);
        tooltips.add(pixie.getName());
        tooltips.add(new TextComponentTranslation("item.fbiomes.pixie_bottle.tooltip.variant",
                new TextComponentTranslation("entity.fbiomes.pixie.variant."
                        + pixie.getVariant().getName())).getFormattedText());
                tooltips.add(new TextComponentTranslation("item.fbiomes.pixie_bottle.tooltip.health",
                        pixie.getHealth(),  pixie.getMaxHealth()).getFormattedText());
        return tooltips;
    }
    
}
