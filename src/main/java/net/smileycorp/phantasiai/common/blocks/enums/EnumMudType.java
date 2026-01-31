package net.smileycorp.phantasiai.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumMudType implements IStringSerializable {
    
    MUD("mud"),
    PEAT("peat");
    
    private final String name;
    
    EnumMudType(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static EnumMudType get(int meta) {
        return meta < values().length ? values()[meta] : MUD;
    }
    
}
