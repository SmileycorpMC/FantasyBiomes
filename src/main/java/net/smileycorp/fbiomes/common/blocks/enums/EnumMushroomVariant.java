package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public enum EnumMushroomVariant implements IStringSerializable {
    TOADSTOOL("toadstool", FBiomesBlocks.TOADSTOOL),
    PURPLE("purple", FBiomesBlocks.PURPLE_SHROOM),
    GREEN("green", FBiomesBlocks.GREEN_SHROOM);
    
    final String name;
    final int meta;
    final Block drop;
    
    EnumMushroomVariant(String name, Block drop) {
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
    
    public float getMaxBounce() {
        return this == TOADSTOOL ? 3.5f : 2;
    }
    
    public float getBounceSpeed() {
        return this == TOADSTOOL ? 1.75f : 1.5f;
    }
    
}
