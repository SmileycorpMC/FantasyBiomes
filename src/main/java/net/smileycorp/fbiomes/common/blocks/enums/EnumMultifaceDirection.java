package net.smileycorp.fbiomes.common.blocks.enums;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Arrays;

public enum EnumMultifaceDirection {
    DOWN("down", EnumFacing.DOWN, new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1)),
    UP("up", EnumFacing.UP, new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1)),
    NORTH("north", EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D)),
    SOUTH("south", EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1)),
    WEST("west", EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1)),
    EAST("east", EnumFacing.EAST, new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1));
    
    private final PropertyBool property;
    private final EnumFacing facing;
    private final AxisAlignedBB boundingBox;
    
    public byte getFlag() {
        return flag;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }
    
    public EnumFacing getFacing() {
        return facing;
    }
    
    public PropertyBool getProperty() {
        return property;
    }
    
    private final byte flag;
    
    EnumMultifaceDirection(String name, EnumFacing facing, AxisAlignedBB boundingBox) {
        property = PropertyBool.create(name);
        this.facing = facing;
        this.boundingBox = boundingBox;
        flag = (byte) Math.pow(2, ordinal());
    }
    
    public static EnumMultifaceDirection get(EnumFacing facing) {
        return values()[facing.ordinal()];
    }
    
    public static PropertyBool getProperty(EnumFacing facing) {
        return get(facing).getProperty();
    }
    
    public static AxisAlignedBB getBoundingBox(EnumFacing facing) {
        return get(facing).getBoundingBox();
    }
    
    public static int getFlag(EnumFacing facing) {
        return get(facing).getFlag();
    }
    
    public static int getMeta(EnumFacing... facings) {
        int meta = 0;
        for (EnumFacing facing : facings) meta += get(facing).getFlag();
        return meta;
    }
    
    public static IProperty[] properties() {
        return Arrays.stream(values()).map(EnumMultifaceDirection::getProperty).toArray(IProperty[]::new);
    }
    
    public static IBlockState getDefaultState(IBlockState state) {
        for (EnumMultifaceDirection dir : values()) state = state.withProperty(dir.getProperty(), false);
        return state;
    }
    
    public static EnumFacing[] getFacings(int meta) {
        return Arrays.stream(values()).filter(dir -> (dir.getFlag() & meta) > 0)
                .map(EnumMultifaceDirection::getFacing).toArray(EnumFacing[]::new);
    }
    
}
