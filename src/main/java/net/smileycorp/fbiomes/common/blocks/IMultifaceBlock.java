package net.smileycorp.fbiomes.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMultifaceDirection;

import java.util.List;
import java.util.function.Function;

public interface IMultifaceBlock<T extends Block & IMultifaceBlock> {
    
    AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    PropertyInteger META = PropertyInteger.create("meta", 0, 15);
    
    int getOrdinal();
    
    List<T> getBlocks();
    
    Class<T> multipartClass();
    
    String getName();
    
    default IBlockState getBlockStateFromFacing(EnumFacing... facings) {
        return getBlockStateFromMeta(EnumMultifaceDirection.getMeta(facings));
    }
    
    default IBlockState getBlockStateFromMeta(int meta) {
        return meta == 0 ? Blocks.AIR.getDefaultState() : getBlocks().get(meta / 16).getDefaultState().withProperty(META, meta % 16);
    }
    
    static boolean hasFacing(IBlockState state, EnumFacing facing) {
        return (getMeta(state) & EnumMultifaceDirection.getFlag(facing)) > 0;
    }
    
    static EnumFacing[] getFacings(IBlockState state) {
        return EnumMultifaceDirection.getFacings(getMeta(state));
    }
    
    static int getMeta(EnumFacing... facings) {
        int meta = 0;
        for (EnumFacing facing : facings) meta += EnumMultifaceDirection.getFlag(facing);
        return meta;
    }
    
    static int getMeta(IBlockState state) {
        if (!(state.getBlock() instanceof IMultifaceBlock)) return 0;
        return ((IMultifaceBlock) state.getBlock()).getOrdinal() * 16 + state.getValue(META);
    }
    
    static <T extends Block & IMultifaceBlock> List<T> create(Function<Integer, T> instance) {
        List<T> blocks = Lists.newArrayList();
        for (int i = 0; i < 4; i++) blocks.add(instance.apply(i));
        return blocks;
    }
    
}
