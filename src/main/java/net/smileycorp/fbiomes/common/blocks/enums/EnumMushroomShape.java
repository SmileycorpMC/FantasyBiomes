package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumMushroomShape implements IStringSerializable {
    CAP("cap"),
    STEM("stem"),
    SPOT("spot");
    
    final String name;
    final int meta;
    
    EnumMushroomShape(String name) {
        this.name = name;
        this.meta = this.ordinal();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public int getMeta() {
        return meta;
    }
    
    public boolean isBouncy() {
        return this != STEM;
    }
    
}
