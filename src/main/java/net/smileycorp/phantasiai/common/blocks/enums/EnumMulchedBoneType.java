package net.smileycorp.phantasiai.common.blocks.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumMulchedBoneType implements IStringSerializable {

    MUD("mud"),
    DIRT("dirt");

    private final String name;

    EnumMulchedBoneType(String name) {
        this.name = name;
    }

    public boolean slowsPlayer() {
        return this == MUD;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EnumMulchedBoneType get(int meta) {
        return meta < values().length ? values()[meta] : MUD;
    }

}
