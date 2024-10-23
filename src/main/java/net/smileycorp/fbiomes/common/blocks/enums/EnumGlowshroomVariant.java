package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public enum EnumGlowshroomVariant implements IStringSerializable {
    BLUE("blue", FBiomesBlocks.BLUE_GLOWSHROOM),
    GREEN("green", FBiomesBlocks.GREEN_GLOWSHROOM),
    ORANGE("orange", FBiomesBlocks.ORANGE_GLOWSHROOM),
    PINK("pink", FBiomesBlocks.PINK_GLOWSHROOM),
    PURPLE("purple", FBiomesBlocks.PURPLE_GLOWSHROOM);
    
    final String name;
    final int meta;
    final Block drop;
    
    EnumGlowshroomVariant(String name, Block drop) {
        this.name = name;
        this.meta = this.ordinal();
        this.drop = drop;
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
    
}
