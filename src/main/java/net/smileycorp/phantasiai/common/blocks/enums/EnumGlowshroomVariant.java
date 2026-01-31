package net.smileycorp.phantasiai.common.blocks.enums;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;

public enum EnumGlowshroomVariant implements IStringSerializable {
    BLUE("blue", PhantasiaiBlocks.BLUE_GLOWSHROOM, 0x47E1D3),
    GREEN("green", PhantasiaiBlocks.GREEN_GLOWSHROOM, 0xA8F337),
    YELLOW("yellow", PhantasiaiBlocks.YELLOW_GLOWSHROOM, 0xF9E586),
    PINK("pink", PhantasiaiBlocks.PINK_GLOWSHROOM, 0xFA43A3),
    PURPLE("purple", PhantasiaiBlocks.PURPLE_GLOWSHROOM, 0xA439FF);
    
    final String name;
    final int meta;
    final Block drop;
    final int colour;
    
    EnumGlowshroomVariant(String name, Block drop, int colour) {
        this.name = name;
        this.meta = this.ordinal();
        this.drop = drop;
        this.colour = colour;
    }
    
    public static EnumGlowshroomVariant get(int amplifier) {
        return values()[amplifier % values().length];
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getMeta() {
        return meta;
    }
    
    public Item getDrop() {
        return Item.getItemFromBlock(drop);
    }
    
    public int getColour() {
        return colour;
    }
    
}
