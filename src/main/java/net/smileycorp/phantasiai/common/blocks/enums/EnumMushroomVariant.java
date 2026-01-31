package net.smileycorp.phantasiai.common.blocks.enums;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;

public enum EnumMushroomVariant implements IStringSerializable {
    TOADSTOOL("toadstool", PhantasiaiBlocks.TOADSTOOL),
    PURPLE("purple", PhantasiaiBlocks.PURPLE_SHROOM),
    GREEN("green", PhantasiaiBlocks.GREEN_SHROOM);
    
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
        return this == TOADSTOOL ? 1.5f : 1.1f;
    }
    
    public float getBounceSpeed() {
        return this == TOADSTOOL ? 1.5f : 1.25f;
    }
    
}
